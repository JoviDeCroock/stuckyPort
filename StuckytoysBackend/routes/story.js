/**
 * Created by jovi on 11/1/2016.
 */
var mongoose = require('mongoose');
var express = require('express');
var jwt = require('express-jwt');
var router = express.Router();
var path = require('path');
var mime = require('mime');

var config = require('../config/config');
var User = mongoose.model('User');
var Story = mongoose.model('Story');
var Scene = mongoose.model('Scene');
var Theme = mongoose.model('Theme');
var Widget = mongoose.model('Widget');
var Figure = mongoose.model('Figure');
var WidgetFile = mongoose.model('WidgetFile');

// configuring auth
var auth = jwt({secret:config.secret,userProperty:config.userProperty});

// Params
router.param('story', function(req,res,next,id)
{
    var query = Story.findById(id);
    query.exec(function(err, story)
    {
        if(err) {return next(err);}
        if(!story) {return next(new Error('Kan het gekozen verhaal niet vinden.'));}
        req.story = story;
        return next();
    });
});

router.param('widgetFile', function(req,res,next,id)
{
    var query = WidgetFile.findById(id);
    query.exec(function(err, wfile)
    {
        if(err) {return next(err);}
        if(!wfile) {return next(new Error('Kan de gekozen file niet vinden.'));}
        req.widgetFile = wfile;
        return next();
    });
});

router.param('theme', function(req,res,next,id)
{
    var query = Theme.findById(id);
    query.exec(function(err, theme)
    {
        if(err) {return next(err);}
        if(!theme) {return next(new Error('Kan het gekozen thema niet vinden.'));}
        req.theme = theme;
        return next();
    });
});

router.param('widget', function(req,res,next,id)
{
    var query = Widget.findById(id);
    query.exec(function(err, widget)
    {
        if(err) {return next(err);}
        if(!widget) {return next(new Error('Kan het gekozen widget niet vinden.'));}
        req.widget = widget;
        return next();
    });
});

router.param('scene', function(req,res,next,id)
{
    var query = Scene.findById(id);
    query.exec(function(err, scene)
    {
        if(err) {return next(err);}
        if(!scene) {return next(new Error('Kan de gekozen scene niet vinden.'));}
        req.scene = scene;
        return next();
    });
});

// API methods
router.post('/createStory', auth, function(req,res,next)
{
    var story = new Story();
    story.name = req.body.name;
    story.scenes = [];
    story.themes = [];
    if(req.body.scenes.length !== 0)
    {
        req.body.scenes.forEach(function(entry)
        {
            var scene = new Scene();
            scene.sceneNr = entry.sceneNr;
            scene.widgets = [];
            scene.figures = [];
            entry.widgets.forEach(function(widgetEntry)
            {
                var widget = Widget.findById(widgetEntry._id);
                scene.widgets.push(widget);
            });
            req.body.figures.forEach(function(figureEntry)
            {
                var figure = Figure.findById(figureEntry._id);
                scene.figures.push(figure);
            });
            scene.save(function(err)
            {
                if(err){console.log(err);}
            });
            story.scenes.push(scene);
        });
    }
    req.body.themes.forEach(function(themeEntry)
    {
        var theme = Theme.findById(themeEntry._id);
        story.themes.push(theme);
    });
    story.saveDate(req.body.date);
    story.save(function(err)
    {
        if(err) {console.log(err);}
    });
    res.json(story);
});

router.post('/:story/addScene', auth, function(req,res,next) {
    var story = req.story;
    var x = req.story.scenes.length;
    var newScene = new Scene();

    newScene.widgets = [];
    req.body.widgets.forEach(function(widgetEntry)
    {
        var widget = Widget.findById(widgetEntry._id);
        newScene.widgets.push(widget);
    });

    newScene.sceneNr = x;
    newScene.figures = [];
    req.body.figures.forEach(function(figureEntry)
    {
        var figure = Figure.findById(figureEntry._id);
        newScene.figures.push(figure);
    });
    newScene.save(function(err)
    {
        if(err){console.log(err);}
    });
    story.scenes.push(newScene);
    story.save(function(err)
    {
        if(err) {console.log(err);}
    });
    res.json(story);
});

router.get('/getStory/:story', auth, function(req,res,next)
{
    req.story.populate('scenes', function(err, story)
    {
        Story.populate(story,
            {
                path:'themes',
                model:'Theme'
            },function(err, x)
            {
                Story.populate(x,
                    {
                        path:'scenes.figures',
                        model:'Figure'
                    },function(err, scene)
                    {
                        Story.populate(scene,
                            {
                                path:'scenes.widgets',
                                model:'Widget'
                            }, function(err, y)
                            {
                                Story.populate(y,
                                    {
                                        path:'scenes.widgets.widgetFiles',
                                        model:'WidgetFile'
                                    }, function(err, z)
                                    {
                                        Scene.populate(z,
                                            {
                                                path: 'scenes.figures.picture',
                                                model: 'Picture'
                                            }, function(err, figures)
                                            {
                                                res.json(figures);
                                            });
                                    });
                            });
                    });
            });
    });
});

/*Download widget per widget*/
router.get('/download/:widgetFile', auth, function(req,res, next)
{
    var file =  __dirname + '/downloads/' + req.widgetFile.type + '/' + req.widgetFile.fileName;
    res.download(file);
});

router.get('/widgetTypes', auth, function(req,res,next)
{
    var fs = require('fs');
    var path = __dirname + '/downloads/';
    fs.readdir(path, function(err, items) {
        res.json(items);
    });
});

router.post('/widgetsOfType', auth, function(req,res,next)
{
    var fs = require('fs');
    var path = __dirname + '/downloads/' + req.body.type;
    fs.readdir(path, function(err, items) {
        res.json(items);
    });
});

/*Element adders*/
router.post('/addWidget', auth, function(req,res,next)
{
    var w = new Widget();
    w.widgetFiles = [];
    w.id = req.body.id;
    req.body.widgetFiles.forEach(function(widgetFile)
    {
       var f = new WidgetFile();
        f.fileName = widgetFile.fileName;
        f.type = widgetFile.type;
        f.save(function(err)
        {
            if(err){console.log(err);}
        });
        w.files.push(f);
    });
    w.save(function(err)
    {
        if(err){console.log(err);}
        res.json(w);
    });
});

router.post('/addTheme', auth, function(req,res,next)
{
    var theme = Theme.find({name: req.body.theme.name});
    if(!theme)
    {
        var t = new Theme();
        t.name = req.body.name;
        t.description = req.body.description;
        t.save(function(err)
        {
            if(err){console.log(err);}
            res.json(t);
        });
    }else{
        res.json(theme);
    }
});

router.post('/editTheme', auth, function(req,res,next)
{
    var query = {_id: req.body.theme._id};
    Theme.findOneAndUpdate(query, req.body.theme,{upsert:true}, function(err, doc) {
            if (err) return res.send(500, {error: err});
            return res.send("succesfully saved");
        });
});

/*Getters voor elementen*/
router.get('/getAllStories', auth, function(req,res,next)
{
    /*TODO: populates*/
    Story.find(function(err, stories)
    {
        if(err){return next(err);}
        return res.json(stories);
    });
});

router.get("/getAllThemes", auth, function(req,res,next)
{
    Theme.find(function(err,themes)
    {
        if(err){return next(err);}
        return res.json(themes);
    });
});

router.get("/getAllWidgets", auth, function(req,res,next)
{
    Widget.find(function(err, widgets)
    {
        Widget.populate(widgets,
        {
            path:'widgetFiles',
            model:'WidgetFile'
        },function(err, x)
        {
            res.json(x);
        });
    });
});

router.get("/themes/:theme", auth, function(req,res,next)
{
    res.json(req.theme);
});

router.get("/widgets/:widget", auth, function(req,res,next)
{
    req.widget.populate('files', function(err, file)
    {
       res.json(file);
    });
});

router.get("/:theme/allStories", auth, function(req,res,next)
{
    /*TODO*/
});

router.post("/removeWidget/:widget", auth, function(req,res,next)
{
    Widget.remove({_id: req.widget._id}, function(err)
    {
        if (!err) {
            res.json('fail');
        }
        else {
            res.json('succes');
        }
    });
});

router.post("/removeTheme/:theme", auth, function(req,res,next)
{
    Theme.remove({_id: req.theme._id}, function(err)
    {
        if (!err) {
            res.json('fail');
        }
        else {
            res.json('succes');
        }
    });
});
/*
TODO:
*
* remove scene from story (scene nr aanpassen)*
* getAllStories populates
* allStories per theme
*
* */

module.exports = router;
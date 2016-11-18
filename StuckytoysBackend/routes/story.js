/**
 * Created by jovi on 11/1/2016.
 */
// DECLARATIONS
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
//END declarations

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
//END params

// API methods
router.post('/createStory', auth, function(req,res,next)
{
    if(!req.body.scenes || !req.body.name || !req.body.themes || !req.body.date){
        return res.status(400).json({message:'Vul alle velden in'});
    }
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
            scene.text = entry.text;
            entry.widgets.forEach(function(widgetEntry)
            {
                var widget = Widget.findById(widgetEntry._id);
                scene.widgets.push(widget);
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

router.post('/:story/addScene', auth, function(req,res,next)
{
    if(!req.body.widgets || !req.body.figures){
        return res.status(400).json({message:'Vul alle velden in'});
    }
    var story = req.story;
    var x = req.story.scenes.length;
    var newScene = new Scene();

    newScene.widgets = [];
    req.body.widgets.forEach(function(widgetEntry)
    {
        var widget = Widget.findById(widgetEntry._id);
        newScene.widgets.push(widget);
    });
    /*TODO: Rearrange scenenrs if >><<*/
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

router.post('/publish/:story', auth, function(req,res,next)
{
    var query = {_id: req.story._id};
    var x = req.story.published;
    if(!x)
    {
        req.story.published = true;
        Story.findOneAndUpdate(query, w,{upsert:true}, function(err, doc) {
            if (err) return res.send(500, {error: err});
            return res.send("succesfully published");
        });
    }
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

router.get('/getAllStories', auth, function(req,res,next)
{
    /*TODO: populates*/
    Story.find(function(err, stories)
    {
        Story.populate(stories,
            {
                path:'scenes',
                model:'Scene'
            }, function(err, scenes)
            {
                Story.populate(scenes,
                    {
                        path:'themes',
                        model:'Theme'
                    }, function(err, themes)
                    {
                        Story.populate(themes,
                            {
                                path:'picture',
                                model:'Picture'
                            }, function(err, pic)
                            {
                                Story.populate(pic,
                                    {
                                        path:'scenes.figures',
                                        model:'Figure'
                                    }, function(err, figures)
                                    {
                                        Story.populate(figures,
                                            {
                                                path:'scenes.widgets',
                                                model:'Widget'
                                            }, function(err, widgets)
                                            {
                                                Story.populate(widgets,
                                                    {
                                                        path:'scenes.widgets.widgetFiles',
                                                        model:'WidgetFile'
                                                    }, function(err, files)
                                                    {
                                                        Story.populate(files,
                                                            {
                                                                path:'scenes.figures.picture',
                                                                model:'Picture'
                                                            }, function(err, figPics)
                                                            {
                                                                res.json(figPics);
                                                            });
                                                    });
                                            });
                                    });
                            });
                    });
            });
    });
});

router.get("/:theme/allStories", auth, function(req,res,next)
{
    /*TODO*/
});

router.post('/editScene', auth, function(req,res,next)
{
    var query = {_id: req.body.scene._id};
    Scene.findOneAndUpdate(query, req.body.scene,{upsert:true}, function(err, doc) {
        if (err) return res.send(500, {error: err});
        return res.send("succesfully saved");
    });
});

router.post("/:story/removeScene/:scene", auth, function(req,res,next)
{
    var sceneNr = req.scene.sceneNr;
    var deleted = false;
    var query = {_id: req.story._id};
    Scene.remove({_id:req.scene._id}, function(err)
    {
        if(!err)
        {
            req.story.scenes.forEach(function(entry)
            {
                var x = req.story.scenes.indexOf(entry);
                if(entry.sceneNr === sceneNr)
                {
                    req.story.scenes.splice(x, 1);
                }
                if(entry.sceneNr>sceneNr)
                {
                    var y = entry.sceneNr-1;
                    req.story.scenes[x].sceneNr = y;
                }
            });
            Story.findOneAndUpdate(query, req.story,{upsert:true}, function(err, doc)
            {
                if(err)
                {
                    return err;
                }
            });
            res.json(req.story);
        }
    });
});
/*
 TODO:
 *
 * remove scene from story (scene nr aanpassen)
 * allStories per theme
 *
 * */

module.exports = router;
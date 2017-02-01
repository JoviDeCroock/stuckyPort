/**
 * Created by jovi on 11/1/2016.
 */
// DECLARATIONS
var mongoose = require('mongoose');
var express = require('express');
var jwt = require('express-jwt');
var router = express.Router();
var fs = require('fs');
var path = require('path');
var mime = require('mime');
var multi = require('connect-multiparty');

var config = require('../config/config');
var User = mongoose.model('User');
var Story = mongoose.model('Story');
var Scene = mongoose.model('Scene');
var Theme = mongoose.model('Theme');
var Widget = mongoose.model('Widget');
var WidgetFile = mongoose.model('WidgetFile');

// configuring auth
var auth = jwt({secret:config.secret,userProperty:config.userProperty});
var mp = multi();
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
router.param('user',function(req,res,next,id)
{
    var query = User.findById(id);
    query.exec(function(err,user){
        if(err){return next(err);}
        if(!user){return next(new Error('Kan de gekozen familie niet vinden.'));}
        req.user = user;
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
router.post('/createStory',auth,mp, function(req,res,next)
{
    if(!req.files.file ||!req.body.scenes || !req.body.name || !req.body.themes || !req.body.date || !req.body.duration || !req.body.price){
        return res.status(400).json({message:'Vul alle velden in'});
    }
    var story = new Story();
    story.name = req.body.name;
    story.price = req.body.price;
    story.scenes = [];
    story.themes = [];
    story.path = req.files.file.name;
    fs.readFile(req.files.file.path, function(err,data)
    {
        var fPath = path.join(__dirname, 'downloads', 'Afbeelding', story.path);
        fs.writeFile(fPath, data, function(err)
        {
            if (err) { return res.status(400).json({message: 'Upload failed'}); }
        });
    });
    story.duration = req.body.duration;
    if(req.body.scenes.length !== 0)
    {
        req.body.scenes.forEach(function(entry)
        {
            var scene = new Scene();
            scene.sceneNr = entry.sceneNr;
            scene.layout = entry.layout;
            scene.widgets = [];
            scene.figures = [];
            scene.hints = [];
            scene.text = entry.text;
            if(entry.hints)
            {
                entry.hints.forEach(function(hint)
                {
                    scene.hints.push(hint);
                });
            }
            if(entry.widgets)
            {
                entry.widgets.forEach(function(widgetEntry)
                {
                    scene.widgets.push(widgetEntry);
                });
            }
            scene.save(function(err)
            {
                if(err){console.log(err);}
            });
            story.scenes.push(scene);
        });
    }
    req.body.themes.forEach(function(themeEntry)
    {
        story.themes.push(themeEntry);
    });
    story.published = req.body.published;
    story.date = req.body.date;
    story.save(function(err)
    {
        if(err) {console.log(err);}
        res.json(story);
    });
});

router.post('/publish/:story', auth, function(req,res,next)
{
    var query = {_id: req.story._id};
    var x = req.story.published;
    if(!x)
    {
        req.story.published = true;
        Story.update(query, req.story ,{upsert:true}, function(err, doc) {
            if (err) { return res.status(400).json({ message: 'Er is iets fout gegaan' });}
            return res.json(req.story);
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
                                        res.json(z);
                            });
                    });
            });
    });
});

router.get('/download/:widgetFile', auth, function(req,res, next)
{
    var file =  __dirname + '/downloads/' + req.widgetFile.type + '/' + req.widgetFile.fileName;
    res.download(file);
});

router.get('/:user/getAllStories', auth, function(req,res,next)
{
    req.user.stories.find(function(err, stories)
    {
        Story.populate(stories,
            {
                path:'scenes',
                model:'Scene'
            }, function(err, scenes)
            {
              res.json(scenes);
                /*Story.populate(scenes,
                    {
                        path:'themes',
                        model:'Theme'
                    }, function(err, themes)
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
                                        res.json(files);
                                    });
                            });
                    });*/
            });
    });
});

router.get('/getAllStories', auth, function(req,res,next)
{
    Story.find(function (err, stories)
    {
        Story.populate(stories,
            {
                path: 'scenes',
                model: 'Scene'
            }, function (err, scenes)
            {
                Story.populate(scenes,
                    {
                        path: 'themes',
                        model: 'Theme'
                    }, function (err, themes)
                    {
                        Story.populate(themes,
                            {
                                path: 'scenes.widgets',
                                model: 'Widget'
                            }, function (err, widgets) {
                                Story.populate(widgets,
                                    {
                                        path: 'scenes.widgets.widgetFiles',
                                        model: 'WidgetFile'
                                    }, function (err, files) {
                                        res.json(files);
                                    });
                            });
                    });
            });
    });
});

router.get("/getPublishedStories", auth, function(req,res,next)
{
    Story.find({published: true}, function(err, stories)
    {
        Story.populate(stories,
            {
                path:'themes',
                model:'Theme'
            },
            function(err, themes)
            {
                Story.populate(themes,
                    {
                        path:'scenes',
                        model:'Scene'
                    },function(err, scenes)
                    {

                        Story.populate(scenes,
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
                                        res.json(files);
                                    });
                            });
                    });
            });
    });
});

router.post("/:user/buyStory/:story", auth,function(req,res,next)
{
    // if sufficient cash?
    req.user.stories.push(req.story);
    req.user.save(function(err)
    {
        console.log(err);
        res.json(req.user.stories);
    });
});

module.exports = router;

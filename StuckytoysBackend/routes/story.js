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

router.param('scene', function(req,res,next,id)
{
    var query = Scene.findById(id);
    query.exec(function(err, scene)
    {
        if(err) {return next(err);}
        if(!scene) {return next(new Error('Kan het gekozen verhaal niet vinden.'));}
        req.scene = scene;
        return next();
    });
});

/*Test Method*/
router.get('/download/test', function(req,res,next)
{
    var file = [];
    file.push(__dirname + '/downloads/sounds/cash_register.ogg');
    file.push(__dirname + '/downloads/sounds/bushorn.mp3');
    file.forEach(function(entry)
    {
        res.json(entry);
        //res.download(entry);
    });
});


// API methods
router.post('/createStory', auth, function(req,res,next)
{
    var story = new Story();
    story.name = req.body.name();
    story.scenes = [];
    if(req.body.scenes.length !== 0)
    {
        // add scenes
    }
    // kijken of het bestaand of nieuw thema is
    story.theme = req.body.theme;
    story.saveDate(req.body.date);
});

router.post(':story/addScene', auth, function(req,res,next)
{
    var story = req.story;
    var x = req.story.scenes.length;
    var newScene = new Scene();
    //if implementeren voor not found == nieuwe maken
    var theme = Theme.find({_id: req.body.theme._id});
    newScene.theme = theme;
    //if implementeren voor not found == nieuwe maken
    var widget = Widget.find({_id: req.body.widget._id});
    newScene.widget = widget;
    newScene.sceneNr = x;
    newScene.figures = req.body.figures;
    newScene.save(function(err)
    {
        if(err){console.log(err);}
    });
    story.scenes.push(newScene);
    story.save(function(err)
    {
        if(err) {console.log(err);}
    });
});

router.get('/getStory/:story', auth, function(req,res,next)
{
    req.story.populate('scenes', function(err, story)
    {
        Story.populate(story,
            {
                path:'theme',
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
                                path:'scenes.widget',
                                model:'Widget'
                            }, function(err, y)
                            {
                                Scene.populate(y,
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

router.get(':story/download/:scene', auth, function(req,res, next)
{
    //download scene files
});

/*Getters voor elementen*/
router.get("/getAllThemes", auth, function(req,res,next)
{

});

router.get("/getAllWidgets", auth, function(req,res,next)
{

});

module.exports = router;
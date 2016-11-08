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

/*Test Method*/
router.get('/download/test', function(req,res,next)
{
    var file = __dirname + '/downloads/sounds/cash_register.ogg';
    res.download(file);
});


// API methods
router.post('/createStory', auth, function(req,res,next)
{

});

router.get('/getStory/:story', auth, function(req,res,next)
{
   return res.json(req.story);
});

router.get('/download/:story', auth, function(req,res, next)
{
    req.story.populate('scenes', function(err, scene)
    {
        Story.populate('scenes',
            {
                path:'Scenes.widget',
                model: 'Widget'
            }, function(err, dat)
            {
                dat.scenes.forEach()
            })
    });
    var file = __dirname + 'downloads/';
});

module.exports = router;
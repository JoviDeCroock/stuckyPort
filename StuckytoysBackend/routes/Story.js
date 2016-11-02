/**
 * Created by jovi on 11/1/2016.
 */
var mongoose = require('mongoose');
var express = require('express');
var jwt = require('express-jwt');
var router = express.Router();

var config = require('../config/config');
var User = mongoose.model('User');
var Member = mongoose.model('Member');
var Story = mongoose.model('Story');

// configuring auth
var auth = jwt({secret:config.secret,userProperty:config.userProperty});

// Params
router.param('user',function(req,res,next,id){
    var query = User.findById(id);
    query.exec(function(err,user){
        if(err){return next(err);}
        if(!user){return next(new Error('Kan de gekozen familie niet vinden.'));}
        req.user = user;
        return next();
    });
});

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

// API methods
router.post(':user/createStory', auth, function(req,res,next)
{

});

router.get(':user/getStory/:story', auth, function(req,res,next)
{
   return res.json(req.story);
});

module.exports(router);
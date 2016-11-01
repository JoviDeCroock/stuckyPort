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

router.param('user',function(req,res,next,id){
    var query = User.findById(id);
    query.exec(function(err,user){
        if(err){return next(err);}
        if(!user){return next(new Error('Kan de gekozen familie niet vinden.'));}
        req.user = user;
        return next();
    });
});

router.post(':user/createStory', auth, function(req,res,next)
{

});

module.exports(router);
/**
 * Created by jovi on 10/6/2016.
 */
// wist geen goeie naam welkom tot renamen :-)


// Dependencies express etc
var mongoose = require('mongoose');
var express = require('express');
var router = express.Router();
var passport = require('passport');

//models
var User = require('../models/Users.js');

// API tasks for models
User.methods['get', 'post'];

//API routes
User.route('Drawings', function(req, res, next)
    {
        User.find(function(err, drawings)
        {
           if(err){return next(err);}
           res.json(drawings);
        });
       //zoeken hoe ik via mongo de drawings krijg :)
    });


router.post('/register', function(req, res, next)
{

});

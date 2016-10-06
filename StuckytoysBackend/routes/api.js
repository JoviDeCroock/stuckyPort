/**
 * Created by jovi on 10/6/2016.
 */
// Dependencies express etc
var mongoose = require('mongoose');
var express = require('express');
var router = express.Router();
var passport = require('passport');

//models
var User = mongoose.model('User');

// API tasks for models
router.get('/',function(req,res,next){
  return res.json({message: 'De api werkt'});
});
//louter om te testen
router.get('/users',function(req,res,next){
  User.find(function(err,users){
    if(err){return next(err);}
    return res.json(users);
  });
});
router.post('/register',function(req,res,next){
   if(!req.body.login || !req.body.password || !req.body.email){
     return res.status(400).json({message:'Vul alle velden in'});
   }
   var user = new User();
   user.login = req.body.login;
   user.password = req.body.password;
   user.email = req.body.email;
   user.save(function(err){
     if(err){return next(err);}
     return res.json(user);
   });
});
module.exports = router;

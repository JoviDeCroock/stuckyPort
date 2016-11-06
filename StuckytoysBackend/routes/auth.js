/**
 * Created by jovi on 10/6/2016.
 */
// Dependencies express etc
var mongoose = require('mongoose');
var express = require('express');
var passport = require('passport');
var router = express.Router();

var tokenGenerator = require('../config/tokenGenerator');

//models
var User = mongoose.model('User');
var Figure = mongoose.model('Figure');

// Sanity test
router.get('/',function(req,res,next){
  return res.json({message: 'De api werkt'});
});

// Louter om te testen
router.get('/users',function(req,res,next){
  User.find(function(err,users){
    if(err){return next(err);}
    return res.json(users);
  });
});

// API methods
router.post('/register',function(req,res,next){
   if(!req.body.username || !req.body.password || !req.body.email){
     return res.status(400).json({message:'Vul alle velden in'});
   }
   var user = new User();
   user.username = req.body.username;
   user.password = req.body.password;
   user.email = req.body.email;
   user.members = [];
    var query = Figure.find();
    query.exec(function(err, figures)
    {
        if(err){return next(err);}
        figures.forEach(function(figure)
        {
            if(figure.default)
            {
                user.figures.push(figure);
            }
        });
        user.save(function(err){
            if(err){return next(err);}
            res.json({token: tokenGenerator(user)});
        });
    });

});

router.post('/login',function(req,res,next){
  if(!req.body.username || !req.body.password){
    return res.status(400).json({message:'Vul alle velden in'});
  }
  passport.authenticate('local',function(err,user,info){
    if(err){return next(err);}
    if(user){
      return res.json({token: tokenGenerator(user)});
    }
    else{
      return res.status(401).json(info);
    }
  })(req,res,next);
});

module.exports = router;

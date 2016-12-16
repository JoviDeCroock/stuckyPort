/**
 * Created by jovi on 10/6/2016.
 */
// Dependencies injecten
var mongoose = require('mongoose');
var express = require('express');
var passport = require('passport');
var router = express.Router();

// Declare tokengenerator
var tokenGenerator = require('../config/tokenGenerator');

//models
var User = mongoose.model('User');
var Admin = mongoose.model('Admin');
var Story = mongoose.model('Story');

// Sanity test
router.get('/',function(req,res,next)
{
  return res.json({message: 'De api werkt'});
});

// API methods
router.post('/register',function(req,res,next)
{
   if(!req.body.username || !req.body.password || !req.body.email){
     return res.status(400).json({message:'Vul alle velden in'});
   }
   if(User.findOne({email: req.body.email}))
   {
      if(data != null)
      {
          return res.status(400).json({message:'Het emailadres is al bezet.'});
      }
   };
    var user = new User();
    user.username = req.body.username;
    user.password = req.body.password;
    user.email = req.body.email;
    user.stories = [];
    Story.findOne({name: 'Recyclage'}, function(err, data)
    {
        if(err){console.log(err);}
        user.stories.push(data);
    });
    user.save(function(err) {
        if (err) {
            return next(err);
        }
        res.json({token: tokenGenerator(user)});
    });
});

// Angular admin login
router.post('/adminLogin', function(req, res, next)
{
    if(!req.body.username || !req.body.password){
        return res.status(400).json({message:'Vul alle velden in'});
    }
    passport.authenticate('admin-local',function(err, admin, info){
        if(err){return next(err);}
        if(admin){
            return res.json({token: tokenGenerator(admin)});
        }
        else{
            return res.status(401).json(info);
        }
    })(req,res,next);
});

// Normal login
router.post('/login',function(req,res,next)
{
  if(!req.body.username || !req.body.password){
    return res.status(400).json({message:'Vul alle velden in'});
  }
  passport.authenticate('user-local',function(err,user,info){
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

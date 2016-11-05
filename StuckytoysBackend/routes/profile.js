
/**
 * Created by jovi on 10/7/2016.
 */
var mongoose = require('mongoose');
var express = require('express');
var jwt = require('express-jwt');
var router = express.Router();

var config = require('../config/config');
var User = mongoose.model('User');
var Member = mongoose.model('Member');
var Figure = mongoose.model('Figure');
var Picture = mongoose.model('Picture');

// configuring auth
var auth = jwt({secret:config.secret,userProperty:config.userProperty});

// param method for the member
router.param('member',function(req,res,next,id){
  var query = Member.findById(id);
  query.exec(function(err,member){
    if(err){return next(err);}
    if(!member){return next(new Error('Kan het lid niet vinden.'));}
    req.member = member;
    return next();
  });
});

router.param('user',function(req,res,next,id){
  var query = User.findById(id);
  query.exec(function(err,user){
    if(err){return next(err);}
    if(!user){return next(new Error('Kan de gekozen familie niet vinden.'));}
    req.user = user;
    return next();
  });
});

// Add a member
router.post('/users/:user/addMember',auth,function(req, res, next)
{
  if(!req.body.firstName
     || !req.body.nickname
     || !req.body.role
     || !req.body.authority
     || !req.body.dateOfBirth
     || !req.body.figure
      ){
    return res.status(400).json({message: 'Vul alle velden in.'});
  }
  var member = new Member();
  member.firstName = req.body.firstName;
  member.nickname = req.body.nickname;
  member.role = req.body.role;
  member.authority = req.body.authority;
  member.saveDate(req.body.dateOfBirth);
  member.figure = req.body.figure;
  member.stories = [];
  member.save(function(err){
    if(err){return next(err);}
    req.user.members.push(member);
      req.user.save(function(err, user)
      {
         if(err){return next(err);}
         res.json(member);
      });
  });
});

// Get a specific member to log in to main screen
router.get('/users/:user/getMember/:member',auth,function(req,res,next){
  req.member.populate('figure',function(err, member){
    if(err){ return next(err); }
    member.figure.populate('picture',function(err,figure){
      if(err){ return next(err); }
      res.json(member);
    });
  });
});

// Get all family members (Select member screen)
router.get('/users/:user/getAllMembers',auth,function(req,res,next){
    req.user.populate('members', function(err, user){
       if(err) { return next(err); }
        user.members.forEach(function(member){
          member.populate('figure',function(err, theMember){
            if(err){ return next(err); }
            theMember.figure.populate('picture', function(err, figure){
              if(err){ return next(err); }
              res.json(user.members);
            });
          });
        });
    });
});

module.exports = router;

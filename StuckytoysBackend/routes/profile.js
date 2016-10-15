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

//configuring auth
var auth = jwt({secret:config.secret,userProperty:config.userProperty});

router.param('member',function(req,res,next,id){
  var query = Member.findById(id);
  query.exec(function(err,member){
    if(err){return next(err);}
    if(!member){return next(new Error('Kan het familielid niet vinden'));}
    req.member = member;
    return next();
  });
});
// Add a family member
router.post("/addMember",auth,function(req, res, next)
{
  if(!req.body.firstName || !req.body.nickname || !req.body.familyRole || !req.body.picture || !req.body.dateOfBirth){
    return res.status(400).json({message: 'Vul alle velden in'});
  }
  var member = new Member();
  member.firstName = req.body.firstName;
  member.nickname = req.body.nickname;
  member.familyRole = req.body.familyRole;
  member.picture = req.body.picture;
  member.saveDate(req.body.dateOfBirth);
  member.user = req.payload._id;
  member.save(function(err){
    if(err){return next(err);}
    return res.json(member);
  });
});

//Get a specific family member (Get all info) param?
router.get('/getMember/:member',auth,function(req,res,next){
  return res.json(req.member);
});
//Get all family members (Select member screen)
router.get("/getAllMembers",auth,function(req,res,next)
{
  Member.find({user: req.payload._id},function(err,members){
    if(err){return next(err);}
    return res.json(members);
  });
});

module.exports = router;

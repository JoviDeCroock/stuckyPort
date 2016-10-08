/**
 * Created by jovi on 10/7/2016.
 */
var mongoose = require('mongoose');
var express = require('express');
var router = express.Router();

var config = require('../config/config');
var User = mongoose.model('User');
var Member = mongoose.model('Member');


// Add a family member
router.post("/addMember", function(req, res, next)
{

});

//Get a specific family member (Get all info) param?

//Get all family members (Select member screen)
router.get("/getAll", function(req,res,next)
{

});

module.exports = router;
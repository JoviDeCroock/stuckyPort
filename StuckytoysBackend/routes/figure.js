var express = require('express');
var mongoose = require('mongoose');
var jwt = require('express-jwt');
var router = express.Router();

var Figure = mongoose.model('Figure');
var Picture = mongoose.model('Picture');

var config = require('../config/config');
var auth = jwt({secret:config.secret,userProperty:config.userProperty});

/*router.post('/addFigure',function(req,res,next){
   var figure = new Figure();
   figure.name = req.body.name;
   figure.type = req.body.type;
   figure.picture =
   figure.description =
});*/

module.exports = router;

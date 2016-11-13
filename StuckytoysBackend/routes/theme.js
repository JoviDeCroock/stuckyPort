/**
 * Created by jovi on 11/13/2016.
 */
var mongoose = require('mongoose');
var express = require('express');
var jwt = require('express-jwt');
var router = express.Router();
var path = require('path');
var mime = require('mime');

var config = require('../config/config');
var Theme = mongoose.model('Theme');

router.param('theme', function(req,res,next,id)
{
    var query = Theme.findById(id);
    query.exec(function(err, theme)
    {
        if(err) {return next(err);}
        if(!theme) {return next(new Error('Kan het gekozen thema niet vinden.'));}
        req.theme = theme;
        return next();
    });
});

router.post('/addTheme', auth, function(req,res,next)
{
    var theme = Theme.find({name: req.body.theme.name});
    if(!theme)
    {
        var t = new Theme();
        t.name = req.body.name;
        t.description = req.body.description;
        t.save(function(err)
        {
            if(err){console.log(err);}
            res.json(t);
        });
    }else{
        res.json(theme);
    }
});

router.post('/editTheme', auth, function(req,res,next)
{
    var query = {_id: req.body.theme._id};
    Theme.findOneAndUpdate(query, req.body.theme,{upsert:true}, function(err, doc) {
        if (err) return res.send(500, {error: err});
        return res.send("succesfully saved");
    });
});

router.get("/getAllThemes", auth, function(req,res,next)
{
    Theme.find(function(err,themes)
    {
        if(err){return next(err);}
        return res.json(themes);
    });
});

router.get("/themes/:theme", auth, function(req,res,next)
{
    res.json(req.theme);
});

router.post("/removeTheme/:theme", auth, function(req,res,next)
{
    Theme.remove({_id: req.theme._id}, function(err)
    {
        if (!err) {
            res.json('fail');
        }
        else {
            res.json('succes');
        }
    });
});

module.exports = router;
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
var Widget = mongoose.model('Widget');
var WidgetFile = mongoose.model('WidgetFile');

// configuring auth
var auth = jwt({secret:config.secret,userProperty:config.userProperty});

router.param('widget', function(req,res,next,id)
{
    var query = Widget.findById(id);
    query.exec(function(err, widget)
    {
        if(err) {return next(err);}
        if(!widget) {return next(new Error('Kan het gekozen widget niet vinden.'));}
        req.widget = widget;
        return next();
    });
});

router.get('/widgetTypes', auth, function(req,res,next)
{
    var fs = require('fs');
    var path = __dirname + '/downloads/';
    fs.readdir(path, function(err, items) {
        res.json(items);
    });
});

router.post('/widgetsOfType', auth, function(req,res,next)
{
    var fs = require('fs');
    var path = __dirname + '/downloads/' + req.body.type;
    fs.readdir(path, function(err, items) {
        res.json(items);
    });
});

router.post('/addWidget', auth, function(req,res,next)
{
    if(!req.body.widgetFiles || !req.body.id){
        return res.status(400).json({message:'Vul alle velden in'});
    }
    var w = new Widget();
    w.widgetFiles = [];
    w.id = req.body.id;
    req.body.widgetFiles.forEach(function(widgetFile)
    {
        var f = new WidgetFile();
        f.fileName = widgetFile.fileName;
        f.type = widgetFile.type;
        f.save(function(err)
        {
            if(err){console.log(err);}
        });
        w.files.push(f);
    });
    w.save(function(err)
    {
        if(err){console.log(err);}
        res.json(w);
    });
});

router.get("/getAllWidgets", auth, function(req,res,next)
{
    Widget.find(function(err, widgets)
    {
        Widget.populate(widgets,
            {
                path:'widgetFiles',
                model:'WidgetFile'
            },function(err, x)
            {
                res.json(x);
            });
    });
});

router.post("/removeWidget/:widget", auth, function(req,res,next)
{
    Widget.remove({_id: req.widget._id}, function(err)
    {
        if (!err) {
            res.json('fail');
        }
        else {
            res.json('succes');
        }
    });
});


router.get("/widgets/:widget", auth, function(req,res,next)
{
    req.widget.populate('files', function(err, file)
    {
        res.json(file);
    });
});

module.exports = router;
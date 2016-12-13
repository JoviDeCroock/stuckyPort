/**
 * Created by jovi on 13/12/2016.
 */
var mongoose = require('mongoose');
var express = require('express');
var router = express.Router();
var User = mongoose.model('User');
var Story = mongoose.model('Story');
var Scene = mongoose.model('Scene');
var Theme = mongoose.model('Theme');
var Widget = mongoose.model('Widget');
var WidgetFile = mongoose.model('WidgetFile');


router.param('story', function(req,res,next,id)
{
    var query = Story.findById(id);
    query.exec(function(err, story)
    {
        if(err) {return next(err);}
        if(!story) {return next(new Error('Kan het gekozen verhaal niet vinden.'));}
        req.story = story;
        return next();
    });
});
router.param('user',function(req,res,next,id)
{
    var query = User.findById(id);
    query.exec(function(err,user){
        if(err){return next(err);}
        if(!user){return next(new Error('Kan de gekozen familie niet vinden.'));}
        req.user = user;
        return next();
    });
});
router.param('widgetFile', function(req,res,next,id)
{
    var query = WidgetFile.findById(id);
    query.exec(function(err, wfile)
    {
        if(err) {return next(err);}
        if(!wfile) {return next(new Error('Kan de gekozen file niet vinden.'));}
        req.widgetFile = wfile;
        return next();
    });
});
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
router.param('scene', function(req,res,next,id)
{
    var query = Scene.findById(id);
    query.exec(function(err, scene)
    {
        if(err) {return next(err);}
        if(!scene) {return next(new Error('Kan de gekozen scene niet vinden.'));}
        req.scene = scene;
        return next();
    });
});
router.param('widget', function(req, res, next, id)
{
    var query = Widget.findById(id);
    query.exec(function(err, widget) {
        if (err) {
            return next(err);
        }
        if (!widget) {
            return next(new Error('Kan het gekozen widget niet vinden.'));
        }
        req.widget = widget;
        return next();
    });
});

module.exports = router;
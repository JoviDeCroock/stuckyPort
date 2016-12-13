var express = require('express');
var mongoose = require('mongoose');
var jwt = require('express-jwt');
var router = express.Router();

var User = mongoose.model('User');
var Figure = mongoose.model('Figure');
var Picture = mongoose.model('Picture');

var config = require('../config/config');
var auth = jwt({secret:config.secret,userProperty:config.userProperty});

router.get('/:user/getFigures', auth,function(req,res,next)
{
    req.user
        .populate('figures', function(err,figures)
        {
            if(err) {return next(err);}
            User.populate(figures, {
                    path: 'figures.picture',
                    model: 'Picture'
                },
                function(err, user)
                {
                    if(err){return next(err);}
                    res.json(user.figures);
                });
        });
});

module.exports = router;

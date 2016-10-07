var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;
var mongoose = require('mongoose');
var User = mongoose.model('User');

passport.use(new LocalStrategy(
  function(username,password,done){
    User.findOne({login: username}, function(err,user){
      if(err) { return done(err); }
      if(!user) {
        return done(null,false,{message: 'Aanmeldgegevens incorrect'});
      }/*
      if(!user.validPassword(password)){
        return done(null,false,{message:'Aanmeldgegevens incorrect'});
      }*/
      user.comparePassword(password, function (err, isMatch) {
        if (!isMatch || err) {
          return done(null,false,{message:'Aanmeldgegevens incorrect'});
        }
      });
      return done(null,user);
    });
  }
));

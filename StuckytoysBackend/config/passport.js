var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;
var mongoose = require('mongoose');
var User = mongoose.model('User');
var Admin = mongoose.model('Admin');

passport.use('user-local', new LocalStrategy(
  function(username,password,done){
    User.findOne({email: username}, function(err,user){
      if(err) { return done(err); }
      if(!user)
      {
            return done(null,false,{message: 'Aanmeldgegevens incorrect'});
      }
      user.comparePassword(password, function (err, isMatch) {
        if (err) { return done(err); }
        // Password did not match
        if (!isMatch) { return done(null, false, {message: 'Passwoord incorrect'}); }
        // Success
        return done(null, user);
      });
    });
  }
));

passport.use('admin-local', new LocalStrategy(
    function(username,password,done){
      Admin.findOne({email: username}, function(err,user){
        if(err) { return done(err); }
        if(!user)
        {
          return done(null,false,{message: 'Aanmeldgegevens incorrect'});
        }
        user.comparePassword(password, function (err, isMatch) {
          if (err) { return done(err); }
          // Password did not match
          if (!isMatch) { return done(null, false, {message: 'Passwoord incorrect'}); }
          // Success
          return done(null, user);
        });
      });
    }
));
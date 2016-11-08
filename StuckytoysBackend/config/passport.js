var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;
var mongoose = require('mongoose');
var User = mongoose.model('User');
var Admin = mongoose.model('Admin');

passport.use(new LocalStrategy(
  function(username,password,done){
    User.findOne({email: username}, function(err,user){
      if(err) { return done(err); }
      if(!user)
      {
        Admin.findOne({email: username},  function(err, admin)
        {
          if(err) { return done(err);}
          if(!admin)
          {
            return done(null,false,{message: 'Aanmeldgegevens incorrect'});
          }
          admin.comparePassword(password, function(err, isMatch)
          {
            if(err) {return done(err);}
            if(!isMatch) {return done(null, false, {message:'Passwoord incorrect'});}
            return done(null, admin);
          });
        });

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

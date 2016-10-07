var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');
var salt = bcrypt.genSaltSync(10);
/*
    sources:
    http://codetheory.in/using-the-node-js-bcrypt-module-to-hash-and-safely-store-passwords/
    https://www.npmjs.com/package/bcrypt-nodejs
    http://stackoverflow.com/questions/13582862/mongoose-pre-save-async-middleware-not-working-as-expected
*/
var UserSchema = new mongoose.Schema(
    {
        login: {type: String, unique:true},
        password: String, //begint als normaal string passwoord maar wordt door de pre vervangen met hashed
        email: {type: String, unique:true},
        isHashed:
        {
            type: Boolean,
            default: false
        }
        //admin? Kan handig zijn voor portaal?
    }
);
UserSchema.methods.comparePassword = function(password,cb){
  bcrypt.compare(password,this.password,function(err,isMatch){
      if(err){
        return cb(err);
      }
      cb(null,isMatch);
  });
}
UserSchema.pre('save', function(next)
{
    var user = this;
    if(user.isHashed) {return next();}

    bcrypt.hash(user.password, salt, function(err, hash)
    {
        if(err) {return next(err);}
        user.password = hash; // user zijn passwoord vervangen door het gehashte passwoord
        user.isHashed = true; // dan kan een passwoord niet dubbel gehashd worden
        return next();
    });
});

mongoose.model('User', UserSchema);

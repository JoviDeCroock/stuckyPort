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

//hebben we nog iets nodig om en passwoord te controleren? voorbeeld flapper:
/*
 UserSchema.methods.validPassword = function(password)
 {
 var hash = ccrypto.pbkdf2Sync(password, this.salt,1000,64).toString('hex');
 return this.hash === hash;
 }
 */

// persoonlijk weinig nut van JsonWebTokens gevonden die in models moeten, meesten zetten deze in de app


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

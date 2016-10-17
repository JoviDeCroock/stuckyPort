var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');
var salt = bcrypt.genSaltSync(10);

var UserSchema = new mongoose.Schema(
    {
        username: {type: String, unique:true},
        password: String,
        email: {type: String, unique:true},
        isHashed:
        {
            type: Boolean,
            default: false
        }
    }
);

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

UserSchema.methods.comparePassword = function(password,cb){
    bcrypt.compare(password,this.password,function(err,isMatch){
        if(err){
            return cb(err);
        }
        cb(null,isMatch);
    });
};

mongoose.model('User', UserSchema);

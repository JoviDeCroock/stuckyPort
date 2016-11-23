/**
 * Created by jovi on 8/11/2016.
 */
var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');
var salt = bcrypt.genSaltSync(10);

var AdminSchema = new mongoose.Schema(
    {
        email: {type: String, unique: true},
        username: {type: String, unique: true},
        password: String,
        isHashed:
        {
            type: Boolean,
            default: false
        }
    }
);

AdminSchema.pre('save', function(next)
{
    var admin = this;
    if(admin.isHashed) {return next();}
    bcrypt.hash(admin.password, salt, function(err, hash)
    {
        if(err) {return next(err);};
        admin.password = hash;
        admin.isHashed = true;
        return next();
    });
});

AdminSchema.methods.comparePassword = function(password,cb){
    bcrypt.compare(password,this.password,function(err,isMatch){
        if(err){
            return cb(err);
        }
        cb(null,isMatch);
    });
};

mongoose.model('Admin', AdminSchema);

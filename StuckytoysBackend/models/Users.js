var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');
var salt = bcrypt.genSaltSync(10);

var UserSchema = new mongoose.Schema(
    {
        username: {type: String},
        password: String,
        email: {type: String, unique:true},
        isHashed:
        {
            type: Boolean,
            default: false
        },
        members: [{type: mongoose.Schema.Types.ObjectId, ref:'Member'}],
        stories: [{type: mongoose.Schema.Types.ObjectId, ref:'Story'}]
        //Drawings: [{type: mongoose.Schema.Types.ObjectId, ref:'Drawing'}]
    }
);

UserSchema.pre('save', function(next)
{
    var user = this;
    if(user.isHashed) {return next();}

    bcrypt.hash(user.password, salt, function(err, hash)
    {
        if(err) {return next(err);}
        user.password = hash;
        user.isHashed = true;
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

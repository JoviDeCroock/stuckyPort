var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');
var salt = bcrypt.genSaltSync(10);
var Figure = mongoose.model('Figure');

var UserSchema = new mongoose.Schema(
    {
        email: {type: String, unique: true},
        username: {type: String, unique: true},
        password: String,
        isHashed:
        {
            type: Boolean,
            default: false
        },
        stories: [{type: mongoose.Schema.Types.ObjectId, ref:'Story'}],
        members: [{type: mongoose.Schema.Types.ObjectId, ref:'Member'}],
        figures: [{type: mongoose.Schema.Types.ObjectId, ref:'Figure'}]
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

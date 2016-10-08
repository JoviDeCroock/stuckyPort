/**
 * Created by jovi on 10/7/2016.
 */
var mongoose = require('mongoose');

var MemberSchema = new mongoose.Schema(
    {
        firstName: String,
        nickname: String,
        familyRole: String, //Daughter Son Dad Mom
        picture: String, //idk yet ok?
        DateOfBirth: Date,
        user: {type: mongoose.schema.Types.ObjectId, ref:'User'}
    }
);

mongoose.model("Member", MemberSchema);
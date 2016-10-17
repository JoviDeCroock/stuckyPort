/**
 * Created by jovi on 10/7/2016.
 */
var mongoose = require('mongoose');

var MemberSchema = new mongoose.Schema(
    {
        firstName: String,
        nickname: {type: String, unique: true},
        role: String, //Daughter Son Dad Mom
        picture: String, //idk yet ok?
        dateOfBirth: Date,
        user: {type: mongoose.Schema.Types.ObjectId, ref:'User'}
    }
);

MemberSchema.methods.saveDate = function(dateString){
  var dateArray = dateString.split(' ');
  var day = dateArray[0];
  var month = dateArray[1] -1;
  var year = dateArray[2];
  this.dateOfBirth = new Date(year,month,day);
}

mongoose.model("Member", MemberSchema);

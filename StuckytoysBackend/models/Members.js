/**
 * Created by jovi on 10/7/2016.
 */
var mongoose = require('mongoose');

var MemberSchema = new mongoose.Schema({
        firstName: String,
        nickname: String,
        role: String,
        authority:
        {
            type:Boolean,
            default: false
        },
        //picture: String,
        dateOfBirth: Date,
        figure: { type: mongoose.Schema.Types.ObjectId, ref: 'Figure'},
        stories: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Story' }]
    }
);

MemberSchema.methods.saveDate = function(dateString){
  var dateArray = dateString.split(' ');
  var day = dateArray[0];
  var month = dateArray[1] -1;
  var year = dateArray[2];
  this.dateOfBirth = new Date(year,month,day);
};

mongoose.model('Member', MemberSchema);

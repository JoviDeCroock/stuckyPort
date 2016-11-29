/**
 * Created by jovi on 10/6/2016.
 */
var mongoose = require('mongoose');

var StorySchema = new mongoose.Schema({
  name: String,
  date: Date,
  price: Number,
  duration: Number,
  published: Boolean,
  themes: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Theme' }],
  scenes: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Scene' }],
  picture: {type: mongoose.Schema.Types.ObjectId, ref:'Picture'}
});

StorySchema.methods.saveDate = function(dateString){
  var dateArray = dateString.split(' ');
  var day = dateArray[0];
  var month = dateArray[1] -1;
  var year = dateArray[2];
  this.date = new Date(year,month,day);
};

mongoose.model('Story', StorySchema);

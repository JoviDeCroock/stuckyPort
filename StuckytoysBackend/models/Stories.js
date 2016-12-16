/**
 * Created by jovi on 10/6/2016.
 */
var mongoose = require('mongoose');

var StorySchema = new mongoose.Schema({
  name: String,
  date: Date,
<<<<<<< HEAD
  price: Number,
  duration: Number,
=======
>>>>>>> 12583424e5cba9c7ea794957dd96fbab7ab6a0d7
  published: Boolean,
  themes: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Theme' }],
  scenes: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Scene' }],
  path: String
});

StorySchema.methods.saveDate = function(dateString){
  var dateArray = dateString.split(' ');
  var day = dateArray[0];
  var month = dateArray[1] -1;
  var year = dateArray[2];
  this.date = new Date(year,month,day);
};

mongoose.model('Story', StorySchema);

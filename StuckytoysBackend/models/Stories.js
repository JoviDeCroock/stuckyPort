/**
 * Created by jovi on 10/6/2016.
 */
var mongoose = require('mongoose');

var StorySchema = new mongoose.Schema({
  name: String,
  date: Date,
  theme: { type: mongoose.Schema.Types.ObjectId, ref: 'Theme' },
  scenes: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Scene' }]
});

mongoose.model('Story', StorySchema);

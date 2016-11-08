/*
var mongoose = require('mongoose');

var BuildingBlockSchema = new mongoose.Schema({
  name: String,
  type: String,
  text: String,
  picture: { type: mongoose.Schema.Types.ObjectId, ref: 'Picture' },
  themes: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Theme' }]
});

mongoose.model('BuildingBlock',BuildingBlockSchema);
*/
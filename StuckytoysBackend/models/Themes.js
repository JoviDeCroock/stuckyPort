var mongoose = require('mongoose');

var ThemeSchema = new mongoose.Schema({
  name: String,
  description: String
});

mongoose.model('Theme', ThemeSchema);
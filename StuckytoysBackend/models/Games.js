/**
 * Created by jovi on 11/8/2016.
 */
var mongoose = require('mongoose');

var GameSchema = new mongoose.Schema({
    id: String,
    description: String,
    theme: { type: mongoose.Schema.Types.ObjectId, ref: 'Theme' }
});

mongoose.model('Game', GameSchema);

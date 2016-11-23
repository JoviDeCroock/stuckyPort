/**
 * Created by jovi on 10/6/2016.
 */
var mongoose = require('mongoose');

//Ingescande karakters etc
var FigureSchema = new mongoose.Schema({
    name: String,
    type: String,
    default: Boolean,
    picture: {type: mongoose.Schema.Types.ObjectId, ref:'Picture'},
    description: String
});

mongoose.model('Figure', FigureSchema);

/**
 * Created by jovi on 10/6/2016.
 */
var mongoose = require('mongoose');

//Ingescande karakters etc
var DrawingSchema = new mongoose.Schema({
    picture:  String,
    //picture: [{type: mongoose.Schema.Types.ObjectId, ref:'Picture'}]
    Description: String
});

mongoose.model('Drawing', DrawingSchema);

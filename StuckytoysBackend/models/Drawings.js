/**
 * Created by jovi on 10/6/2016.
 */
var mongoose = require('mongoose');

//Ingescande karakters etc
var DrawingSchema = new mongoose.Schema({
    picture:  String, //tijdelijk op String gezet (->bitmap omzetting?)
    //picture: [{type: mongoose.Schema.Types.ObjectId, ref:'Picture'}]
    Description: String,
    user: {type: mongoose.Schema.Types.ObjectId, ref:'User'}
});

mongoose.model('Drawing', DrawingSchema);

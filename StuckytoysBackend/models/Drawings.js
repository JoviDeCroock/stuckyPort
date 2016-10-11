/**
 * Created by jovi on 10/6/2016.
 */
var mongoose = require('mongoose');

var drawingSchema = new mongoose.Schema(
{
    picture:  String, //tijdelijk op String gezet (->bitmap omzetting?)
    Description: String,
    user: {type: mongoose.Schema.Types.ObjectId, ref:'User'}
});

mongoose.model("Drawing", drawingSchema);

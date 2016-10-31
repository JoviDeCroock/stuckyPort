/**
 * Created by jovi on 10/6/2016.
 */
var mongoose = require('mongoose');

var picSchema = new mongoose.Schema(
    {
        base64:  String, //tijdelijk op String gezet (->bitmap omzetting?)
        Description: String
    });

mongoose.model("Picture", picSchema);
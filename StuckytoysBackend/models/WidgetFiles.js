/**
 * Created by jovi on 10/11/2016.
 */
var mongoose = require('mongoose');

var WidgetFileSchema = new mongoose.Schema({
    type: String,
    fileName: String
});

mongoose.model('WidgetFile', WidgetFileSchema);
/**
 * Created by jovi on 8/11/2016.
 */
var mongoose = require('mongoose');

var WidgetSchema = new mongoose.Schema({
    id: String,
    addition: String,
    type: String
});

mongoose.model('Widget', WidgetSchema);
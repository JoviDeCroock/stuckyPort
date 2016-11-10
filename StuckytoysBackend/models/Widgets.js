/**
 * Created by jovi on 8/11/2016.
 */
var mongoose = require('mongoose');

var WidgetSchema = new mongoose.Schema({
    id: String,
    widgetFiles: [{type: mongoose.Schema.Types.ObjectId, ref:'WidgetFile'}]
});

mongoose.model('Widget', WidgetSchema);
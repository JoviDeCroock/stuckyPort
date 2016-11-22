/**
 * Created by jovi on 11/2/2016.
 */
var mongoose = require('mongoose');

var SceneSchema = new mongoose.Schema({
        sceneNr: Number,
        widgets: [{type: mongoose.Schema.Types.ObjectId, ref:'Widget'}],
        text: String,
        layout: Number,
        hints: [{type: Text}]
    });

mongoose.model('Scene', SceneSchema);

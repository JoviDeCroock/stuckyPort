/**
 * Created by jovi on 11/2/2016.
 */
var mongoose = require('mongoose');

var SceneSchema = new mongoose.Schema({
        sceneNr: Number,
        widget: {type: mongoose.Schema.Types.ObjectId, ref:'Widget'},
        figures: [
          //positionX: Number,
          //positionY: Number,
          { type: mongoose.Schema.Types.ObjectId, ref: 'Figure' }
        ]/*,
        game: { type: mongoose.Schema.Types.ObjectId, ref: 'Game' }*/
    });

mongoose.model('Scene', SceneSchema);

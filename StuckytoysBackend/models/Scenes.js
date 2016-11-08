/**
 * Created by jovi on 11/2/2016.
 */
var mongoose = require('mongoose');

var SceneSchema = new mongoose.Schema({
        sceneNr: Number,
        widget: {type: mongoose.Schema.Types.ObjectId, ref:'Widget'},
        figures: [{
          //positionX: Number,
          //positionY: Number,
          figure: { type: mongoose.Schema.Types.ObjectId, ref: 'Figure' }
        }]/*
        blocks: [{
          positionX: Number,
          positionY: Number,
          buildingBlock: { type: mongoose.Schema.Types.ObjectId, ref: 'BuildingBlock' }
        }]*/

    });

mongoose.model('Scene', SceneSchema);

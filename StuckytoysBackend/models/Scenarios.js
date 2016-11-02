/**
 * Created by jovi on 11/2/2016.
 */
var mongoose = require('mongoose');

var ScenarioSchema = new mongoose.Schema(
    {
        background: String,
        Dialogue: String
    });

mongoose.model('Scenario', ScenarioSchema);

/**
 * Created by jovi on 10/6/2016.
 */
var mongoose = require('mongoose');

var StorySchema = new mongoose.Schema(
    {
        scenarios: [{type: mongoose.Schema.Types.ObjectId, ref:'Scenario'}]
    });

mongoose.model('Story', StorySchema);

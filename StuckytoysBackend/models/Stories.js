/**
 * Created by jovi on 10/6/2016.
 */
var mongoose = require('mongoose');

var storySchema = new mongoose.Schema(
    {
        /*
            background:
            characters:
            Moeten chars gelinkt zijn met hun tekstballon?
        * */

    });

mongoose.model("Story", storySchema);

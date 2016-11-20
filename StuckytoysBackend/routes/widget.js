/**
 * Created by jovi on 11/13/2016.
 */
var mongoose = require('mongoose');
var express = require('express');
var jwt = require('express-jwt');
var router = express.Router();
var path = require('path');
var mime = require('mime');
var multer = require('multer')

var config = require('../config/config');
var Widget = mongoose.model('Widget');
var WidgetFile = mongoose.model('WidgetFile');

// configuring auth

var auth = jwt({
  secret: config.secret,
  userProperty: config.userProperty
});

var storage = multer.diskStorage({
  destination: function(req, file, cb) {
    cb(null, './downloads/')
  },
  fileName: function(req, file, cb) {
    cb(null, file.widgetId + file.originalname.split('.')[file.originalname
      .split('.').length - 1]);
  }
});

var upload = multer({
  storage: storage
}).single('file');

router.param('widgetFile', function(req, res, next, id) {
  var query = WidgetFile.findById(id);
  query.exec(function(err, wfile) {
    if (err) {
      return next(err);
    }
    if (!wfile) {
      return next(new Error('Kan de gekozen file niet vinden.'));
    }
    req.widgetFile = wfile;
    return next();
  });
});
router.param('widget', function(req, res, next, id) {
  var query = Widget.findById(id);
  query.exec(function(err, widget) {
    if (err) {
      return next(err);
    }
    if (!widget) {
      return next(new Error('Kan het gekozen widget niet vinden.'));
    }
    req.widget = widget;
    return next();
  });
});



router.post(':widget/addFile', auth, function(req, res, next) {
  var query = {
    _id: req.widget._id
  };
  var w = Widget.findById(req.widget._id);
  w.widgetFiles.push(req.body.widgetFile);
  Widget.findOneAndUpdate(query, w, {
    upsert: true
  }, function(err, doc) {
    if (err) return res.send(500, {
      error: err
    });
    return res.send("succesfully saved");
  });
});

router.post(':widget/removeFile/:widgetFile', auth, function(req, res, next) {
  var query = {
    _id: req.widget._id
  };
  var id = req.widgetFile._id;
  WidgetFile.remove({
    _id: req.widgetFile._id
  }, function(err) {
    if (!err) {
      req.widget.widgetFiles.forEach(function(entry) {
        if (entry._id === id) {
          var x = req.widget.widgetFiles.indexOf(entry);
          req.widget.widgetFiles.splice(x, 1);
        }
      });
      Widget.findOneAndUpdate(query, req.widget, {
        upsert: true
      }, function(err, doc) {
        if (err) {
          return err;
        }
        res.json(req.widget);
      });
    }
  });
});

router.post('/addWidget', auth, function(req, res, next) {
if (!req.body.widgetFiles || !req.body.id) {
  return res.status(400).json({
    message: 'Vul alle velden in'
  });
}
if (!widget) {
  return next(new Error('Kan het gekozen widget niet vinden.'));
}
req.widget = widget;
return next();
});
});

router.get('/widgetTypes', auth, function(req, res, next) {
  var fs = require('fs');
  var path = __dirname + '/downloads/';
  fs.readdir(path, function(err, items) {
    res.json(items);
  });
});

router.post('/widgetsOfType', auth, function(req, res, next) {
  var fs = require('fs');
  var path = __dirname + '/downloads/' + req.body.type;
  fs.readdir(path, function(err, items) {
    res.json(items);
  });
});

router.post('/addWidget', auth, function(req, res, next) {
  if (!req.body.widgetFiles || !req.body.id) {
    return res.status(400).json({
      message: 'Vul alle velden in'
    });
  }
  var w = new Widget();
  w.widgetFiles = [];
  w.id = req.body.id;
  req.body.widgetFiles.forEach(function(widgetFile) {
    var f = new WidgetFile();
    f.fileName = widgetFile.fileName;
    f.type = widgetFile.type;
    f.save(function(err) {
      if (err) {
        console.log(err);
      }
    });
    w.files.push(f);
  });
  w.save(function(err) {
    if (err) {
      console.log(err);
    }
    res.json(w);
  });
});

router.get("/getAllWidgets", auth, function(req, res, next) {
  Widget.find(function(err, widgets) {
    Widget.populate(widgets, {
      path: 'widgetFiles',
      model: 'WidgetFile'
    }, function(err, x) {
      res.json(x);
    });
  });
});

router.post("/removeWidget/:widget", auth, function(req, res, next) {
  Widget.remove({
    _id: req.widget._id
  }, function(err) {
    if (!err) {
      res.json('fail');
    } else {
      res.json('succes');
    }
  });
});


router.get("/widgets/:widget", auth, function(req, res, next) {
  req.widget.populate('files', function(err, file) {
    res.json(file);
  });
});

router.post('/uploadResource/:widget', auth, function(req, res) {

  if (req.file) {
    req.file.widgetId = req.widget.id;
  }

  upload(req, res, function(err) {
      if (err) {
        res.json({
          error_code: 1,
          err_desc: err
        });
        return;
      }
      res.json({
        error_code: 0,
        err_desc: null
      });

      var widgetFile = new widgetFile();
      widgetFile.type = file.originalname.split('.')[file.originalname.split(
        '.').length - 1]); widgetFile.fileName = req.file.widgetId +
    widgetFile.type; req.widget.resources.push(widgetFile);

  });
})



module.exports = router;

var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var passport = require('passport');
var multer = require('multer');

//Requiring models
//require('./models/BuildingBlocks');
require('./models/Figures');
require('./models/Members');
require('./models/Pictures');
require('./models/Scenes');
require('./models/Stories');
require('./models/Themes');
require('./models/Users');
require('./models/Admins');
require('./models/Widgets');
require('./models/WidgetFiles');

//Creating database
require('./config/passport');
var config = require('./config/config');
mongoose.Promise = global.Promise;
mongoose.connect(config.database);
//initializeDatabase
require('./config/initializeDatabase');

//Requiring routes
var auth = require('./routes/auth');
var profile = require('./routes/profile');
var figure = require('./routes/figure');
var story = require('./routes/story');
var theme = require('./routes/theme');
var widget = require('./routes/widget');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');
// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));

//Middleware for allowing cross-origin api calls
app.use(function (req, res, next) {
    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', 'http://127.0.0.1:8080');
    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
    // Request headers you wish to allow
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type,Authorization');
    // Set to true if you need the website to include cookies in the requests sent
    // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);
    // Pass to next layer of middleware
    next();
});

app.use(express.static('../client'));



//Using middleware
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use(passport.initialize());

//Defining routes
app.use('/', auth);
app.use('/profile', profile);
app.use('/figure', figure);
app.use('/story', story);
app.use('/theme', theme);
app.use('/widget', widget);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});


module.exports = app;

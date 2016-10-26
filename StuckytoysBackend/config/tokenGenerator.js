var jwt = require('jsonwebtoken');
var config =require('./config');

module.exports = function(user){
  var today = new Date();
  var exp = new Date(today);
  exp.setDate(today.getDate()+60);

  return jwt.sign({
    _id: user.id,
    username: user.login,
    exp: parseInt(exp.getTime()/1000)
  },config.secret);
}

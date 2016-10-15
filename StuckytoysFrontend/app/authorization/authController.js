/**
 * Created by jovi on 10/6/2016.
 */
(function(){

  'use strict';

  angular
    .module('stuckyToys')
    .controller('authController', authController);

  authController.$inject = ['authService']; //scope hoeft niet meer geïnject worden

  function authController(authService)
  {
      var vm = this;

      vm.register = register;
      vm.logIn = logIn;

      //functions
      function register()
      {
          authService.register(vm.user).error(function(error)
          {
              vm.error = error;
          }).succes(function()
          {
              //ga naar locationurl (inloggen)
          });
      };

      function logIn()
      {
          auth.logIn(vm.user).error(function(error)
          {
              vm.error = error;
          }).succes(function()
          {
              //ga naar locationurl (home/eersteMemberToevoegen)
          })
      }
  };

})();

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
      //vm.test = 'Hehe controllerAs werkt';
      vm.register = register;
      vm.logIn = logIn;
      vm.logOut = logOut;

      //functions
      function register()
      {
          authService.register(vm.registerUser).error(function(error)
          {
              vm.error = error;
          }).succes(function()
          {
              // ga naar member feature
          });
      };

      function logIn()
      {
          auth.logIn(vm.loginUser).error(function(error)
          {
              vm.error = error;
          }).succes(function()
          {
            // ga naar member feature
          })
      };

      function logOut()
      {
          auth.logOut(vm.logOut).error(function(error)
          {
              //optioneel als we errors hebben bvb mid tekening uitloggen
          }).succes(function()
          {
              //redirect to auth
          });
      };

  };

})();

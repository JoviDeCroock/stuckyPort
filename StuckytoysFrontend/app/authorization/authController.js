/**
 * Created by jovi on 10/6/2016.
 */
(function(){

  'use strict';

  angular
    .module('stuckyToys')
    .controller('authController', authController);

  authController.$inject = ['$location','authService']; //scope hoeft niet meer geïnject worden

  function authController($location, authService)
  {
      var vm = this;
      //vm.test = 'Hehe controllerAs werkt';
      vm.register = register;
      vm.logIn = logIn;
      vm.logOut = logOut;

      //functions
      function register(isValid)
      {
        if(isValid){
          authService.register(vm.registerUser).error(function(error)
          {
              vm.error = error;
              console.log(error);
          }).success(function()
          {
              // ga naar member feature
              $location.path('/member');
          });
        }
        vm.registerSubmitted = true;
      };

      function logIn(isValid)
      {
          if(isValid){
            authService.logIn(vm.loginUser).error(function(error)
            {
                vm.error = error;
            }).success(function()
            {
              // ga naar member feature
              $location.path('/member');
            });
          }
          vm.loginSubmitted = true;
      };

      function logOut()
      {
          authService.logOut(vm.logOut).error(function(error)
          {
              //optioneel als we errors hebben bvb mid tekening uitloggen
          }).success(function()
          {
              //redirect to auth
          });
      };

  };

})();

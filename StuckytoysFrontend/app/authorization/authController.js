/**
 * Created by jovi on 10/6/2016.
 */
(function(){

  'use strict';

  angular
    .module('stuckyToys')
    .controller('authController', authController);

  authController.$inject = ['$location','authService'];

  function authController($location, authService)
  {
      var vm = this;
      vm.register = register;
      vm.logIn = logIn;

      //functions
      function register(isValid)
      {
        if(isValid){
          if(vm.registerUser.password !== vm.registerUser.confirm){
            vm.confirmError = 'Wachtwoord en Bevestig wachtwoord komen niet overeen';
            return;
          }
          authService.register(vm.registerUser).error(function(error)
          {
              vm.registerError = error;
              console.log(error);
          }).success(function()
          {
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
                vm.loginError = error;
            }).success(function()
            {
              $location.path('/member');
            });
          }
          vm.loginSubmitted = true;
      };

  };

})();

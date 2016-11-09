/**
 * Created by jovi on 10/6/2016.
 */
(function(){

  'use strict';

  angular
    .module('stuckyToys')
    .controller('authController', authController);

  authController.$inject = [ '$location', 'authService' ];

  function authController ($location, authService) {
      var vm = this;

      vm.logIn = logIn;

      //functions
      function logIn(isValid){
          if(isValid){
            authService.logIn(vm.admin).error(function(error){
                vm.loginError = error;
            }).success(function(){
              $location.path('/main');
            });
          }
          vm.loginSubmitted = true;
      };
  };
})();

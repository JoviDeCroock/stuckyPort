(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('storyController', storyController);

  storyController.$inject = ['$location', 'authService'];

  function storyController ($location, authService) {
    var vm = this;
    vm.title = "Maak verhaal";
    vm.username = authService.currentUser();
    vm.logOut = logOut;

    function logOut () {
      authService.logOut();
      $location.path('/auth');
    }
  };
})();

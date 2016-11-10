(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('storyController', storyController);

  storyController.$inject = ['$location', 'authService', 'storyService'];

  function storyController ($location, authService, storyService) {
    var vm = this;
    vm.username = authService.currentUser();
    //maak verhaal
    vm.selectedThemes = [];
    vm.themes = [{
      name: 'Recyclage',
      description: 'Het kind leren omgaan met afval'
    }, {
      name: 'Pesten',
      description: 'Moeilijk thema om over te praten'
    }]; // test
    vm.activeStory = {
      name: '',
      date: '',
      theme: {},
      scenes: []
    };
    vm.addTheme = addTheme;

    function addTheme(){
      
    };

    //alle verhalen
    vm.logOut = logOut;

    function logOut () {
      authService.logOut();
      $location.path('/auth');
    }
  };
})();
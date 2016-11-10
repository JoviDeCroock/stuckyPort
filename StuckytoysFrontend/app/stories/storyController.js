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
    vm.themeChooserClicked = false;
    vm.selectedThemes = [];
    vm.scenes = [];
    //Wordt uit service gehaald
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
    vm.selectTheme = selectTheme;
    vm.addScene = addScene;

    function addTheme () {
      vm.themeChooserClicked = true;
    };
    function selectTheme () {
      vm.selectedThemes.push(vm.selectedTheme);
      vm.themeChooserClicked = false;
      vm.selectedTheme = {};
    };
    function addScene () {
      vm.scenes.push({
        sceneNr: vm.scenes.length + 1
      });
    };

    //alle verhalen
    vm.logOut = logOut;

    function logOut () {
      authService.logOut();
      $location.path('/auth');
    }
  };
})();

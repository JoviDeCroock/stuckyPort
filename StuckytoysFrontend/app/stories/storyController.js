(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('storyController', storyController);

  storyController.$inject = ['$location', 'authService', 'storyService','themeFactory', 'widgetService'];

  function storyController ($location, authService, storyService, themeFactory, widgetService) {
    var vm = this;
    vm.username = authService.currentUser();

    vm.activeStory = {
      themes: [],
      scenes: [],
    };

    // scenes
    vm.addScene = addScene;
    vm.selectScene = selectScene;
    // themes
    vm.themes = themeFactory.themes;
    // scenes
    function addScene () {
      var temp = {
        sceneNr: vm.activeStory.scenes.length + 1,
        widgets: [],
        hints: []
      };
      vm.activeStory.scenes.push(temp);
      vm.activeScene = temp;
    };
    function selectScene (scene) {
      vm.activeScene = scene;
    };
    // themes

    vm.logOut = logOut;

    function logOut () {
      authService.logOut();
      $location.path('/auth');
    }
  };
})();

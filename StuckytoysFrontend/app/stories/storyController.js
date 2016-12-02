(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('storyController', storyController);

  storyController.$inject = ['$location', 'authService', 'storyService', 'widgetService'];

  function storyController ($location, authService, storyService, widgetService) {
    var vm = this;
    vm.username = authService.currentUser();

    vm.activeStory = {
      themes: [],
      scenes: [],
    };
    
    vm.addScene = addScene;
    vm.selectScene = selectScene;

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
    //alle verhalen
    vm.logOut = logOut;

    function logOut () {
      authService.logOut();
      $location.path('/auth');
    }
  };
})();

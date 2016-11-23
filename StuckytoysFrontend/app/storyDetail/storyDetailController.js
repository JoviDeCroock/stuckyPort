(function(){
    'use strict';

    angular
      .module('stuckyToys')
      .controller('storyDetailController', storyDetailController);

    storyDetailController.$inject = ['authService', 'storyService', '$location'];

    function storyDetailController(authService, storyService, $location) {
      var vm = this;

      // bindables
      vm.username = authService.currentUser();
      vm.story = storyService.story;
      vm.niceDate = new Date(vm.story.date).toLocaleDateString('nl-NL');
      vm.activeScene = vm.story.scenes[0];
      vm.editMode = false;
      // functions
      vm.selectScene = selectScene;
      // implementations
      function selectScene(scene) {
        vm.activeScene = scene;
      }

      vm.logOut = logOut;

      function logOut () {
        authService.logOut();
        $location.path('/auth');
      };

    }
})();

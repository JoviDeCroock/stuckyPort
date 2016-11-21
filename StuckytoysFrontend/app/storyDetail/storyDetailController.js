(function(){
    'use strict';

    angular
      .module('stuckyToys')
      .controller('storyDetailController', storyDetailController);

    storyDetailController.$inject = ['authService', 'storyService'];

    function storyDetailController(authService, storyService) {
      var vm = this;

      // bindables
      vm.username = authService.currentUser();
      vm.story = storyService.story;
      vm.activeScene = vm.story.scenes[0];
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

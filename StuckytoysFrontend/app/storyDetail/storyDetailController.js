(function(){
    'use strict';

    angular
      .module('stuckyToys')
      .controller('storyDetailController', storyDetailController);

    storyDetailController.$inject = ['authService', 'storyService','widgetService', '$location'];

    function storyDetailController(authService, storyService, widgetService, $location) {
      var vm = this;

      // bindables
      vm.username = authService.currentUser();
      vm.story = storyService.story;
      vm.niceDate = new Date(vm.story.date).toLocaleDateString('nl-NL');
      vm.activeScene = vm.story.scenes[0];

      vm.editMode = false;
      // functions
      vm.selectScene = selectScene;
      vm.selectHint = selectHint;
      vm.getTypeOfWidget = getTypeOfWidget;
      // implementations
      function selectScene(scene) {
        vm.activeScene = scene;
        vm.activeHint = null;
      };
      function selectHint(hint) {
        vm.activeHint = hint;
      };
      function getTypeOfWidget(widget) {
         return widgetService.getTypeOfWidget(widget);
      };

      vm.logOut = logOut;

      function logOut () {
        authService.logOut();
        $location.path('/auth');
      };

    }
})();

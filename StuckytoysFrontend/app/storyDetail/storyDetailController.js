(function(){
    'use strict';

    angular
      .module('stuckyToys')
      .controller('storyDetailController', storyDetailController);

    storyDetailController.$inject = ['authService', 'storyService', 'widgetService', '$location'];

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
      vm.getTypeOfWidget = getTypeOfWidget;
      vm.getImageFileName = getImageFileName;
      // implementations
      function selectScene(scene) {
        vm.activeScene = scene;
      };
      function getTypeOfWidget(widget) {
        return widgetService.getTypeOfWidget(widget);
      };
      function getImageFileName(widget) {
        return widgetService.getImageFileName(widget);
      };

      vm.logOut = logOut;

      function logOut () {
        authService.logOut();
        $location.path('/auth');
      };

    }
})();

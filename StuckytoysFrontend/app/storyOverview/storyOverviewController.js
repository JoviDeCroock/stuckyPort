(function (){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('storyOverviewController', storyOverviewController)

  storyOverviewController.$inject = ['storyService', 'authService', '$location'];

  function storyOverviewController(storyService, authService, $location) {
    var vm = this;

    vm.username = authService.currentUser();
    vm.stories = storyService.stories;
    vm.stories.forEach(function(story){
      story.date = new Date(story.date).toLocaleDateString('nl-NL');
    });

    vm.logOut = logOut;

    function logOut() {
      authService.logOut();
      $location.path('/auth');
    }
  }
})();

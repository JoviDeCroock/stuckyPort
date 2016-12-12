(function (){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('storyOverviewController', storyOverviewController)

  storyOverviewController.$inject = ['storyService', 'authService','themeFactory', '$location'];

  function storyOverviewController(storyService, authService, themeFactory, $location) {
    var vm = this;

    vm.username = authService.currentUser();
    vm.stories = storyService.stories;
    vm.themes = themeFactory.themes;
    vm.filteredStories = storyService.stories;
    vm.filter = filter;
    vm.filterDuration = filterDuration;
    vm.reset = reset;

    function filter() {
      var result = [];
      if (vm.keyword === '') { result = vm.stories; }
      else {
        vm.stories.forEach(function(story) {
          if (story.name.toLowerCase().indexOf(vm.keyword.toLowerCase()) > -1) {
            result.push(story);
          }
        });
      }
      (result.length === 0)? vm.noEntriesFound = 'Er zijn geen verhalen gevonden': vm.noEntriesFound = null;
      vm.filteredStories = result;
    };
    function filterDuration() {
      var result = [];
      if(vm.maxDuration === '') { result = vm.stories; }
      else {
        vm.stories.forEach(function(story) {
          if(story.duration <= parseFloat(vm.maxDuration)) {
            result.push(story);
          }
        });
      }
      (result.length === 0)? vm.noEntriesFound = 'Er zijn geen verhalen gevonden': vm.noEntriesFound = null;
      vm.filteredStories = result;
    };
    function reset() {
      vm.keyword = '';
      vm.maxDuration = '';
    };

    vm.logOut = logOut;

    function logOut() {
      authService.logOut();
      $location.path('/auth');
    };
  }
})();

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
    vm.filterTheme = filterTheme;
    vm.filterDuration = filterDuration;
    vm.reset = reset;
    vm.publishStory = publishStory;

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
    function filterTheme() {
      var result = [];
      if (vm.selectedTheme === 'Toon alle') { result = vm.stories; }
      else {
        vm.stories.forEach(function(story) {
          story.themes.forEach(function(theme) {
            if(theme.name === vm.selectedTheme) { result.push(story); }
          });
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
    function publishStory(story) {
      storyService.publishStory(story._id).success(function(data) {
        vm.stories = storyService.stories;
        alert('Het verhaal '+data.name+' is gepubliceerd');
      }).error(function(err) {
        console.log(err);
      });
    };

    vm.logOut = logOut;

    function logOut() {
      authService.logOut();
      $location.path('/auth');
    };
  }
})();

(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .factory('storyService', storyService);

  storyService.$inject = ['$http', 'url'];

  function storyService ($http, url) {
    var usedUrl = url.dev;
    var story = {
      stories: [],
      getStories: getStories //TODO
    };

    return story;

    /*function getStories () {
      return $http.get()
    }*/
  };
})();

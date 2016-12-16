(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .factory('storyService', storyService);

  storyService.$inject = ['$http', 'url', 'authService', 'Upload'];

  function storyService ($http, url, authService, Upload) {
    var usedUrl = url.dev;
    var token = authService.getToken();
    var story = {
      stories: [], // voor storyOverview
      activeStory: {},
      story: {}, // voor storyDetail
      setActiveStory : setActiveStory,
      getStories: getStories,
      getStory: getStory,
      createStory: createStory,
      addSceneToStory: addSceneToStory,
      removeSceneFromStory: removeSceneFromStory,
      addScene: addScene
    };

    return story;

    function setActiveStory (aStory) {
      story.activeStory = aStory;
    };
    function getStories () {
      return $http.get(usedUrl+'story/getAllStories', {
        headers: { Authorization: 'Bearer '+token }
      })
        .success(function (data) {
          data.forEach(function(story) {
            story.date = new Date(story.date).toLocaleDateString('nl-NL');
          });
          angular.copy(data, story.stories);
          //console.log(story.stories);
        });
    };
    function getStory (id) {
      return $http.get(usedUrl+'story/getStory/'+id, {
        headers: { Authorization: 'Bearer '+token }
      })
      .success(function (data) {
         story.story = data;
      });
    };
    function createStory (aStory) {
      aStory.date = new Date();
      aStory.published = false;
      return Upload.upload({
        url: usedUrl+'story/createStory',
        headers: { Authorization: 'Bearer ' + token },
        method: 'POST',
        data: aStory,
        file: aStory.picture,
      }).success(function(data) {
        story.stories.push(data);
      });
    };
    //tijdens aanmaken => createStory
    function addSceneToStory (scene) {
      story.activeStory.scenes.push(scene);
    };
    function removeSceneFromStory (scene) {
      for (aScene in story.activeStory.scenes) {
        if (aScene.sceneNr = scene.sceneNr) {
          var pos = story.activeStory.scenes.indexOf(aScene);
          story.activeStory.scenes.splice(pos, 1);
        }
      }
    };
    //tijdens bewerken
    function addScene (storyId, scene) {
      return $http.post(usedUrl+'story/'+storyId+'/addScene', scene, {
        headers: { Authorization: 'Bearer '+token }
      })
      .success(function (data) {
        story.activeStory = data;
      })
    }
  };
})();

(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('makeStory', makeStory);

  function makeStory () {
    var directive = {
      link: link,
      templateUrl: 'app/stories/directives/makeStory.html',
      restrict: 'EA'
    };
    return directive;

    function link (scope, element, attrs) {

    };
  };
})();

(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('displayScenes', displayScenes);

  function displayScenes () {
    var directive = {
      link: link,
      templateUrl: 'app/stories/directives/displayScenes.html',
      restrict: 'EA'
    };
    return directive;

    function link (scope, element, attrs) {

    };

  }
})();

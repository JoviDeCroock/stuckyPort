(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('sceneCreator', sceneCreator);

  function sceneCreator() {
    return {
      restrict: 'E',
      templateUrl: 'app/components/sceneCreator.html',
      link: link
    };
    function link(scope, element, attrs, controller, transcludeFn) {

    };
  };
})();

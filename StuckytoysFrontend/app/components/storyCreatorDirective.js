(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('storyCreator', storyCreator);

  function storyCreator() {
    return {
      restrict: 'E',
      templateUrl: 'app/components/storyCreator.html',
      link: link
    };
    function link(scope, element, attrs, controller, transcludeFn) {

    };
  };
})();

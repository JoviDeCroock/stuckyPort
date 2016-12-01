(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('storyCreator', storyCreator);

  function storyCreator() {
    return {
      restrict: 'E',
      templateUrl: 'app/components/storyCreator.html',
      controller: 'storyController',
      controllerAs: 'vm',
      scope: true,
      bindToController: true,
      link: link
    };
    function link(scope, element, attrs, controller, transcludeFn) {

    };
  };
})();

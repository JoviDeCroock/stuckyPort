(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('hintCreator', hintCreator);

  function hintCreator() {
    return {
      restrict: 'E',
      templateUrl: 'app/components/hintCreator.html',
      link: link
    };
    function link(scope, element, attrs, controller, transcludeFn) {

    };
  };
})();

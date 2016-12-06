(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('showWidget', showWidget);

  function showWidget() {
    return {
      restrict: 'E',
      templateUrl: 'app/components/showWidget.html',
      link: link
    };
    function link(scope, element, attrs, controller, transcludeFn) {

    };
  };
})();

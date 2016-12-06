(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('selectWidget', selectWidget);

  function selectWidget() {
    return {
      restrict: 'E',
      templateUrl: 'app/components/selectWidget.html',
      link: link
    };
    function link(scope, element, attrs, controller, transcludeFn) {

    };
  };
})();

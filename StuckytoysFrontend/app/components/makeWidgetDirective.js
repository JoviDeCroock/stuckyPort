(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('makeWidget', makeWidget);

  function makeWidget() {
    return {
      restrict: 'E',
      templateUrl: 'app/components/makeWidget.html',
      controller: 'widgetController',
      controllerAs: 'vm',
      scope: true,
      bindToController: true,
      link: link
    };
    function link(scope, element, attrs, controller, transcludeFn) {

    };
  }


})();

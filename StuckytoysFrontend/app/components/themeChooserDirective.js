(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('themeChooser', themeChooser);

  function themeChooser() {
      return {
        restrict: 'E',
        templateUrl: 'app/components/themeChooser.html',
        link: link
      };
      function link(scope, element, attrs, controller, transcludeFn) {

      };
  }
})();

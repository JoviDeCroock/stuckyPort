(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('sceneChooser', sceneChooser);

  function sceneChooser() {
    return {
      restrict: 'E',
      templateUrl: 'app/components/sceneChooser.html',
      link: link
    };
    function link(scope, element, attrs, controller, transcludeFn) {

    };
  };
})();

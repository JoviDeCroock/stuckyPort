(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('sceneChooser', sceneChooser);

  function sceneChooser() {
    return {
      restrict: 'E',
      templateUrl: 'app/components/sceneChooser.html',
      controller: 'sceneController',
      controllerAs: 'vm',
      scope: true,
      bindToController: true,
      link: link
    };
    function link(scope, element, attrs, controller, transcludeFn) {

    };
  };
})();

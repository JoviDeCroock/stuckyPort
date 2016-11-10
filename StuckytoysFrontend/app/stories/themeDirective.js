(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .directive('selectedThemes', selectedThemes);

    function selectedThemes () {
      var directive = {
        templateUrl: 'app/stories/selectedThemes.html',
        restrict: 'EA',
        controller: 'storyController',
        controllerAs: 'vm',
        bindToController: true
      };
      return directive;
    };

})();

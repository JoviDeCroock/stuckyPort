(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('widgetDetailController', widgetDetailController);

   widgetDetailController.$inject = ['authService', 'widgetService', '$location'];

   function widgetDetailController(authService, widgetService, $location) {
     var vm = this;

     vm.username = authService.currentUser();
     vm.widget = widgetService.widget;

     vm.logOut = logOut;

     function logOut() {
       authService.logOut();
       $location.path('/auth');
     }
   }
})();

(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('widgetController', widgetController);

   widgetController.$inject = ['authService', 'widgetService', '$location'];

   function widgetController(authService, widgetService, $location) {
     var vm = this;

     vm.username = authService.currentUser();
     vm.widgets = widgetService.widgets;
     vm.widget = widgetService.widget;
     vm.types = ['afbeelding','geluid','spel'];//widgetService.types; dit werkt
     vm.getTypeOfWidget = getTypeOfWidget;

     function getTypeOfWidget(widget) {
       return widgetService.getTypeOfWidget(widget);
     };

     vm.logOut = logOut;

     function logOut() {
       authService.logOut();
       $location.path('/auth');
     };
   }
})();

(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .config(routes);

  routes.$inject = ['$routeProvider'];

  function routes($routeProvider){
    $routeProvider.when('/auth',{
      url: '/auth',
      templateUrl: 'app/authorization/auth.html' //gemeenschappelijk template zoals in Mocks?,
      controller: 'authController',
      controllerAs : 'vm' //hierdoor moet scope nie geïnject worden
    }).otherwise({redirectTo:'/auth'}); //default pad
  }

})();

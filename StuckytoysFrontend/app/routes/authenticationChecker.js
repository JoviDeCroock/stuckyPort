(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .run(authenticationChecker);

  authenticationChecker.$inject = ['$rootScope','$location','authService'];

  function authenticationChecker($rootScope,$location,authService){
    $rootScope.$on('$routeChangeStart',function(event){
      if(!authService.isLoggedIn()){
        $location.path('/auth');
      }
    });
  };
})();

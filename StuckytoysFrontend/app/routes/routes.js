/**
 * Created by jovi on 17/10/2016.
 */
(function(){
    'use strict';

    angular
        .module('stuckyToys')
        .config(authRoutes);

    authRoutes.$inject = ['$routeProvider'];

    function authRoutes($routeProvider)
    {
        $routeProvider.when('/auth',{
            url: '/auth',
            templateUrl: 'app/authorization/authorization.html',
            controller: 'authController',
            controllerAs : 'vm' //hierdoor moet scope nie geïnject worden
<<<<<<< HEAD:StuckytoysFrontend/app/routes/routes.js
        }).when('/member',{
            url: '/member',
            templateUrl: 'app/members/memberOverview.html',
            controller: 'memberController',
            controllerAs : 'vm' 
        }).otherwise({redirectTo: '/auth'});
=======
        }).otherwise({redirectTo:'/auth'}); //default pad

>>>>>>> e2889c8c78047a6565e0b9350eddd16065cee1d3:StuckytoysFrontend/app/authorization/authRoutes.js
    };
})();

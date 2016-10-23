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
            templateUrl: 'app/authorization/authorization.html', //gemeenschappelijk template zoals in Mocks?
            controller: 'authController',
            controllerAs : 'vm' //hierdoor moet scope nie geïnject worden
        }).otherwise({redirectTo:'/auth'}); //default pad

    };
})();
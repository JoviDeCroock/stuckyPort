/**
 * Created by jovi on 17/10/2016.
 */
(function(){
    'use strict';

    angular
        .module('stuckyToys')
        .config(authRoutes);

    routes.$inject = ['$routeProvider'];

    function routes($routeProvider)
    {
        $routeProvider.when('/member',{
            url: '/member',
            templateUrl: 'memberOverview.html', //gemeenschappelijk template zoals in Mocks?
            controller: 'memberController',
            controllerAs : 'vm' //hierdoor moet scope nie geïnject worden
        }).otherwise({redirectTo:'/member'}); //default pad
    };
})();
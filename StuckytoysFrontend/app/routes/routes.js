/**
 * Created by jovi on 17/10/2016.
 */
(function(){
    'use strict';

    angular
        .module('stuckyToys')
        .config(authRoutes);

    authRoutes.$inject = ['$routeProvider', 'memberController'];

    function authRoutes($routeProvider, memberController)
    {
        $routeProvider.when('/auth',{
            url: '/auth',
            templateUrl: 'app/authorization/authorization.html',
            controller: 'authController',
            controllerAs : 'vm' //hierdoor moet scope nie geïnject worden
        }).when('/member',{
            url: '/member',
            templateUrl: 'app/members/memberOverview.html',
            controller: 'memberController',
            controllerAs : 'vm',
            resolve:
            {
                postPromise: ['members', function(members)
                {
                    return memberController.giveMembers();
                }]
            }
        }).otherwise({redirectTo: '/auth'});
    };
})();

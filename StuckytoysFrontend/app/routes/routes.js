/**
 * Created by jovi on 17/10/2016.
 */
(function(){
    'use strict';

    angular
        .module('stuckyToys')
        .config(authRoutes);

    authRoutes.$inject = ['$routeProvider'];

    function authRoutes($routeProvider){
        $routeProvider.when('/auth',{
            url: '/auth',
            templateUrl: 'app/authorization/authorization.html',
            controller: 'authController',
            controllerAs : 'vm'
        })/*.when('/member',{
            url: '/member',
            templateUrl: 'app/members/memberOverview.html',
            controller: 'memberController',
            controllerAs : 'vm',
            resolve:{
                postPromise: ['memberFactory', function(memberFactory){
                    memberFactory.getFigures();
                    memberFactory.getMembers();
                    return;
                }]
            }
        }).when('/createMember', {
            url: '/createMember',
            templateUrl: 'app/members/makeMember.html',
            controller: 'memberController',
            controllerAs : 'vm'
        })*/.when('/main', {
            url: '/main',
            templateUrl: 'app/main/mainOverview.html',
            controller: 'mainController',
            controllerAs : 'vm'
        }).when('/makeStory', {
            url: '/makeStory',
            templateUrl: 'app/stories/makeStory.html',
            controller: 'storyController',
            controllerAs: 'vm'
        }).otherwise({redirectTo: '/main'});
    };
})();

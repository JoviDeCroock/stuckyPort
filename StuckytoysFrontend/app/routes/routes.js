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
        }).when('/main', {
            url: '/main',
            templateUrl: 'app/main/mainOverview.html',
            controller: 'mainController',
            controllerAs : 'vm'
        }).when('/makeStory', {
            url: '/makeStory',
            templateUrl: 'app/stories/makeStory.html',
            controller: 'storyController',
            controllerAs: 'vm',
            resolve: {
              postPromise: ['themeFactory','widgetService', function(themeFactory, widgetService) {
                 themeFactory.getAllThemes();
                 widgetService.getAllWidgets();
                 widgetService.getTypes();
              }]
            }
        }).when('/makeWidget', {
            url: '/makeWidget',
            templateUrl: 'app/widgets/makeWidget.html',
            controller: 'widgetController',
            controllerAs: 'vm',
            resolve: {
              postPromise: ['widgetService', function(widgetService) {
                return widgetService.getTypes();
              }]
            }
        }).when('/stories', {
            url: '/stories',
            templateUrl: 'app/storyOverview/storyOverview.html',
            controller: 'storyOverviewController',
            controllerAs: 'vm',
            resolve: {
              postPromise: ['storyService','themeFactory', function(storyService, themeFactory) {
                storyService.getStories();
                themeFactory.getAllThemes();
              }]
            }
        }).when('/story/:id',{
          url: '/story/:id',
          templateUrl: 'app/storyDetail/storyDetail.html',
          controller: 'storyDetailController',
          controllerAs: 'vm',
          resolve: {
            postPromise: ['$route', 'storyService', function($route, storyService) {
              return storyService.getStory($route.current.params.id);
            }]
          }
        }).when('/widgets',{
          url: '/widgets',
          templateUrl: 'app/widgets/widgetOverview.html',
          controller: 'widgetController',
          controllerAs: 'vm',
          resolve: ['widgetService', function(widgetService) {
            return widgetService.getAllWidgets();
          }]
        }).when('/widget/:id', {
          url:'/widget/:id',
          templateUrl: 'app/widgets/widgetDetail.html',
          controller: 'widgetController',
          controllerAs: 'vm',
          resolve: {
            postPromise: ['$route', 'widgetService', function($route, widgetService) {
              return widgetService.getWidget($route.current.params.id);
            }]
          }
        })
        .otherwise({redirectTo: '/main'});
    };
})();

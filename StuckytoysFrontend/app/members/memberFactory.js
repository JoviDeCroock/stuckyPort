/**
 * Created by jovi on 10/10/2016.
 */
(function() {
    'use strict';

    angular
        .module('stuckyToys')
        .factory('memberFactory', memberFactory);

    memberFactory.$inject = ['$http', 'authService','url','$window'];

    function memberFactory($http,authService,url, $window) {
        var usedUrl = url.dev;
        var token = authService.getToken();
        //var theLoggedMember = {};
        var memberFactory =
        {
            members : [],
            figures: [],
            //loggedMember : {},
            createMember : createMember,
            getMembers : getMembers,
            getMember : getMember,
            getFigures : getFigures
        };

        function getFigures()
        {
            return $http.get(usedUrl + authService.getUserId() + '/getFigures').success(function(data){
                angular.copy(data,memberFactory.figures);
            });
        }

        function createMember(member)
        {
            return $http.post(usedUrl + 'profile/users/' + authService.getUserId() + '/addMember', member,{
              headers: {Authorization: 'Bearer ' + token}
            }).success(function (data)
            {
                memberFactory.members.push(data);
            });
        };

        function getMembers()
        {
            return $http.get(usedUrl + 'profile/users/' + authService.getUserId() + '/getAllMembers',{
              headers: {Authorization: 'Bearer ' + token}
            }).success(function(data){
                angular.copy(data, memberFactory.members);
            });

        };

        function getMember(member)
        {
            return $http.get(usedUrl + 'profile/users/' + authService.getUserId() + '/getMember/' + member,{
              headers: {Authorization: 'Bearer ' + token}
            }).success(function(data)
            {
                memberFactory.loggedMember = data;
                //theLoggedMember = data; //test
                //return data;
            });
        };

        return memberFactory;
    };
})();

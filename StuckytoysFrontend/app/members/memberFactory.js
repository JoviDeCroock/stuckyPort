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
        var memberFactory =
        {
            members : [],
            createMember : createMember,
            getMembers : getMembers,
            getMember : getMember,
            isAuthority : isAuthority,
            getStorageMember : getStorageMember
        };

        function saveMember(member)
        {
            console.log(member);
            $window.sessionStorage['StuckytoysMember'] = member;
        };

        function getStorageMember()
        {
            return  $window.sessionStorage['StuckytoysMember'];
        };

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
                angular.copy(data,memberFactory.members);
            });
        };

        function getMember(member)
        {
            return $http.get(usedUrl + 'profile/users/' + authService.getUserId() + '/getMember/' + member._id,{
              headers: {Authorization: 'Bearer ' + token}
            }).success(function(data)
            {
                saveMember(member);
                return data;
            });
        };

        function isAuthority(member)
        {
            if(member.authority)
            {
                return true;
            }else{
                return false;
            }
        };

        return memberFactory;
    };
})();

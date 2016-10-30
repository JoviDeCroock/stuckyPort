/**
 * Created by jovi on 10/10/2016.
 */
(function() {
    'use strict';

    angular
        .module('stuckyToys')
        .factory('memberFactory', memberFactory);

    memberFactory.$inject = ['$http', 'authService'];

    function memberFactory($http, authService) {
        var token = authService.getToken();
        var memberFactory =
        {
            members : [],
            createMember : createMember,
            getMembers : getMembers,
            getMember : getMember
        };

        function createMember(member)
        {
            return $http.post('http://188.166.173.147:3000/profile/users/' + authService.getUserId() + '/addMember', member,{
              headers: {Authorization: 'Bearer ' + token}
            }).success(function (data)
            {
                memberFactory.members.push(data);
                //succes --> toont leden
            });
        };

        function getMembers()
        {
            return $http.get('http://188.166.173.147:3000/profile/users/' + authService.getUserId() + '/getAllMembers',{
              headers: {Authorization: 'Bearer ' + token}
            }).success(function(data){
                angular.copy(data,memberFactory.members);
            });
        };

        function getMember(member)
        {
            return $http.get('http://188.166.173.147:3000/profile/users/' + authService.getUserId() + '/members/' + member._id,{
              headers: {Authorization: 'Bearer ' + token}
            }).success(function(data)
            {
                return data;
            });
        };

        return memberFactory;
    };
})();

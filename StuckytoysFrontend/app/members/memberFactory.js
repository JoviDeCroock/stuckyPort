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
            createMember : create,
            giveMembers : getMembers,
            getMember : getMember
        };

        function create(member)
        {
            return $http.post('188.166.173.147:3000/profile/users/' + authService.getUserId() + '/addMember', member).success(function (data)
            {
                headers: {Authorization: 'Bearer ' + token}
                //succes --> toont leden
            });
        };

        function getMembers()
        {
            return $http.get('188.166.173.147:3000/profile/users/' + authService.getUserId() + '/getAllMembers').success(function(data)
            {
                headers: {Authorization: 'Bearer ' + token}
                return data;
            });
        };

        function getMember(member)
        {
            return $http.get('188.166.173.147:3000/profile/users/' + authService.getUserId() + '/members/' + member._id).success(function (data)
            {
                headers: {Authorization: 'Bearer ' + token}
            });
        };

        return memberFactory;
    };
})();

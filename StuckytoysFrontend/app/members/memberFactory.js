/**
 * Created by jovi on 10/10/2016.
 */
(function() {
    'use strict';

    angular
        .module('stuckyToys')
        .factory('memberFactory', memberFactory);

    memberFactory.$inject = ['$http', '$window'];

    function memberFactory($http, $window) {
        var memberFactory =
        {
            createMember : create,
            giveMembers : getMembers
        };

        function create(member)
        {
            return $http.post('localhost:3000/members/addMember', member).succes(function (data)
            {
                //succes
            });
        };

        function getMembers()
        {
            var token = auth.getToken();
            var payload = JSON.parse($window.atob(token.split('.')[1]));
            return $http.post('localhost:')
        };

        return memberFactory;
    };
});
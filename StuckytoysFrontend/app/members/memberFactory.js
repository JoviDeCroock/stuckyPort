/**
 * Created by jovi on 10/10/2016.
 */
(function() {
    'use strict';

    angular
        .module('stuckyToys')
        .factory('memberFactory', memberFactory);

    memberFactory.$inject = ['$http', '$window', 'auth'];

    function memberFactory($http, $window) {
        var memberFactory =
        {
            createMember : create,
            giveMembers : getMembers,
            selectMember : chooseMember
        };

        function create(member)
        {
            return $http.post('188.166.173.147:3000/profile/users/' + /* hier moet de user ID komen */ + '/addMember', member).success(function (data)
            {
                //succes --> toont leden
            });
        };

        function getMembers()
        {
            return $http.post('188.166.173.147:3000/profile/getAllMembers').success(function(data)
            {
                return data;
            });
        };

        function chooseMember(member)
        {
            return $http.post('188.166.173.147:3000/profile/users/' + /* hier moet de user ID komen */ + '/members/' + member._id).success(function (data)
            {
                //succes --> verwijst door naar main
            });
        }
        return memberFactory;
    };
})();

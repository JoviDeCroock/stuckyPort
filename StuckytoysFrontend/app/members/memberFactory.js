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
            giveMembers : getMembers,
            selectMember : chooseMember
        };

        function create(member)
        {
            /*
                Checken op valid data?
                Geldige datum/Datepicker
                Afbeelding converteren --> bitmap/String
            */
            return $http.post('localhost:3000/profile/addMember', member).succes(function (data)
            {
                //succes --> toont leden
            });
        };

        function getMembers()
        {
            return $http.post('localhost:3000/profile/getAllMembers');
        };

        function chooseMember(member)
        {
            // log in als geselecteerde gebruiker
        }
        return memberFactory;
    };
});
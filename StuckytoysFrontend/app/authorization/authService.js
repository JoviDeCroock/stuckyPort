/**
 * Created by jovi on 7/10/2016.
 */
(function(){
    'use strict';

    angular
        .module('stuckyToys')
        .factory('authService', authService);

    authService.$inject = ['$http', '$window'];

    function authService($http, $window){

        var auth =
        {
            register: register,
            logIn : logIn,
            saveToken: saveToken,
            getToken : getToken,
            isLoggedIn : isLoggedIn,
            currentUser : currentUser,
            logOut : logOut,
            getUserId : getUserId
        };

        function saveToken(token)
        {
            $window.localStorage['StuckytoysToken'] = token;
        };

        function getToken()
        {
            return $window.localStorage['StuckytoysToken'];
        };

        function isLoggedIn(){
            var token  = auth.getToken();
            if(token){
                var payload = JSON.parse($window.atob(token.split('.')[1]));
                return payload.exp > Date.now()/1000;
            }
            else{
                return false;
            }
        };

        function currentUser()
        {
            if(auth.isLoggedIn())
            {
                var token = auth.getToken();
                var payload = JSON.parse($window.atob(token.split('.')[1]));
                return payload.username;
            }
        };

        function getUserId()
        {
            if(auth.isLoggedIn())
            {
                var token = auth.getToken();
                var payload = JSON.parse($window.atob(token.split('.')[1]));
                return payload._id;
            }
        };

        function logIn(user)
        {
            return $http.post('http://188.166.173.147:3000/login', user).success(function(data)
            {
                auth.saveToken(data.token);
            });
        };

        function logOut()
        {
            $window.localStorage.removeItem('StuckytoysToken');
        };

        function register(user)
        {
            return $http.post('http://188.166.173.147:3000/register', user).success(function (data)
            {
                auth.saveToken(data.token);
            });
        };
        return auth;
    };
})();

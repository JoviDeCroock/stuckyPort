/**
 * Created by jovi on 7/10/2016.
 */
(function(){
    'use strict';

    angular
        .module('stuckyToys')
        .factory('authService', authService);

    authService.$inject = ['$http', '$window','url'];

    function authService($http, $window, url){
        var usedUrl = url.dev;
        var auth =
        {
            logIn : logIn,
            saveToken: saveToken,
            getToken : getToken,
            isLoggedIn : isLoggedIn,
            currentUser : currentUser,
            getUserId : getUserId,
            logOut : logOut
        };
        return auth;
        //functions
        function logIn(user){
            return $http.post(usedUrl + 'login', user).success(function(data)
            {
                auth.saveToken(data.token);
            });
        };
        function saveToken(token){
            $window.localStorage['StuckytoysToken'] = token;
        };
        function getToken(){
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
        function currentUser(){
            if(auth.isLoggedIn())
            {
                var token = auth.getToken();
                var payload = JSON.parse($window.atob(token.split('.')[1]));
                console.log(payload);
                return payload.username;
            }
        };
        function getUserId(){
            if(auth.isLoggedIn())
            {
                var token = auth.getToken();
                var payload = JSON.parse($window.atob(token.split('.')[1]));
                return payload._id;
            }
        };
        function logOut(){
            $window.localStorage.removeItem('StuckytoysToken');
        };
    };
})();

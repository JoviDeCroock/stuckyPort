/**
 * Created by jovi on 11/1/2016.
 */
(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('mainController', mainController);

    mainController.$inject = ['$location','authService'];

    function mainController($location, authService){
        var vm = this;
        vm.username = authService.currentUser();
        vm.navigateTo = navigateTo;
        vm.logOut = logOut;

        //functions
        function navigateTo (state) {
          $location.path('/'+state);
        };
        function logOut () {
          authService.logOut();
          $location.path('/auth');
        };
    };
})();

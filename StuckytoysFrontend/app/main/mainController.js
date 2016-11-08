/**
 * Created by jovi on 11/1/2016.
 */
(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('mainController', mainController);

    mainController.$inject = ['authService'];

    function mainController(authService){
        var vm = this;
        vm.username = authService.currentUser();
    };
})();

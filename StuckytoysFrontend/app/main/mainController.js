/**
 * Created by jovi on 11/1/2016.
 */
(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('mainController', mainController);

    mainController.$inject = ['$routeParams','memberFactory'];

    function mainController($routeParams,memberFactory)
    {
        var vm = this;
        vm.member = memberFactory.loggedMember;
        //console.log(vm.member);
    };
})();

/**
 * Created by jovi on 11/1/2016.
 */
(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('mainController', mainController);

    mainController.$inject = ['memberFactory'];

    function mainController(memberFactory)
    {
        var vm = this;
        vm.member = memberFactory.getLoggedMember();
        console.log(vm.member);
    };
})();
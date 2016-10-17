(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('memberController', memberController);

    memberController.$inject = ['memberFactory']; //scope hoeft niet meer geïnject worden

    function memberController(memberFactory) {
        var vm = this;
        vm.createMember = createMember;
        vm.giveMembers = giveMembers;

        function createMember()
        {

        };

        function giveMembers()
        {

        };
    }
});
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
        vm.selectMember = selectMember;

        function createMember()
        {
            vm.createMember();
        };

        function giveMembers()
        {
            //members in object zodat deze op het scherm kunnen getoond worden.
            vm.members = memberFactory.giveMembers();
        };

        function selectMember()
        {

        }
    }
});
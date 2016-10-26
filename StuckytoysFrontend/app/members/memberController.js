(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('memberController', memberController);

    memberController.$inject = ['memberFactory', 'auth']; //scope hoeft niet meer geïnject worden

    function memberController(memberFactory) {
        var vm = this;
        vm.title = 'Wie ben ik?';
        vm.createMember = createMember;
        vm.giveMembers = giveMembers;
        vm.selectMember = selectMember;

        function createMember()
        {
            memberFactory.createMember(vm.addMember).error(function(error)
            {
                vm.error = error;
            }).succes(function(data)
            {
                // toont alle members om uit te kiezen (member overview)
            });
        };

        function giveMembers(member)
        {
            // members in object zodat deze op het scherm kunnen getoond worden.
            vm.members = memberFactory.giveMembers(member);
        };

        function selectMember()
        {
            memberFactory.selectMember();
            // succes --> ga naar hoofdscherm met functies
        }
    }
})();

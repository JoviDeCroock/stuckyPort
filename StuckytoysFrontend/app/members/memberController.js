(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('memberController', memberController);

    memberController.$inject = ['$location','memberFactory', 'authService']; //scope hoeft niet meer geïnject worden

    function memberController(memberFactory, $location) {
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
                $location.path('/member');
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
            // $location.path('main');
        }
    }
})();

(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('memberController', memberController);

    memberController.$inject = ['$location','memberFactory', 'authService']; //scope hoeft niet meer geïnject worden

    function memberController(memberFactory, $location)
    {
        var vm = this;
        vm.title = 'Wie ben ik?';
        vm.createMember = createMember;
        vm.giveMembers = giveMembers;
        vm.selectMember = selectMember;

        function createMember()
        {
            memberFactory.createMember(vm.member).error(function(error)
            {
                vm.error = error;
            }).succes(function(data)
            {
                $location.path('/member');
            });
        };

        function giveMembers()
        {
            // members in object zodat deze op het scherm kunnen getoond worden.
            vm.members = memberFactory.giveMembers();
        };

        function getMember(member)
        {
            memberFactory.getMember(member);
            // $location.path('main');
        };

    };
})();

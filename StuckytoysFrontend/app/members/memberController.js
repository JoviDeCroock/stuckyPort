(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('memberController', memberController);

    memberController.$inject = ['$location','memberFactory']; //scope hoeft niet meer geïnject worden

    function memberController($location,memberFactory)
    {
        var vm = this;
        vm.title = 'Wie ben ik?';
        vm.members = memberFactory.members;
        vm.createMember = createMember;
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

        function getMember(member)
        {
            memberFactory.getMember(member);
            // $location.path('main');
        };

    };
})();

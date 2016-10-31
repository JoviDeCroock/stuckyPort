(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('memberController', memberController);

    memberController.$inject = ['$location','memberFactory'];

    function memberController($location,memberFactory)
    {
        var vm = this;
        vm.title = 'Wie ben ik?';
        vm.members = memberFactory.members;
        vm.createMember = createMember;
        vm.selectMember = selectMember;
        vm.param = {};

        /* !!!!!!!!!Sanity Test!!!!!! Check console before Panic*/
        vm.members.forEach(function(entry)
        {
           console.log(entry);
        });

        //https://github.com/ninjatronic/angular-base64
        //http://jsfiddle.net/handtrix/YvQ5y/

        function createMember()
        {
            console.log(vm.param);
            console.log(vm.member);
           /* memberFactory.createMember(vm.member).error(function(error)
            {
                vm.error = error;
            }).success(function(data)
            {
                $location.path('/member');
            });*/
        };

        function selectMember(member)
        {
            memberFactory.getMember(member);
            // $location.path('main'); + selected member meegeven?
        };

    };
})();


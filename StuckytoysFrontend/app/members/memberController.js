(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('memberController', memberController);

    memberController.$inject = ['$location','authService','memberFactory','$base64'];

    function memberController($location,authService,memberFactory, $base64)
    {
        var vm = this;
        vm.title = 'Wie ben ik?';
        vm.members = memberFactory.members;
        vm.createMember = createMember;
        vm.selectMember = selectMember;
        vm.logOut = logOut;
        vm.imgData = 'data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/W29iamVjdCBGaWxlXQ==';

        /* !!!!!!!!!Sanity Test!!!!!! Check console before Panic*/
        vm.members.forEach(function(entry)
        {
           console.log(entry);
        });

        function createMember()
        {
            vm.x = $base64.encode(vm.member.picture);
            vm.z = $base64.decode(vm.x);
            console.log(vm.x);
            console.log(vm.z);
           /*memberFactory.createMember(vm.member).error(function(error)
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
        function logOut(){
          authService.logOut();
          $location.path('/auth');
        };
    };
})();

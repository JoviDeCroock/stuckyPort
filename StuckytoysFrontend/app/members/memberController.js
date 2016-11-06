(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('memberController', memberController);

    memberController.$inject = ['$location','authService','memberFactory','$base64'];

    function memberController($location,authService,memberFactory, $base64)
    {
        var vm = this;
        vm.avatars = ["bever.png", "geit.png", "wasbeer.png"];
        vm.title = 'Wie ben ik?';
        vm.members = memberFactory.members;
        vm.userPics = authService.loggedInUser();
        console.log(vm.userPics);
        vm.createMember = createMember;
        vm.selectMember = selectMember;
        vm.logOut = logOut;
        vm.chooseAvatar = chooseAvatar;
        var chosenImage = "";

        /* !!!!!!!!!Sanity Test!!!!!! Check console before Panic*/
        vm.members.forEach(function(entry)
        {
           console.log(entry);
        });

        function chooseAvatar(avatar)
        {
            console.log(avatar);
            chosenImage = avatar;
        }

        function createMember()
        {
            vm.member.picture = chosenImage;
            console.log(vm.member);
           memberFactory.createMember(vm.member).error(function(error)
            {
                vm.error = error;
            }).success(function(data)
            {
                $location.path('/member');
            });
        };

        function selectMember(member)
        {
            memberFactory.getMember(member);
            $location.path('main');
        };

        function logOut(){
          authService.logOut();
          $location.path('/auth');
        };
    };

})();

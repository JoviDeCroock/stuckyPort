(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('memberController', memberController);

    memberController.$inject = ['$location','authService','memberFactory','$base64'];

    function memberController($location,authService,memberFactory, $base64)
    {
        var vm = this;
        //vm.avatars = ["bever.png", "geit.png", "wasbeer.png"];
        vm.title = 'Wie ben ik?';
        vm.members = memberFactory.members;
        vm.figures = memberFactory.figures;
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

        vm.figures.forEach(function(entry)
        {
            console.log(entry);
        });

        function chooseAvatar(avatar)
        {
            chosenImage = avatar._id;
            console.log(avatar);
        }

        function createMember()
        {
            vm.member.figure = chosenImage;
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
            //memberFactory.getMember(member);
            $location.path('/main/'+member._id);
        };

        function logOut(){
          authService.logOut();
          $location.path('/auth');
        };
    };

})();

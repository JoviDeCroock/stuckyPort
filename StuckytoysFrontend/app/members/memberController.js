(function() {

    'use strict';

    angular
        .module('stuckyToys')
        .controller('memberController', memberController);

    memberController.$inject = ['$location','memberFactory'];

    function memberController($location,memberFactory)
    {
        var vm = this;
        vm.image = "";
        vm.title = 'Wie ben ik?';
        vm.members = memberFactory.members;
        vm.createMember = createMember;
        vm.selectMember = selectMember;
        vm.onFileSelect = fileSelect;

        /* !!!!!!!!!Sanity Test!!!!!! Check console before Panic*/
        vm.members.forEach(function(entry)
        {
           console.log(entry);
        });


        function fileSelect(image)
        {
            if (angular.isArray(image)) {
                image = image[0];
            };
            if (image.type !== 'image/png' && image.type !== 'image/jpeg') {
                alert('Only PNG and JPEG are accepted.');
                return;
            };
            vm.image = image;
        };
        //http://stackoverflow.com/questions/25019134/how-do-you-upload-an-image-file-to-mongoose-database-using-mean-js
        function createMember()
        {
            vm.member.picture = image;
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
            // $location.path('main'); + selected member meegeven?
        };

    };
})();

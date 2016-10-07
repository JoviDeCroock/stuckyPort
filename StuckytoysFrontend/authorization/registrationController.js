/**
 * Created by jovi on 10/6/2016.
 */
angular.module('stuckyToys').controller('registrationController', registrationController);

RegisterController.$inject = ['$state', '$scope', 'authService', '$window'];
registrationController = function(authService, $state, $scope)
{
    $scope.register = function()
    {
        authService.register($scope.user).error(function(error)
        {
            $scope.error = error;
        }).then(function()
        {
            $state.go('home');
        });
    };
};
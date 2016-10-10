/**
 * Created by jovi on 10/6/2016.
 */
angular.module('stuckyToys').controller('authController', authController);

authController.$inject = ['$scope', 'authService'];
authController = function(authService, $scope)
{
    $scope.register = function()
    {
        authService.register($scope.user).error(function(error)
        {
            $scope.error = error;
        }).succes(function()
        {
            //ga naar locationurl (inloggen)
        });
    };

    $scope.logIn = function()
    {
        auth.logIn($scope.user).error(function(error)
        {
            $scope.error = error;
        }).succes(function()
        {
            //ga naar locationurl (home/eersteMemberToevoegen)
        })
    }
};
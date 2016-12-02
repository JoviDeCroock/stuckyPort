(function() {
    "use strict"

    angular
      .module('stuckyToys')
      .controller('themesController', themesController);

    themesController.$inject = ['$location', 'authService', 'themesFactory'];

    function themesController($location, authService, themesFactory) {
      var vm = this;

      vm.themes = themesFactory.themes;
      vm.getThemes = getAllThemes;
      vm.addTheme = addTheme;
      vm.editTheme = editTheme;
      vm.logOut = logOut;

      vm.username = authService.currentUser();

      function getAllThemes() {
        themesFactory.getAllThemes();
      }

      function addTheme(theme) {
        themeFactory.addTheme(theme).error(function(error) {
          vm.error = error;
        }).succes(function(data) {
          vm.themes.push(data);
        });
      }

      function editTheme(theme) {
        themeFactory.editTheme(theme).error(function(error) {
        vm.error = error;
      }).succes(function(data) {
        vm.themes.push(data);
      });
    }

    function logOut(){
      authService.logOut()
      $location.path("/auth");
    }
  }
})()

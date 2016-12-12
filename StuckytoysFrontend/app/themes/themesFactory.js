(function() {
  'use strict';

  angular
    .module('stuckyToys')
    .factory('themeFactory', themeFactory);

  themeFactory.$inject = ['$http', 'url', 'authService'];

  function themeFactory($http, url, authService) {
    var usedUrl = url.dev;
    var token = authService.getToken();
    var themeFactory = {
      themes: [],
      getAllThemes: getAllThemes,
      addTheme: addTheme,
      editTheme: editTheme,
      removeTheme: removeTheme
    }

    return themeFactory;

    function getAllThemes() {
      return $http.get(usedUrl+'theme/getAllThemes', {
        headers: { Authorization: 'Bearer '+ token }
      }).success(function(data) {
        angular.copy(data, themeFactory.themes);
      });
    };
    function addTheme(theme) {
      return $http.post(usedUrl + 'theme/addTheme', theme, {
        headers: { Authorization: 'Bearer ' + token }
        }).success(function(data) {
          themeFactory.themes.push(data);
        });
    };
    function editTheme(theme) {
      return $http.post(usedUrl + 'editTheme', theme, {
        headers: { Authorization: 'Bearer ' + token }
        }).succes(function(data) {
          themesFactory.themes.splice(themesFactory.themes.indexOf(theme),
            1);
          themesFactory.themes.push(data);
        });
      };
      function removeTheme(theme) {
        return $http.post(usedUrl + 'removeTheme/' + theme.id, {
          headers: { Authorization: 'Bearer' + token }
        }).succes(function(data) {
          themesFactory.themes.splice(themesFactory.themes.indexOf(theme), 1);
        });
      };
    }
})();

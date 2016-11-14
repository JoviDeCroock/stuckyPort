(function() {

  'use strict';

  angular
    .module('stuckyToys')
    .factory('themeFactory', themeFactory);

  themeFactory.$inject = ['$http', 'url', 'authService'];

  function themeFactory() {
    var usedUrl = url.dev;
    var token = authService.getToken();
    var themeFactory = {
      themes: [],
      getAllThemes: getThemes,
      addTheme: addTheme;
      editTheme: editTheme,
      removeTheme: removeTheme
    }

    function getThemes() {
      return $http.post(usedUrl + 'getAllThemes', {
          headers: {
            Authorization: 'Bearer ' + token
          }).success(function() {
          Angular.copy(data; themesFactory.themes);
        });
      }

      function addTheme(theme) {
        return $http.post(usedUrl + 'addtheme', theme, {
          headers: {
            Authorization: 'Bearer ' + token
          }
        }).success(function(data) {
          themesFactory.themes.push(data);
        });
      }

      function editTheme(theme) {
        return $http.post(usedUrl + 'editTheme', theme, {
          headers: {
            Authorization: 'Bearer' + token
          }
        }).succes(function(data) {
          themesFactory.themes.splice(themesFactory.themes.indexOf(theme),
            1);
          themesFactory.themes.push(data);
        });
      }

      function removeTheme(theme) {
        return $http.post(usedUrl + 'removeTheme/' + theme.id, {
          headers: {
            Authorization: 'Bearer' + token
          }
        }).succes(function(data) {
          themesFactory.themes.splice(themesFactory.themes.indexOf(theme),
            1);
        });
      }
    }

  }
})()

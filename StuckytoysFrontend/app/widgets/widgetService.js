(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .factory('widgetService', widgetService);

  widgetService.$inject = ['url', 'authService', '$http'];

  function widgetService (url, authService, $http) {
    var usedUrl = url.dev;
    var token = authService.getToken();
    var widget = {
      widget: {},
      widgets: [],
      types: [],
      getWidget: getWidget,
      getAllWidgets: getAllWidgets,
      getTypes: getTypes,
      getTypeOfWidget: getTypeOfWidget,
      getImageFileName: getImageFileName
    }

    return widget;

    function getWidget(id) {
      return $http.get(usedUrl+'widget/widgets/'+id, {
        headers: { Authorization: 'Bearer ' + token }
      }).success(function(data) {
        widget.widget = data;
      });
    };
    function getAllWidgets() {
      return $http.get(usedUrl+'widget/getAllWidgets', {
        headers: { Authorization: 'Bearer ' + token }
      }).success(function(data) {
        angular.copy(data, widget.widgets);
      })
    }
    function getTypes() {
      return $http.get(usedUrl+'widget/widgetTypes', {
        headers: { Authorization: 'Bearer ' + token }
      }).success(function(data) {
        angular.copy(data, widget.types);
      });
    };
    function getTypeOfWidget(widget) {
      var types = [];
      var value = '';
      widget.widgetFiles.forEach(function(file) {
        types.push(file.type);
      });
      types.forEach(function(type) {
        if(type === 'Geluid') { value = 'Geluid'; }
        if(type === 'Spel' ) { value = 'Spel'; }
      });
      if(value !== '') { return value; }
      else { return 'Afbeelding'; }
    };
    function getImageFileName(widget) {
      var value = {};
      widget.widgetFiles.forEach(function(file) {
        if(file.type === 'Afbeelding') { value = file; }
      });
      return value.fileName;
    };
  }
})();

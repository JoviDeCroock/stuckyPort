(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .factory('widgetService', widgetService);

  widgetService.$inject = ['url', 'authService', '$http', '$base64'];

  function widgetService (url, authService, $http, $base64) {
    var usedUrl = url.dev;
    var token = authService.getToken();
    var widget = {
      widget: {},
      widgets: [],
      types: [],
      getWidget: getWidget,
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
        if(type === 'music') { value = 'music'; }
        if(type === 'game' ) { value = 'game'; }
      });
      if(value !== '') { return value; }
      else { return 'image'; }
    };
    function getImageFileName(widget) {
      var value = {};
      widget.widgetFiles.forEach(function(file) {
        if(file.type === 'image') { value = file; }
      });
      return value.fileName;
    }
  }
})();

(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .factory('widgetService', widgetService);

<<<<<<< HEAD
  widgetService.$inject = ['url', 'authService', 'Upload', '$http'];

  function widgetService (url, authService, Upload, $http) {
=======
  widgetService.$inject = ['url', 'authService', '$http'];

  function widgetService (url, authService, $http) {
>>>>>>> 12583424e5cba9c7ea794957dd96fbab7ab6a0d7
    var usedUrl = url.dev;
    var token = authService.getToken();
    var widgetService = {
      widget: {},
      widgets: [],
<<<<<<< HEAD
      types: [],
      getWidget: getWidget,
      getAllWidgets: getAllWidgets,
      getTypes: getTypes,
      getTypeOfWidget: getTypeOfWidget,
      getImageFileName: getImageFileName,
      addImage: addImage,
      addSound: addSound
=======
      getWidget: getWidget
>>>>>>> 12583424e5cba9c7ea794957dd96fbab7ab6a0d7
    }

    return widgetService;

    function getWidget(id) {
      return $http.get(usedUrl+'widget/widgets/'+id, {
        headers: { Authorization: 'Bearer ' + token }
      }).success(function(data) {
        widgetService.widget = data;
      });
    };
    function getAllWidgets() {
      return $http.get(usedUrl+'widget/getAllWidgets', {
        headers: { Authorization: 'Bearer ' + token }
      }).success(function(data) {
        angular.copy(data, widgetService.widgets);
      })
    };
    function getTypes() {
      return $http.get(usedUrl+'widget/widgetTypes', {
        headers: { Authorization: 'Bearer ' + token }
      }).success(function(data) {
        angular.copy(data, widgetService.types);
      });
    };
<<<<<<< HEAD
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
    function addImage(widget) {
      return Upload.upload({
        url: usedUrl+'widget/addWidget',
        headers: { Authorization: 'Bearer ' + token },
        method: 'POST',
        data: {
          id: widget.id,
          type: 'Afbeelding'
        },
        file: [widget.picture]
      }).success(function(data) {
        widgetService.widgets.push(data);
      });
    };
    function addSound(widget) {
      return Upload.upload({
        url: usedUrl+'widget/addWidget',
        headers: { Authorization: 'Bearer ' + token },
        method: 'POST',
        data: {
          id: widget.id,
          type: 'Geluid'
        },
        file: [widget.picture, widget.music]
      }).success(function(data) {
        widgetService.widgets.push(data);
      });;
    };
=======
>>>>>>> 12583424e5cba9c7ea794957dd96fbab7ab6a0d7
  }
})();

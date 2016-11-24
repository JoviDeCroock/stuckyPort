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
      getWidget: getWidget,
      download: download,
      musicImageToBase64: musicImageToBase64,
      getTypeOfWidget: getTypeOfWidget
    }

    return widget;

    function getWidget(id) {
      return $http.get(usedUrl+'widget/widgets/'+id, {
        headers: { Authorization: 'Bearer ' + token }
      }).success(function(data) {
        widget.widget = data;
      });
    };
    function download(id) {
      return $http.get(usedUrl+'story/download/'+id, {
        headers: { Authorization: 'Bearer ' + token }
      });
    };
    function musicImageToBase64(theWidget) {
      var id;
      theWidget.widgetFiles.forEach(function(file) {
        if(file.type === 'image') {
          id = file._id;
        }
      });
      console.log(id);
      // var image = $base64.encode(widget.download(id));
      // return image;
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
  }
})();

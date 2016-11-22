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
      getWidget: getWidget,
      download: download
    }

    return widget;

    function getWidget(id) {
      return $http.get(usedUrl+'widget/widgets/'+id, {
        headers: { Authorization: 'Bearer ' + token }
      }).success(function(data) {
        widget.widget = data;
      });
    };
    function download(file) {
      return $http.get(usedUrl+'story/download/'+file, {
        headers: { Authorization: 'Bearer '+ token }
      });
    };
  }
})();

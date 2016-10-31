/**
 * Created by jovi on 10/31/2016.
 */
(function() {
    'use strict';

    angular
        .module('stuckyToys')
        .directive('file', file);

    file.$inject = [];

    function file()
    {
        return{
            scope:
            {
                file: '='
            },
            link: function(scope, el, attrs)
            {
                el.bind('change', function(event)
                {
                    var files = event.target.files;
                    var file = files[0];
                    scope.file = file ? file.name : undefined;
                    scope.$apply();
                });
            }
        };
    };
})();

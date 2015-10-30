(function () {
    'use strict';

    angular.module('app.directives')
        .directive('menuBar', function () {
            return {
                restrict: 'E',
                templateUrl: 'views/menubar.html'
            };
        });
})();
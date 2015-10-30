(function () {
    'use strict';

    angular.module('app')
        .config(['$httpProvider',
            function ($httpProvider) {
                $httpProvider.interceptors.push('InterceptorFactory');
            }]);
})();
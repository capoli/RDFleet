(function () {
    'use strict';

    angular.module('app')
        .config(['$httpProvider',
            function ($httpProvider) {
                $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
                $httpProvider.interceptors.push('InterceptorFactory');
            }])
        .config(function ($locationProvider) {
            $locationProvider.html5Mode(true);
        })
})();
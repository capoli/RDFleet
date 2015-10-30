(function() {
    'use strict';
    angular.module('app', [
        'ui.router',
        'ngAnimate',
        'ngResource',
        'ngCookies',
        'ui.bootstrap',
        'angular-loading-bar',
        'pascalprecht.translate',
        'restangular',
        'app.controllers',
        'app.directives',
        'app.factories'
    ]);
})();
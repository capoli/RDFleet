(function () {
    'use strict';

    angular.module('app')
        .config(['RestangularProvider', 'Config',
            function (RestangularProvider, Config) {
              RestangularProvider.setBaseUrl(Config.baseUrl);
            }]);
})();
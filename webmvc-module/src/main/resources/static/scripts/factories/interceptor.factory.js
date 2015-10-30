(function () {
    'use strict';

    angular.module('app.factories')
        .factory('InterceptorFactory', ['$q', '$location', 'Config',
            function ($q, $location, Config) {
                return {
                    request: function (config) {
                        return config || $q.when(config);
                    },

                    requestError: function (rejection) {
                        return $q.reject(rejection);
                    },

                    response: function (response) {
                        return response || $q.when(response);
                    },

                    responseError: function (rejection) {
                        $location.path(Config.routeForUnauthorizedAccess);

                        return $q.reject(rejection);
                    }
                }
            }]);
})();
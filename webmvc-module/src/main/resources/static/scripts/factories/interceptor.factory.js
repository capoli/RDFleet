(function () {
    'use strict';

    angular.module('app.factories')
        .factory('InterceptorFactory', ['$q', '$location', 'Config', 'TokenFactory',
            function ($q, $location, Config, TokenFactory) {
                return {
                    request: function (config) {
                        //config.headers = config.headers || {};
                        //TokenFactory.getToken().then(function(token) {
                        //    config.headers['X-Auth-Token'] = token;
                        //});
                        return config || $q.when(config);
                    },

                    requestError: function (rejection) {
                        return $q.reject(rejection);
                    },

                    response: function (response) {
                        console.log('response succes url');
                        return response || $q.when(response);
                    },

                    responseError: function (rejection) {
                        console.log('response error url');
                        $location.path(Config.routeForUnauthorizedAccess);
                        return $q.reject(rejection);
                    }
                }
            }]);
})();
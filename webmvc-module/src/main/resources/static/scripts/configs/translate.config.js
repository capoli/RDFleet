(function () {
    'use strict';

    angular.module('app')
        .config(['$translateProvider', function ($translateProvider) {
            $translateProvider
                .useStaticFilesLoader({
                    prefix: 'languages/',
                    suffix: '.json'
                })
                .preferredLanguage('en')
                .useLocalStorage()
                .useSanitizeValueStrategy('escape');
        }]);
})();
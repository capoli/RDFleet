(function () {
    'use strict';

    angular.module('app')
        .constant('Config', {
            baseUrl: '/api',
            deleteMessageDelay: 1000 * 6,//6 seconds
            roles: {
                superUser: 0,
                admin: 1,
                user: 2
            },
            routeForUnauthorizedAccess: '/app/error'
        })
})();
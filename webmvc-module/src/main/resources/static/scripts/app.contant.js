(function () {
    'use strict';

    angular.module('app')
        .constant('Config', {
            baseUrl: '/api',
            deleteMessageDelay: 1000 * 6,//6 seconds
            roles: {
                rdemployee: 'ROLE_RdEmployee',
                rdfleet: 'ROLE_FleetEmployee'
            },
            routeForUnauthorizedAccess: '/app/error'
        })
})();
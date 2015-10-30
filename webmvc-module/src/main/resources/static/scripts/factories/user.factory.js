(function () {
    'use strict';

    angular.module('app.factories')
        .factory('UserFactory', ['$q',
            function ($q) {
                //simple restangular method
                //Restangular.service('users')
                return {
                    user: {
                        isLoggedIn: false
                    },
                    login: function () {
                        var deferred = $q.defer();
                        (function () {
                            this.user.isLoggedIn = true;
                            deferred.resolve();
                        }.bind(this))();
                        return deferred.promise;
                    },
                    logout: function () {
                        var deferred = $q.defer();
                        (function () {
                            this.user.isLoggedIn = false;
                            deferred.resolve();
                        }.bind(this))();
                        return deferred.promise;
                    }
                };
            }]);
})();
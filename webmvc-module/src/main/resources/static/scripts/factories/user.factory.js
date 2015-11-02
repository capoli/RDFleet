(function () {
    'use strict';

    angular.module('app.factories')
        .factory('UserFactory', ['$q', '$http', '$location', 'Config',
            function ($q, $http, $location, Config) {
                roles: Config.roles;
                //simple restangular method
                //Restangular.service('users')
                return {
                    user: {
                        isLoggedIn: false,
                        hasRole: ''
                    },
                    login: function (user) {
                        var callbacks = {
                            success: function (res) {
                                console.log(res);
                                this.user.isLoggedIn = res.data.authenticated;
                                this.user.hasRole = res.data.authorities[0].authority;
                                return {message: 'login success!', error: false};
                            }.bind(this),
                            error: function (err) {
                                console.log(err);
                                this.user.isLoggedIn = false;
                                //$q.reject("Error on " + Config.baseUrl + "/login");
                                return {message: 'login error!', error: true};
                            }.bind(this)
                        };
                        var headers = user ? {
                            authorization: "Basic "
                            + btoa(user.email + ":" + user.password)
                        } : {};

                        return $http.get(Config.baseUrl + '/authenticate/login', {headers: headers})
                            .then(callbacks.success, callbacks.error);
                        //var deferred = $q.defer();
                        //(function () {
                        //    this.user.isLoggedIn = true;
                        //    deferred.resolve();
                        //}.bind(this))();
                        //return deferred.promise;
                    },
                    logout: function () {
                        var callbacks = {
                            success: function (res) {
                                console.log(res);
                                this.user.isLoggedIn = false;
                                this.user.hasRole = '';
                            }.bind(this),
                            error: function (err) {
                                $q.reject('logout call');
                                this.user.isLoggedIn = false;
                                this.user.hasRole = '';
                            }.bind(this)
                        };
                        return $http.post('/logout', {})
                            .then(callbacks.success, callbacks.error);
                        //var deferred = $q.defer();
                        //(function () {
                        //    this.user.isLoggedIn = false;
                        //    deferred.resolve();
                        //}.bind(this))();
                        //return deferred.promise;
                    },
                    permissionCheck : function(roleCollection) {
                        var deferred = $q.defer();

                        (function () {
                            var ifPermissionPassed = false;

                            angular.forEach(roleCollection, function (role) {
                                switch (role) {
                                    case roles.rdemployee:
                                        if (this.user.hasRole === role) {
                                            ifPermissionPassed = true;
                                        }
                                        break;
                                    case roles.rdfleet:
                                        if (this.user.hasRole === role) {
                                            ifPermissionPassed = true;
                                        }
                                        break;
                                    default:
                                        ifPermissionPassed = false;
                                }
                            });
                            if (!ifPermissionPassed) {
                                //If user does not have required access,
                                //we will route the user to unauthorized access page
                                $location.path(Config.routeForUnauthorizedAccess);
                                //As there could be some delay when location change event happens,
                                //we will keep a watch on $locationChangeSuccess event
                                // and would resolve promise when this event occurs.
                                $rootScope.$on('$locationChangeSuccess', function (next, current) {
                                    deferred.resolve();
                                });
                            } else {
                                deferred.resolve();
                            }
                        }.bind(this))();

                        return deferred.promise;
                    }
                };
            }]);
})();
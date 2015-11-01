(function () {
    'use strict';

    angular.module('app.factories')
        .factory('UserFactory', ['$q', '$http', 'Config',
            function ($q, $http, Config) {
                //simple restangular method
                //Restangular.service('users')
                return {
                    user: {
                        isLoggedIn: false
                    },
                    login: function (user) {
                        var callbacks = {
                            success: function (res) {
                                console.log(res);
                                this.user.isLoggedIn = true;
                            }.bind(this),
                            error: function (err) {
                                this.user.isLoggedIn = false;
                                $q.reject("Error on " + Config.baseUrl + "/login");
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
                            }.bind(this),
                            error: function (err) {
                                $q.reject('logout call');
                                this.user.isLoggedIn = false;
                            }.bind(this)
                        };
                        return $http.post(Config.baseUrl + '/logout', {})
                            .then(callbacks.success, callbacks.error);
                        //var deferred = $q.defer();
                        //(function () {
                        //    this.user.isLoggedIn = false;
                        //    deferred.resolve();
                        //}.bind(this))();
                        //return deferred.promise;
                    }
                };
            }]);
})();
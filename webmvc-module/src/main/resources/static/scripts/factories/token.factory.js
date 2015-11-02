(function() {
    'use strict';

    angular.module('app.factories')
        .factory('TokenFactory', ['$q', '$http', '$cookies', function ($q, $http, $cookies) {
            return {
                setToken: function() {
                    return $http.get('api/token').success(function(token) {
                        $cookies.put('token', token.token);
                        return token.token;
                    });
                },
                getToken: function() {
                    var token = $cookies.get('token');
                    var deferred = $q.defer();
                    if(token) return deferred.resolve(token);
                    else {
                        this.setToken().then(function(token) {
                            return deferred.resolve(token);
                        });
                    }
                }
            }
        }]);
})();

//var deferred = $q.defer();
//(function () {
//    this.user.isLoggedIn = true;
//    deferred.resolve();
//}.bind(this))();
//return deferred.promise;
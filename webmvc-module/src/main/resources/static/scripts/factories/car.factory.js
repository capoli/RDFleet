(function () {
    'use strict';

    angular.module('app.factories')
        .factory('CarFactory', ['$q', '$http', 'Config', function ($q, $http, Config) {
            function getAll() {
                var callbacks = {
                    success: function (res) {
                        return res.data;
                    },
                    error: function () {
                        $q.reject('error: ' + Config.server + '/cars/all');
                    }
                };
                return $http.get(Config.server + '/cars/all')
                    .then(callbacks.success, callbacks.error);
            }

            //function getById(id) {
            //    var callbacks = {
            //        success: function (res) {
            //            return res.data;
            //        },
            //        error: function () {
            //            $q.reject('error: ' + Config.server + '/Movies/id');
            //        }
            //    };
            //    return $http.get(Config.server + '/Movies/' + id)
            //        .then(callbacks.success, callbacks.error);
            //}
            //
            //function deleteById(id) {
            //    var callbacks = {
            //        success: function (res) {
            //            return res.data;
            //        },
            //        error: function () {
            //            $q.reject('error: ' + Config.server + '/Movies/id');
            //        }
            //    };
            //    return $http.delete(Config.server + '/Movies/' + id)
            //        .then(callbacks.success, callbacks.error);
            //}
            //
            //function searchByTitle(title) {
            //    var callbacks = {
            //        success: function (res) {
            //            return res.data;
            //        },
            //        error: function () {
            //            $q.reject('error: ' + Config.server + '/Movies/search?title=' + title);
            //        }
            //    };
            //    return $http.get(Config.server + '/Movies/search?title=' + title)
            //        .then(callbacks.success, callbacks.error);
            //}
            //
            //function addById(id, post) {
            //    var callbacks = {
            //        success: function (res) {
            //            return res.data;
            //        },
            //        error: function () {
            //            $q.reject('error: ' + Config.server + '/Movies/' + id);
            //        }
            //    };
            //    return $http.post(Config.server + '/Movies/' + id, post)
            //        .then(callbacks.success, callbacks.error);
            //}

            return {
                getAll: getAll
                //getById: getById,
                //deleteById: deleteById,
                //searchByTitle: searchByTitle,
                //addById: addById
            }
        }]);
})();
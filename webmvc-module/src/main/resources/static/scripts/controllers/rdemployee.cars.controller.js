(function () {
    'use strict';

    angular.module('app.controllers')
        .controller('RdEmployeeCarsCtrl', ['$scope', '$state', 'CarFactory',
            function ($scope, $state, CarFactory) {
                $scope.itemsByPage = 10;
                function _init() {
                    CarFactory.getAll().then(function (data) {
                        $scope.cars = data;
                    });
                }

                _init();
            }])
})();
(function () {
    'use strict';

    angular.module('app.controllers')
        .controller('LoginCtrl', ['$scope', '$state', 'UserFactory',
            function ($scope, $state, UserFactory) {
                $scope.loginUser = {email: '', password: ''};

                $scope.login = function () {
                    UserFactory.login($scope.loginUser).then(function (res) {
                        console.log(res);
                        $scope.alerts.push(
                            {type: 'success', msg: 'login success!'}
                        );
                        $scope.deleteLastMessageAfterDelay();
                        $state.go('app.home');
                    });
                };
            }]);
})();
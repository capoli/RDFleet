(function () {
    'use strict';

    angular.module('app.controllers')
        .controller('LoginCtrl', ['$scope', '$state', 'UserFactory',
            function ($scope, $state, UserFactory) {
                $scope.loginUser = {email: '', password: ''};

                $scope.login = function () {
                    UserFactory.login($scope.loginUser).then(
                        function (res) {
                            if(res.error) {
                                $scope.alerts.push(
                                    {type: 'danger', msg: res.message}
                                );
                                $scope.deleteLastMessageAfterDelay();
                            }
                            else {
                                $scope.alerts.push(
                                    {type: 'success', msg: res.message}
                                );
                                $scope.deleteLastMessageAfterDelay();
                                $state.go('app.home');
                            }
                        }
                    );
                };
            }]);
})();
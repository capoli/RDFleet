(function () {
    'use strict';

    angular.module('app.controllers')
        .controller('MenuCtrl', ['$scope', '$state', 'AuthorizationFactory', 'UserFactory',
            function ($scope, $state, AuthorizationFactory, UserFactory) {
                $scope.menu = {
                    about: 'app.about',
                    home: 'app.home',
                    login: 'app.login',
                    logout: 'app.home'
                };

                $scope.isCollapsed = true;

                $scope.collapseMenu = function () {
                    if (!$scope.isCollapsed) $scope.isCollapsed = true;
                };

                $scope.user = UserFactory.user;
                $scope.permissionModel = AuthorizationFactory.permissionModel;

                $scope.logout = function () {
                    UserFactory.logout().then(function () {
                        $scope.collapseMenu();
                        $scope.alerts.push(
                            {type: 'success', msg: 'logout success!'}
                        );
                        $scope.deleteLastMessageAfterDelay();
                        $state.go('app.home');
                    });
                }
            }]);
})();
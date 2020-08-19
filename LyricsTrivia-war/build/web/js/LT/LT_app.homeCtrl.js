/*Controller within home*/
angular.module("LTApp").controller("homeCtrl", ['$scope', '$rootScope', '$http', '$httpParamSerializer', function($scope, $rootScope, $http) {
        $scope.page = 'home';
       $('.aNav').removeClass('active');
       $('#linkHome').addClass('active');
       console.log($rootScope.username);
}]);

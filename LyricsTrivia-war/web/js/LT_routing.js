angular.module("LTApp", ['ngRoute'])
    
    .controller("LTController", function($scope) {
        $scope.title = "Simple Router Example";
    })

    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/Home', {
                templateUrl: './fragments/home/questions.html',
            }).
            when('/Players', {
                templateUrl: './fragments/home/players.html',
            }).
            when('/Profile', {
                templateUrl: './fragments/home/profile.html',
            }).
            otherwise({
                redirectTo: '/Home'
            });
    }]);



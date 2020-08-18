let LTApp = angular.module("LTApp", ['ngRoute', 'ngSanitize']);
    
LTApp.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/Home', {
                templateUrl: './fragments/home/questions.html'
            }).
            when('/Players', {
                templateUrl: './fragments/home/players.html',
                controller: 'playersCtrl'
            }).
            when('/Profile', {
                templateUrl: './fragments/home/profile.html'
            }).
            otherwise({
                redirectTo: '/Home'
            });
    }]);


/*Controller for players sub-page: containing list of all online/offline players*/
LTApp.controller("playersCtrl", ['$scope', '$http', '$httpParamSerializer', function($scope, $http) {
        $scope.playersOnlineList = [];
        
        $scope.orderByMe = function(x) {
            $scope.myOrderBy = x;
        };
        
        function refreshPlayersList(){
            $http.get("Players?online_status=1")
                .then(
                    (response) => {
                        $scope.playersOnlineList = response.data;
                    },
                    (error) =>  console.error(error)
                );
        }
        
        /*refresh periodically online status and number of all players*/
        refreshPlayersList();
        $scope.orderByMe("username");//order automatically, just first time
        setInterval(refreshPlayersList, 3000);  
  
        
    }]);

LTApp.filter('onlineStatus', function($sce) {
    return function(online) {
        let circle;
        if(online)//put a green circle
            circle = "<svg width='32' height='32'><circle cx='16' cy='16' r='12' stroke='green' stroke-width='0' fill='green' /></svg> ";
        else
            circle = "<svg width='32' height='32'><circle cx='16' cy='16' r='12' stroke='red' stroke-width='0' fill='red' /></svg> ";
        
        return $sce.trustAsHtml(circle);
    };
});

LTApp.filter('computeAge', function() {
    return function(birthdate) {
        let now = new Date();
        let birth = new Date(birthdate);
        let yearsDiff = now.getFullYear() - birth.getFullYear();
        if(now.getMonth() < birth.getMonth() || now.getMonth() == birth.getMonth() && now.getDate() < birth.getDate()) //he/she is still not <yearsDiff>
            yearsDiff--;
        return yearsDiff;
    };
});


$(document).ready(function(){
  
  /*Hide some col, when resizing*/
  if(screen.width < 768 || $(window).width()<768)
      $('.col-priority-low').addClass('d-none');
    
  $(window).resize(function(){
      console.log("Resize " + $(window).width());
      if($(window).width()<768)
        $('.col-priority-low').addClass('d-none');
      else
        $('.col-priority-low').removeClass('d-none');
  });
});
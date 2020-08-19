/*Controller for players sub-page: containing list of all online/offline players*/
angular.module("LTApp").controller("playersCtrl", ['$scope', '$rootScope', '$http', '$httpParamSerializer', function($scope, $rootScope, $http) {
       $scope.page = 'players';
       $('.aNav').removeClass('active');
       $('#linkPlayers').addClass('active');
        
        $scope.playersOnlineList = [];
        
        $scope.orderByMe = function(x) {
            $scope.myOrderBy = x;
        };
        
        function refreshPlayersList(){
            $http.get("Players?online_status=1")
                .then(
                    (response) => {
                        $scope.playersOnlineList = response.data.filter( (p)=>(p.username != $rootScope.username));
                        if(screen.width < 768 || $(window).width()<768)
                            $scope.col_priority_low = ('d-none');
                        else
                            $scope.col_priority_low = ('');
                    },
                    (error) =>  console.error(error)
                );
        }
        
        /*refresh periodically online status and number of all players*/
        refreshPlayersList();
        $scope.orderByMe("username");//order automatically, just first time
        setInterval(refreshPlayersList, 3000);  
  
        
    }]);

let LTApp = angular.module("LTApp", ['ngRoute', 'ngSanitize']);

LTApp.run(function($rootScope) {
    $rootScope.username = $('#usernamePlayer').val();
});

LTApp.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/Home', {
                templateUrl: './templates/angular/home.template.html',
                controller: 'homeCtrl'
            }).
            when('/Players', {
                templateUrl: './templates/angular/players.template.html',
                controller: 'playersCtrl'
            }).
            when('/Profile', {
                templateUrl: './templates/angular/profile.template.html',
                controller: 'profileCtrl'
            }).
            otherwise({
                redirectTo: '/Home',
                controller: 'homeCtrl'
            });
    }]);

/*Controller for players sub-page: containing list of all online/offline players*/
LTApp.controller("playersCtrl", ['$scope', '$http', '$httpParamSerializer', function($scope, $http) {
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
                        $scope.playersOnlineList = response.data;
                
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

/*Controller within home*/
LTApp.controller("homeCtrl", ['$scope', '$rootScope', '$http', '$httpParamSerializer', function($scope, $rootScope, $http) {
        $scope.page = 'home';
       $('.aNav').removeClass('active');
       $('#linkHome').addClass('active');
       console.log($rootScope.username);
}]);

/*Controller within profile*/
LTApp.controller("profileCtrl", ['$scope', '$rootScope', '$http', '$httpParamSerializer', function($scope, $rootScope, $http, $httpParamSerializer) {
       $scope.page = 'profile';
       $('.aNav').removeClass('active');
       $('#linkProfile').addClass('active');
       
       //recover player info
       $http.get("Players/"+$rootScope.username)
            .then(
                (response) => {
                    $scope.player = response.data;
                    console.log($scope.player);
                },
                (error) =>  console.error(error)
            );
        
        function checkPwdStrong(pwd){
            if(pwd === undefined || pwd === null)
                return false;
                
            if(pwd.length < 8)
                return false;
            const specials = /[`!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
            const minusc = /[qwertyuioplkjhgfdsazxcvbnm]/;
            const maiusc = /[QWERTYUIOPLKJHGFDSAZXCVBNM]/;
            const digits = /[1234567890]/;
            
            return specials.test(pwd) && minusc.test(pwd) && maiusc.test(pwd) && digits.test(pwd);
        }
        
        function isNewPwdStrong(){
            let pwd = $scope.passwordUpd1;
            return checkPwdStrong(pwd);
        }
        
        function arePwdsEquals(){
            return ($scope.passwordUpd1 === $scope.passwordUpd2);
        }
        
        /*Check pwds integrity*/
        $scope.checkPwds = function(){
            document.getElementById("submitUpd").disabled = true;
            if(!isNewPwdStrong()){
                $scope.pwdUpdateErrMsg = "Password doesn't satisfy security requirements!";
                return;
            }
            if(!arePwdsEquals()){
                $scope.pwdUpdateErrMsg = "Passwords don't match!";
                return;
            }
            document.getElementById("submitUpd").disabled = false;
            $scope.pwdUpdateErrMsg = "";
        };
        
        /*perform PUT with flag to communicate that just password needs to be updated*/
        $scope.updPwd = function () {
            let newPwd = $scope.passwordUpd1;
            
            if(!arePwdsEquals() || !isNewPwdStrong()){
                return;
            }
            
            let player = $scope.player;
            player['pwd'] = newPwd;
            
            $http({
                url: "Players/"+player.username+"?updPwd=1", //tells to the API that pwd need to be stored new (hashed and stored in the db)
                method: 'PUT',
                data: $httpParamSerializer(player), // Make sure to inject the service you choose to the controller
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    () => {$scope.pwdUpdateMsg = 'Updated!';},
                    (error) =>  console.error(error)
                );
        };
        
        $scope.checkPwdDel = function(){
            let pwdDel = $scope.passwordDel;
            if(!checkPwdStrong(pwdDel))//surely this wasn't its pwd
                document.getElementById("submitDel").disabled = true;
            else
                document.getElementById("submitDel").disabled = false;
        };
        
        /*Check if the typed pwd is correct, before enabling the deletion of the account*/
        $scope.doDelete = function(){
            
            let player = $scope.player;
            player.pwd = $scope.passwordDel;//pass your pwd for security reason, before deleting the account
            
            $http({
                url: ("Players/"+$rootScope.username), //
                method: 'DELETE',
                data: $httpParamSerializer(player), // 
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    () => location.reload(), //reload.. session will be expired at this point
                    (error) =>  console.error(error)
                );
        };

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
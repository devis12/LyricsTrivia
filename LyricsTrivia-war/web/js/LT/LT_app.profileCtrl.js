/*Controller within profile*/
angular.module("LTApp")
        
        .controller("profileCtrl", ['$scope', '$rootScope', '$http', '$httpParamSerializer', 
            function($scope, $rootScope, $http, $httpParamSerializer) {
        
        $scope.page = 'profile';
        $rootScope.homeLinkC = '';
        $rootScope.playersLinkC = '';
        $rootScope.profileLinkC = 'active';
       
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
        
        $scope.updateBtnActive = false;//disable update btn
        
        /*Check pwds integrity*/
        $scope.checkPwds = function(){
            $scope.updateBtnActive = false;
            if(!isNewPwdStrong()){
                $scope.pwdUpdateErrMsg = "Password doesn't satisfy security requirements!";
                return;
            }
            if(!arePwdsEquals()){
                $scope.pwdUpdateErrMsg = "Passwords don't match!";
                return;
            }
            $scope.updateBtnActive = true;
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
        
        
        $scope.deleteBtnActive = false;
        
        $scope.checkPwdDel = function(){
            let pwdDel = $scope.passwordDel;
            if(!checkPwdStrong(pwdDel))//surely this wasn't its pwd
                $scope.deleteBtnActive = false;
            else
                $scope.deleteBtnActive = true;
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
                    () => location.replace(
                        location.origin + 
                        location.pathname.substring(0, location.pathname.lastIndexOf("/")) 
                        + "?success_msg=Account deleted successfully"), //reload.. session will be expired at this point
                    (error) =>  console.error(error)
                );
        };

}]);

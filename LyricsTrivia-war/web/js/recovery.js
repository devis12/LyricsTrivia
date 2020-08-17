/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angular.module('recovery', [])
    .controller('recoveryCtrl', ['$scope', '$http', function($scope, $http) {
             
        
        //show "ok check", when email valid and not taken by other users    
        $scope.emailOK = false;
        $scope.emailWrong = '';
        
        $scope.checkEmail = function() {
            
            if(!$scope.isValidEmail($scope.emailRecovery)){
                $scope.emailOK = false;
                return;
            }
            
            $http.get("Players?email="+$scope.emailRecovery)
                .then(
                    (response) => { 
                        console.log(response);
                        if(response.data){
                            $scope.emailOK = true;
                            $scope.emailWrong = '';
                        }else{
                            $scope.emailOK = false;
                            $scope.emailWrong = 'No account found!';
                        }
                        
                        $scope.setDisabledSubmitBtn();
                    },
                    (error) =>  console.error(error)
                );
        };
        
        $scope.isValidEmail = function(email){
           return (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email));
        };
        
        
        //disable btn for submit when all the required fields are not filled
        $scope.setDisabledSubmitBtn = function(){
            
            if(!$scope.emailOK)
                document.getElementById("submitRecovery").disabled = true;
                
            else
                document.getElementById("submitRecovery").disabled = false;
        };
       
        
        angular.element(document).ready(function () {
            
        });
        
}]);

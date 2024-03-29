angular.module('registration_recovery', ['vcRecaptcha'])
    .controller('regRecCtrl', ['$scope', '$http', '$httpParamSerializer', function($scope, $http) {
             
        /*------------------------- REGISTRATION ---------------------*/
                
        //show "ok check", when username longer than 5 chars and not taken by other users    
        $scope.usernameFa = "fas fa-pencil-alt";
        $scope.usernameOK = false;
        $scope.usernameWrong = '';
        
        $scope.checkUsername = function() {
            if($scope.username === undefined || $scope.username === null)
                return false;
            
            if($scope.username.length < 5){
                $scope.usernameOK = false;
                $scope.usernameFa = 'far fa-times-circle text-danger';
                $scope.usernameWrong = 'username too short!';
                $scope.checkRegFormOK();
                return;
            }
            
            $http.get("Players/"+$scope.username)
                .then(
                    (response) => { 
                        if(response.data){
                            $scope.usernameOK = false;
                            $scope.usernameWrong = 'username already taken!';
                            $scope.usernameFa = 'far fa-times-circle text-danger';
                        }else{
                            $scope.usernameOK = true;
                            $scope.usernameWrong = '';
                            $scope.usernameFa = 'fas fa-check text-success';
                        }
                        
                        $scope.checkRegFormOK();
                    },
                    (error) =>  console.error(error)
                );
        };
        
        //show "ok check", when email valid and not taken by other users    
        $scope.emailFa = 'fas fa-pencil-alt';
        $scope.emailOK = false;
        $scope.emailWrong = '';
        
        $scope.checkEmail = function() {
            
            if(!$scope.isValidEmail($scope.email)){
                $scope.emailOK = false;
                $scope.emailFa = 'far fa-times-circle text-danger';
                $scope.emailWrong = 'invalid email';
                $scope.checkRegFormOK();
                return;
            }
            
            $http.get("Players?email="+$scope.email)
                .then(
                    (response) => { 
                        if(response.data){
                            $scope.emailOK = false;
                            $scope.emailWrong = 'email address already used';
                            $scope.emailFa = 'far fa-times-circle text-danger';
                        }else{
                            $scope.emailOK = true;
                            $scope.emailWrong = '';
                            $scope.emailFa = 'fas fa-check text-success';
                        }
                        
                        $scope.checkRegFormOK();
                    },
                    (error) =>  console.error(error)
                );
        };
        
        $scope.isValidEmail = function(email){
           return (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email));
        };
        
        
        //show "ok check", when pwd equals and ok for security reasons  
        $scope.Password1Fa = 'fas fa-pencil-alt';
        $scope.Password2Fa = 'fas fa-pencil-alt';
        $scope.pwdsOK = false;
        
        $scope.checkPwds = function() {
            
            function isPwdStrong(pwd){
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
            
            if(!isPwdStrong($scope.password1)){
                $scope.pwdsOK = false;
                $scope.Password1Fa = 'far fa-times-circle text-danger';
                $scope.pwdsWrong = 'Doesn\'t satisfy security requirements!';
            }else if($scope.password1 !== $scope.password2){
                $scope.pwdsOK = false;
                $scope.Password2Fa = 'far fa-times-circle text-danger';
                $scope.pwdsWrong = 'Passwords do not match!';
            }else{
                $scope.pwdsOK = true;
                $scope.pwdsWrong = '';
                $scope.Password1Fa = 'fas fa-check text-success';
                $scope.Password2Fa = 'fas fa-check text-success';
            }
            
            $scope.checkRegFormOK();
            
        };
        
                
        /*captcha metadata and callback*/
        $scope.captcha = {
            key: '6Lfal78ZAAAAANUK80a_FSdn1jewQlwhWc9KCvEE',
            verified: false
        };
        
        $scope.setCaptchaResponse = function (response) {
            //console.log(response);
            $scope.captcha.verified = true;
            $scope.checkRegFormOK();
        };
        
        //disable btn for submit when all the required fields are not filled
        $scope.regFormOK = false;
        $scope.checkRegFormOK = function(){

            if(!$scope.pwdsOK || !$scope.emailOK || !$scope.usernameOK || !$scope.captcha.verified)
                $scope.regFormOK = false;
                
            else
                $scope.regFormOK = true;
            
        };
        
        /*------------------------- PWD RECOVERY ---------------------*/
        
        //show "ok check", when email valid and not taken by other users  
        $scope.emailRecOK = false;
        $scope.emailRecWrong = '';
        
        $scope.checkEmailRec = function() {
            
            if(!$scope.isValidEmail($scope.emailRecovery)){
                $scope.emailRecOK = false;
                return;
            }
            
            $http.get("Players?email="+$scope.emailRecovery)
                .then(
                    (response) => { 
                        //console.log(response);
                        if(response.data){
                            $scope.emailRecOK = true;
                            $scope.emailRecWrong = '';
                        }else{
                            $scope.emailRecOK = false;
                            $scope.emailRecWrong = 'No account found!';
                        }
                    },
                    (error) =>  console.error(error)
                );
        };
        
        angular.element(document).ready(function () {
            
        });
        
}]);

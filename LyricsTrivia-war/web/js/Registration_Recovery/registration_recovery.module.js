angular.module('registration_recovery', [])
    .controller('regRecCtrl', ['$scope', '$http', '$httpParamSerializer', function($scope, $http) {
             
        /*------------------------- REGISTRATION ---------------------*/
        
        //show "ok check", when username longer than 5 chars and not taken by other users    
        document.getElementById('iiUsername').innerHTML = "<i class=\"fas fa-pencil-alt\"></i>";
        $scope.usernameOK = false;
        $scope.usernameWrong = '';
        
        $scope.checkUsername = function() {
            console.log("username changing");
            if($scope.username === undefined || $scope.username === null)
                return false;
            
            if($scope.username.length < 5){
                $scope.usernameOK = false;
                document.getElementById('iiUsername').innerHTML = "<i class=\"far fa-times-circle text-danger\"></i>";
                $scope.usernameWrong = 'username too short!';
                $scope.setDisabledSubmitBtn();
                return;
            }
            
            $http.get("Players/"+$scope.username)
                .then(
                    (response) => { 
                        if(response.data){
                            $scope.usernameOK = false;
                            $scope.usernameWrong = 'username already taken!';
                            document.getElementById('iiUsername').innerHTML = "<i class=\"far fa-times-circle text-danger\"></i>";
                        }else{
                            $scope.usernameOK = true;
                            $scope.usernameWrong = '';
                            document.getElementById('iiUsername').innerHTML = "<i class=\"fas fa-check text-success\"></i>";
                        }
                        
                        $scope.setDisabledSubmitBtn();
                    },
                    (error) =>  console.error(error)
                );
        };
        
        //show "ok check", when email valid and not taken by other users    
        document.getElementById('iiEmail').innerHTML = "<i class=\"fas fa-pencil-alt\"></i>";
        $scope.emailOK = false;
        $scope.emailWrong = '';
        
        $scope.checkEmail = function() {
            
            if(!$scope.isValidEmail($scope.email)){
                $scope.emailOK = false;
                document.getElementById('iiEmail').innerHTML = "<i class=\"far fa-times-circle text-danger\"></i>";
                $scope.emailWrong = 'invalid email';
                $scope.setDisabledSubmitBtn();
                return;
            }
            
            $http.get("Players?email="+$scope.email)
                .then(
                    (response) => { 
                        if(response.data){
                            $scope.emailOK = false;
                            $scope.emailWrong = 'email address already used';
                            document.getElementById('iiEmail').innerHTML = "<i class=\"far fa-times-circle text-danger\"></i>";
                        }else{
                            $scope.emailOK = true;
                            $scope.emailWrong = '';
                            document.getElementById('iiEmail').innerHTML = "<i class=\"fas fa-check text-success\"></i>";
                        }
                        
                        $scope.setDisabledSubmitBtn();
                    },
                    (error) =>  console.error(error)
                );
        };
        
        $scope.isValidEmail = function(email){
           return (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email));
        };
        
        
        //show "ok check", when pwd equals and ok for security reasons    
        document.getElementById('iiPassword1').innerHTML = "<i class=\"fas fa-pencil-alt\"></i>";
        document.getElementById('iiPassword2').innerHTML = "<i class=\"fas fa-pencil-alt\"></i>";
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
                document.getElementById('iiPassword1').innerHTML = "<i class=\"far fa-times-circle text-danger\"></i>";
                $scope.pwdsWrong = 'Doesn\'t satisfy security requirements!';
            }else if($scope.password1 !== $scope.password2){
                $scope.pwdsOK = false;
                document.getElementById('iiPassword2').innerHTML = "<i class=\"far fa-times-circle text-danger\"></i>";
                $scope.pwdsWrong = 'Passwords do not match!';
            }else{
                $scope.pwdsOK = true;
                $scope.pwdsWrong = '';
                document.getElementById('iiPassword1').innerHTML = "<i class=\"fas fa-check text-success\"></i>";
                document.getElementById('iiPassword2').innerHTML = "<i class=\"fas fa-check text-success\"></i>";
            }
            
            $scope.setDisabledSubmitBtn();
            
        };
        
        
        setInterval(()=> {
            //check periodically for unlocking submitBtn (need it cause of captcha verification outside of our scope... you can just infere its result from a callback)
            //anyway it'll be checked also server side
            $scope.setDisabledSubmitBtn();
        }, 1000);
        
        //disable btn for submit when all the required fields are not filled
        $scope.setDisabledSubmitBtn = function(){
            
            function hasCaptchaBeenVerified(){
                return parseInt(document.getElementById('recaptcha-response').value) > 0;//see below, when captcha verified, this is set to 1 by a callback      
            }
            
            if(!$scope.pwdsOK || !$scope.emailOK || !$scope.usernameOK || !hasCaptchaBeenVerified())
                document.getElementById("submitRegister").disabled = true;
                
            else
                document.getElementById("submitRegister").disabled = false;
        };
        
        /*------------------------- PWD RECOVERY ---------------------*/
        
        //show "ok check", when email valid and not taken by other users  
        $scope.emailRecOK = false;
        $scope.emailRecWrong = '';
        
        $scope.checkEmailRec = function() {
            
            if(!$scope.isValidEmail($scope.emailRecovery)){
                $scope.emailRecOK = false;
                $scope.setDisabledSubmitBtnRec();
                return;
            }
            
            $http.get("Players?email="+$scope.emailRecovery)
                .then(
                    (response) => { 
                        console.log(response);
                        if(response.data){
                            $scope.emailRecOK = true;
                            $scope.emailRecWrong = '';
                        }else{
                            $scope.emailRecOK = false;
                            $scope.emailRecWrong = 'No account found!';
                        }
                        
                        $scope.setDisabledSubmitBtnRec();
                    },
                    (error) =>  console.error(error)
                );
        };
        
        //disable btn for submit when all the required fields are not filled
        $scope.setDisabledSubmitBtnRec = function(){
            
            if(!$scope.emailRecOK)
                document.getElementById("submitRecovery").disabled = true;
                
            else
                document.getElementById("submitRecovery").disabled = false;
        };
       
        
        
        angular.element(document).ready(function () {
            
        });
        
}]);

//callback when user verify captcha (set input hidden with recaptcha-response to value greater than zero)
function verifyCaptcha() {
    document.getElementById('recaptcha-response').value = 1;
}
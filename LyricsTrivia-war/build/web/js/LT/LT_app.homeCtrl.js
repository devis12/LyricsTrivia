/*Controller within home*/
angular.module("LTApp").controller("homeCtrl", ['$scope', '$rootScope', '$http', '$httpParamSerializer', function($scope, $rootScope, $http, $httpParamSerializer) {
       $scope.page = 'home';
       $('.aNav').removeClass('active');
       $('#linkHome').addClass('active');
       //console.log($rootScope.username);
       
       if($scope.qp === undefined)
           getNewPracticeQuestion();
       
        function getNewPracticeQuestion(){
            $http.get("Practice")
                .then(
                    (response) => {
                        //when answer is already defined, fade effect to transition to the next one smoothly
                        if($scope.qp !== undefined){
                            $('.qoption').fadeTo(500, 0);
                            $('#qplyrics').fadeTo(500, 0);
                        }
                        
                        $scope.qp = response.data;
                        console.log($scope.qp);
                        $('.qoption').fadeTo(1000, 1);
                        $('#qplyrics').fadeTo(1000, 1);
                    },
                    (error) =>  console.error(error)
                );
        }
        
        function blinkRightAnswer(index){
            $('.qoption').removeClass('bg-success');
            
            for(let i=1; i<=4; i++)
                setTimeout(()=>{
                   if(i%2 !== 0){//already green, move to gray
                        $('#qpoption'+index).removeClass('bg-success');
                        $('#qpoption'+index).addClass('bg-secondary');
                   }else{//from gray to green
                        $('#qpoption'+index).addClass('bg-success');
                        $('#qpoption'+index).removeClass('bg-secondary');
                   }
                }, 500*i);
        }
        
        $scope.qpChoose = function(index){
          $('.qoption').prop("disabled", true);
          $scope.qp.givenAnswerIndex = index;//index chosen by the player
          $http({
                url: "Practice", 
                method: 'PUT',//tells the API, the answer given by the user and it'll reply with the question containing the right answer
                data: $httpParamSerializer($scope.qp), // Make sure to inject the service you choose to the controller
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    (response) => {
                        $scope.qp = response.data;
                        blinkRightAnswer($scope.qp.rightAnswerIndex + 1);//make the right button blinking green
                        console.log($scope.qp);
                        setTimeout(()=>{
                            getNewPracticeQuestion();//load a new practice question
                            
                            //reset options button
                            $('.qoption').prop("disabled", false);
                            $('.qoption').removeClass('bg-success');
                            $('.qoption').removeClass('bg-secondary');//remain btn-secondary
                        }, 6000);
                    },
                    (error) =>  console.error(error)
                );  
        };
        
}]);

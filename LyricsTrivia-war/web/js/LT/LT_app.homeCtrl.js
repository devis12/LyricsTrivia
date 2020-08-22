/*Controller within home*/
angular.module("LTApp").controller("homeCtrl", ['$scope', '$rootScope', '$http', '$httpParamSerializer', function($scope, $rootScope, $http, $httpParamSerializer) {
       $scope.page = 'home';
       $('.aNav').removeClass('active');
       $('#linkHome').addClass('active');
       //console.log($rootScope.username);
       
       if($scope.pq === undefined)
           getNewPracticeQuestion();
       
       if($scope.cq === undefined)
            getNewChallengeQuestion();
       
       setInterval(() => {//check every x seconds if there is a current challenge question that needs to be answered, otherwise get a new one from the server
           if($scope.cq === undefined)
            getNewChallengeQuestion();
       }, 5000);
       
       
        function getNewPracticeQuestion(){
            $http.get("Practice")
                .then(
                    (response) => {
                        //when answer is already defined, fade effect to transition to the next one smoothly
                        if($scope.pq !== undefined){
                            $('.pqoption').fadeTo(512, 0);
                            $('#pqlyrics').fadeTo(512, 0);
                        }
                        
                        $scope.pq = response.data;
                        console.log($scope.pq);
                        $('.pqoption').fadeTo(1024, 1);
                        $('#pqlyrics').fadeTo(1024, 1);
                        
                    },
                    (error) =>  console.error(error)
                );
        }
        
        function moveToWaitingBox(){
            //set opacity of waiting box to zero and remove d-none
            $('#waitChlgBox').fadeTo(0,0);
            $('#waitChlgBox').removeClass('d-none');
            
            //set opacity of newChgBox to zero slowly and add d-none
            $('#newChlgBox').fadeTo(512,0);
            $('#newChlgBox').addClass('d-none');
            
            //then slowly show waiting box
            setTimeout(()=>$('#waitChlgBox').fadeTo(512,1), 512);
        }
        
        function getNewChallengeQuestion(){
            $http.get("Challenge")
                .then(
                    (response) => {
                        
                        if(response.data === null || response.data === undefined){//no challenge waiting for answer -> move to waiting box
                            $scope.cq = undefined;
                            if ($('#waitChlgBox').hasClass('d-none'))//if you're not in waiting box and there is nothing to do, challenge otherwise do nothing -> that's ok, try again later    
                                moveToWaitingBox();
                        
                        }else{    
                            //there is a challenge question!
                            $scope.cq = response.data;
                            console.log($scope.cq);
                            
                            if (!$('#waitChlgBox').hasClass('d-none')){//waiting box is not hidden -> hide it slowly and bring up the newChlgBox
                                $('#newChlgBox').fadeTo(0, 0);
                                $('#waitChlgBox').fadeTo(512, 0);
                                $('#waitChlgBox').addClass('d-none');
                                setTimeout(()=>{
                                    $('#newChlgBox').removeClass('d-none');
                                    $('#newChlgBox').fadeTo(1024, 1);
                                    $('.cqoption').fadeTo(1024, 1);
                                    $('#cqlyrics').fadeTo(1024, 1);
                                },512);
                                
                            }else{//waiting box already hidden -> just bring up new variables for new challenge
                                $('.cqoption').fadeTo(1024, 1);
                                $('#cqlyrics').fadeTo(1024, 1);
                            }
                            
                        }
                        
                    },
                    (error) =>  console.error(error)
                );
        }
        
        function blinkRightAnswer(index, challenge){
            let cp = '';
            if(challenge)//challenge question
                cp = 'c';
            else
                cp = 'p';//practice question
            
            $('.'+cp+'qoption').removeClass('bg-success');
            
            for(let i=1; i<=6; i++)
                setTimeout(()=>{
                   if(i%2 !== 0){//already green, move to gray
                        $('#'+cp+'qoption'+index).removeClass('bg-success');
                        $('#'+cp+'qoption'+index).addClass('bg-secondary');
                   }else{//from gray to green
                        $('#'+cp+'qoption'+index).addClass('bg-success');
                        $('#'+cp+'qoption'+index).removeClass('bg-secondary');
                   }
                }, 400*i);
        }
        
        function showWrongAnswer(index, challenge){
            let cp = '';
            if(challenge)//challenge question
                cp = 'c';
            else
                cp = 'p';//practice question
            
            $('#'+cp+'qoption'+index).removeClass('bg-secondary');
            $('#'+cp+'qoption'+index).addClass('bg-danger');
        }
        
        $scope.pqChoose = function(index){
          $scope.pq.givenAnswerIndex = index;//index chosen by the player
          $http({
                url: "Practice", 
                method: 'PUT',//tells the API the given answer by the user and it'll reply with the question containing the right answer
                data: $httpParamSerializer($scope.pq), // Make sure to inject the service you choose to the controller
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    (response) => {
                        $scope.pq = response.data;
                        blinkRightAnswer($scope.pq.rightAnswerIndex + 1, false);//make the right button blinking green (false cause it's practice q)
                        if($scope.pq.rightAnswerIndex !== $scope.pq.givenAnswerIndex)
                            showWrongAnswer($scope.pq.givenAnswerIndex + 1, false);//make the wrong clicked button red (false cause it's practice q)
                        console.log($scope.pq);
                        setTimeout(()=>{
                            getNewPracticeQuestion();//load a new practice question
                            
                            //reset options button colors
                            $('.pqoption').removeClass('bg-success');
                            $('.pqoption').removeClass('bg-danger');
                            $('.pqoption').addClass('bg-secondary');
                        }, 10000);
                    },
                    (error) =>  console.error(error)
                );  
        };
        
        $scope.cqChoose = function(index){
          $scope.cq.givenAnswerIndex = index;//index chosen by the player
          $http({
                url: "Challenge", 
                method: 'PUT',//tells the API the given answer by the user and it'll reply with the question containing the right answer
                data: $httpParamSerializer($scope.cq), // Make sure to inject the service you choose to the controller
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    (response) => {
                        $scope.cq = response.data;
                        blinkRightAnswer($scope.cq.rightAnswerIndex + 1, true);//make the right button blinking green (true cause it's challenge q)
                        if($scope.cq.rightAnswerIndex !== $scope.cq.givenAnswerIndex)
                            showWrongAnswer($scope.cq.givenAnswerIndex + 1, true);//make the wrong clicked button red (true cause it's challenge q)
                        console.log($scope.cq);
                        setTimeout(()=>{
                            getNewChallengeQuestion();//search for a new challenge question
                            
                            //reset options button colors
                            $('.cqoption').removeClass('bg-success');
                            $('.cqoption').removeClass('bg-danger');
                            $('.cqoption').addClass('bg-secondary');
                        }, 10000);
                    },
                    (error) =>  console.error(error)
                );  
        };
        
}]);

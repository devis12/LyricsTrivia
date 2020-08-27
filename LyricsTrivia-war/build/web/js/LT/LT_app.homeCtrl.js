/*Controller within home*/
angular.module("LTApp")
        .controller("homeCtrl", 
            ['$scope', '$rootScope', '$timeout', '$interval', '$http', '$httpParamSerializer', 
                function($scope, $rootScope, $timeout, $interval, $http, $httpParamSerializer) {
       
        $scope.page = 'home';
        $rootScope.homeLinkC = 'active';
        $rootScope.playersLinkC = '';
        $rootScope.profileLinkC = '';
       //console.log($rootScope.username);
       
       if($scope.pq === undefined)
           getNewPracticeQuestion();
       
       if($scope.cq === undefined)
            getNewChallengeQuestion();
       
       $interval(() => {//check every x seconds if there is a current challenge question that needs to be answered, otherwise get a new one from the server
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
        
        $scope.showWaitBox = true;//show wait challenge
        $scope.showChallengeBox = false;//show challenge box
        
        function moveToWaitingBox(){
            //set opacity of waiting box to zero and remove d-none
            $('#waitChlgBox').fadeTo(0,0);
            $scope.showWaitBox = true;
            
            //set opacity of newChgBox to zero slowly and add d-none
            $('#newChlgBox').fadeTo(512,0);
            $scope.showChallengeBox = false;
            
            //then slowly show waiting box
            $timeout(()=>$('#waitChlgBox').fadeTo(512,1), 512);
        }
        
        function getNewChallengeQuestion(){
            $http.get("Challenge")
                .then(
                    (response) => {
                        
                        if(response.data === null || response.data === undefined){//no challenge waiting for answer -> move to waiting box
                            $scope.cq = undefined;
                            if (!$scope.showWaitBox)//if you're not in waiting box and there is nothing to do, challenge otherwise do nothing -> that's ok, try again later    
                                moveToWaitingBox();
                        
                        }else{    
                            //there is a challenge question!
                            $scope.cq = response.data;
                            console.log($scope.cq);
                            
                            if (!$scope.showChallengeBox){//waiting box is not hidden -> hide it slowly and bring up the newChlgBox
                                $('#newChlgBox').fadeTo(0, 0);
                                $('#waitChlgBox').fadeTo(512, 0);
                                $scope.showWaitBox = false;
                                $scope.showChallengeBox = true;
                                $timeout(()=>{
                                    
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
        
        //default color for options button in practice and challenge questions
        $scope.pqoptionC = ['bg-secondary','bg-secondary','bg-secondary','bg-secondary'];
        $scope.cqoptionC = ['bg-secondary','bg-secondary','bg-secondary','bg-secondary'];
        
        /* reset all options button colors to gray         
         * challenge flag to indicate if you have to do it wrt challenge options or practice
         * */
        function resetOptionsBtn(challenge){
            if(challenge)//challenge box options
                for(let i=0; i<$scope.cqoptionC.length; i++)
                    $scope.cqoptionC[i] = 'bg-secondary';
           
            else//practice box options
                for(let i=0; i<$scope.pqoptionC.length; i++)
                    $scope.pqoptionC[i] = 'bg-secondary';
                
            
        }
        
        /* change every x ms the background of the right button answer, making it blinking green
         * challenge flag to indicate if you have to do it wrt challenge options or practice
         * */
        function blinkRightAnswer(index, challenge){
            
            for(let i=1; i<=6; i++)
                $timeout(()=>{
                   if(i%2 !== 0){//already green, move to gray
                       if(challenge)
                           $scope.cqoptionC[index] = 'bg-secondary';
                       else
                           $scope.pqoptionC[index] = 'bg-secondary';
                   }else{//from gray to green
                       if(challenge)
                           $scope.cqoptionC[index] = 'bg-success';
                       else
                           $scope.pqoptionC[index] = 'bg-success';
                    }
                }, 400*i);
        }
        
        /* display wrong selected options with red background
         * challenge flag to indicate if you have to do it wrt challenge options or practice
         * */
        function showWrongAnswer(index, challenge){
            if(challenge)
                $scope.cqoptionC[index] = 'bg-danger';
            else
                $scope.pqoptionC[index] = 'bg-danger';
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
                        blinkRightAnswer($scope.pq.rightAnswerIndex, false);//make the right button blinking green (false cause it's practice q)
                        if($scope.pq.rightAnswerIndex !== $scope.pq.givenAnswerIndex)
                            showWrongAnswer($scope.pq.givenAnswerIndex, false);//make the wrong clicked button red (false cause it's practice q)
                        console.log($scope.pq);
                        $timeout(()=>{
                            getNewPracticeQuestion();//load a new practice question
                            
                            //reset options button colors
                            resetOptionsBtn(false);//practice
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
                        blinkRightAnswer($scope.cq.rightAnswerIndex, true);//make the right button blinking green (true cause it's challenge q)
                        if($scope.cq.rightAnswerIndex !== $scope.cq.givenAnswerIndex)
                            showWrongAnswer($scope.cq.givenAnswerIndex, true);//make the wrong clicked button red (true cause it's challenge q)
                        console.log($scope.cq);
                        $timeout(()=>{
                            getNewChallengeQuestion();//search for a new challenge question
                            
                            //reset options button colors
                            resetOptionsBtn(true);//true 'cause challenge box
                        }, 10000);
                    },
                    (error) =>  console.error(error)
                );  
        };
        
}]);

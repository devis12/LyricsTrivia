/*Controller for players sub-page: containing list of all online/offline players*/
angular.module("LTApp").controller("playersCtrl", ['$scope', '$rootScope', '$http', '$httpParamSerializer', function($scope, $rootScope, $http, $httpParamSerializer) {
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
        setInterval(refreshPlayersList, 2000);  
  
        $scope.newChg = {};
        
        function resetNewChlgValues(){
            //cleanup search params
            $scope.trackName = '';
            $scope.trackArtist = '';
            $scope.sslDB = []; //cleanup search result table
            
            $scope.trackID1 = false;
            $scope.trackID2 = false;
            $scope.trackID3 = false;
            $scope.trackID4 = false;
            
            $scope.track1Txt = '';
            $scope.track2Txt = '';
            $scope.track3Txt = '';
            $scope.track4Txt = '';
            
            setTrackIDVisibility();
            
            $scope.rightTrackID = "1";//put the radio checked ad default to 1
            
            $('#submitNewChlg').removeClass("btn-success");
            $('#submitNewChlg').addClass("btn-primary");
        }
        
        function setTrackIDVisibility(){
            
            $('.setTrackID1').prop('disabled', !isNaN(parseInt($scope.trackID1)));
            $('.setTrackID2').prop('disabled', !isNaN(parseInt($scope.trackID2)));
            $('.setTrackID3').prop('disabled', !isNaN(parseInt($scope.trackID3)));
            $('.setTrackID4').prop('disabled', !isNaN(parseInt($scope.trackID4)));
            
        }
        
        $scope.newChlgModal = function(username){
            $scope.newChg.askingPlayer = $rootScope.username;
            $scope.newChg.askedPlayer = username;
            resetNewChlgValues();
            $('#newChallengeModal').modal();
        };
        
        $scope.searchSongs = function(){
            //GUI adjustments when clicking
            document.getElementById("searchBtn").disabled = true;
            $("#searchTxt").addClass('d-none');
            $("#loadFa").removeClass('d-none');
            
            let name = $scope.trackName;
            let artist = $scope.trackArtist;
            
            let url = "Songs?storage=lyricstrivia&";
            
            if(name !== undefined && name !== '')
                url += "trackName=" + name + "&";
            if(artist !== undefined && artist !== '')
                url += "trackArtist=" + artist;
            
            $http.get(url)
                .then(
                    (response) => { 
                        //put in the scope objects an empty array in case there is just one single
                        $scope.sslDB = response.data.lyricstrivia;
                         //(do it just a little bit after to be more sure that ng-repeat has visualized content) //TODO check if there is any callback for it
                        setTimeout(() => {setTrackIDVisibility();}, 128);   //adjust visibility wrt track already selected 
                            
                        console.log(response.data);
                        //$scope.sslJMM = response.data.jmusixmatch;
                        
                        //GUI adjustments after response
                        document.getElementById("searchBtn").disabled = false;
                        $("#searchTxt").removeClass('d-none');
                        $("#loadFa").addClass('d-none');
                        
                    },
                    (error) =>  console.error(error)
                );
        };
        
        $scope.eraseTrack = function(idOption){
            //you cannot remove it, until you re-add it
             $("#track"+ idOption +"Erase").prop('disabled', true);
            
            switch(idOption){
                case 1: 
                    $scope.trackID1 = false;
                    $scope.track1Txt = '';
                    $scope.newChg.trackID1 = undefined;
                    break;
                case 2: 
                    $scope.trackID2 = false;
                    $scope.track2Txt = '';
                    $scope.newChg.trackID2 = undefined;
                    break;
                case 3: 
                    $scope.trackID3 = false;
                    $scope.track3Txt = '';
                    $scope.newChg.trackID3 = undefined;
                    break;
                case 4: 
                    $scope.trackID4 = false;
                    $scope.track4Txt = '';
                    $scope.newChg.trackID4 = undefined;
                    break;
            }
            
            //you can now re-add it
            $('.setTrackID'+idOption).prop('disabled', false);
            
            //enable/disable submit
             $scope.enableDisableSubmitChlg();
        };
        
        function alreadySet(trackID){
            return $scope.trackID1 == trackID || $scope.trackID2 == trackID || $scope.trackID3 == trackID || $scope.trackID4 == trackID;
        }
        
        $scope.setTrackID = function(idOption, trackName, trackArtist, trackID){
            if(alreadySet(trackID)){//avoid duplicated options
                alert("You cannot set two times the same song in a question!");
                return;
            }
                
            //cannot re-set it, until you remove it
            $('.setTrackID'+idOption).prop('disabled', true);
            switch(idOption){
                case 1: 
                    $scope.trackID1 = trackID;
                    $scope.track1Txt = trackName + " ("+ trackArtist +")";
                    $scope.newChg.trackID1 = trackID;
                    break;
                case 2: 
                    $scope.trackID2 = trackID;
                    $scope.track2Txt = trackName + " ("+ trackArtist +")";
                    $scope.newChg.trackID2 = trackID;
                    break;
                case 3: 
                    $scope.trackID3 = trackID;
                    $scope.track3Txt = trackName + " ("+ trackArtist +")";
                    $scope.newChg.trackID3 = trackID;
                    break;
                case 4: 
                    $scope.trackID4 = trackID;
                    $scope.track4Txt = trackName + " ("+ trackArtist +")";
                    $scope.newChg.trackID4 = trackID;
                    break;
            }
            
            //possibility to remove it and set another one
             $("#track"+ idOption +"Erase").prop('disabled', false);
             
             //enable/disable submit
             $scope.enableDisableSubmitChlg();
        };
        
        //disable submit button, until all required fields are filled
        $scope.enableDisableSubmitChlg = function(){
          let trackID1 = parseInt($scope.trackID1); 
          let trackID2 = parseInt($scope.trackID2); 
          let trackID3 = parseInt($scope.trackID3); 
          let trackID4 = parseInt($scope.trackID4); 
          console.log(trackID1);
          console.log(trackID2);
          console.log(trackID3);
          console.log(trackID4);
          if(!isNaN(trackID1) && !isNaN(trackID2) && !isNaN(trackID3) && !isNaN(trackID4))
              $('#submitNewChlg').prop('disabled', false);
          else
              $('#submitNewChlg').prop('disabled', true);
        };
        
        
        $scope.throwNewChallenge = function(){
            
            $scope.rightTrackID = parseInt($scope.rightTrackID);
            switch($scope.rightTrackID){
                case 1: $scope.newChg.rightTrackID = $scope.newChg.trackID1; break;
                case 2: $scope.newChg.rightTrackID = $scope.newChg.trackID2; break;
                case 3: $scope.newChg.rightTrackID = $scope.newChg.trackID3; break;
                case 4: $scope.newChg.rightTrackID = $scope.newChg.trackID4; break;
            }
            console.log($scope.newChg);
            
            $http({
                url: ("Challenge"), //
                method: 'POST',
                data: $httpParamSerializer($scope.newChg), // 
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    () => {
                        //$('#newChallengeModal').modal('toggle');
                        $('#submitNewChlg').removeClass("btn-primary");
                        $('#submitNewChlg').addClass("btn-success");
                        $('#submitNewChlg').prop("disabled", true);
                        $('#submitNewChlg').val("SENT");
                        setTimeout(() => $('#newChallengeModal').modal('toggle'), 3000);
                    },
                    (error) =>  console.error(error)
                );
            };
        
    }]);

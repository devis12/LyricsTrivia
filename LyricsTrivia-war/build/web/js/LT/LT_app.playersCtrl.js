/*Controller for players sub-page: containing list of all online/offline players*/
angular.module("LTApp")
        .controller("playersCtrl", 
            
            ['$scope', '$rootScope', '$timeout', '$interval', '$http', '$httpParamSerializer', 
                function($scope, $rootScope, $timeout, $interval, $http, $httpParamSerializer) {
                    
        $scope.page = 'players';
        $rootScope.homeLinkC = '';
        $rootScope.playersLinkC = 'active';
        $rootScope.profileLinkC = '';
        
        $scope.playersOnlineList = [];
        
        $scope.orderByMe = function(x) {
            $scope.myOrderBy = x;
        };
        
        /*Function to periodically refresh players online status & stats*/
        function refreshPlayersList(){
            $http.get("Players?online_status=1")
                .then(
                    (response) => {
                        $scope.playersOnlineList = response.data.filter( (p)=>(p.username != $rootScope.username));
                    },
                    (error) =>  console.error(error)
                );
        }
        
        /*refresh periodically online status and number of all players*/
        refreshPlayersList();
        $scope.orderByMe("username");//order automatically, just first time
        $interval(refreshPlayersList, 2000);  
  
        $scope.newChg = {};
        
        $scope.submitNewChlgC = 'btn-primary';//submit new challenge color
        $scope.submitNewChlgV = 'Challenge';//submit new challenge value
        $scope.submitNewChlgE = false;//submitnew challenge enabled
        
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
            
            setTrackIDEnabled();
            
            $scope.rightTrackID = "1";//put the radio checked ad default to 1
            
            $scope.submitNewChlgC = 'btn-primary';
            $scope.submitNewChlgV = 'Challenge';
            $scope.submitNewChlgE = false;
        }
        
        $scope.setTrackID1E = true;//true (i.e. enabled) if value is not chosen, i.e. isNaN
        $scope.setTrackID2E = true;//true (i.e. enabled) if value is not chosen, i.e. isNaN
        $scope.setTrackID3E = true;//true (i.e. enabled) if value is not chosen, i.e. isNaN
        $scope.setTrackID4E = true;//true (i.e. enabled) if value is not chosen, i.e. isNaN
            
        //change the enable/disable property of setTrackIDx buttons
        function setTrackIDEnabled(){
            $scope.setTrackID1E = isNaN(parseInt($scope.trackID1));//true (i.e. enabled) if value is not chosen, i.e. isNaN
            $scope.setTrackID2E = isNaN(parseInt($scope.trackID2));//true (i.e. enabled) if value is not chosen, i.e. isNaN
            $scope.setTrackID3E = isNaN(parseInt($scope.trackID3));//true (i.e. enabled) if value is not chosen, i.e. isNaN
            $scope.setTrackID4E = isNaN(parseInt($scope.trackID4));//true (i.e. enabled) if value is not chosen, i.e. isNaN
        }
        
        $scope.newChlgModal = function(username){
            $scope.newChg.askingPlayer = $rootScope.username;
            $scope.newChg.askedPlayer = username;
            resetNewChlgValues();//reset modal
            $('#newChallengeModal').modal();//show modal
        };
        
        
        $scope.searchBtnE = true; //enable searchBtn
        $scope.searching = false; //NOT during searching phase
        $scope.searchSongs = function(){
            //GUI adjustments when clicking
            $scope.searchBtnE = false; //disabled searchBtn
            $scope.searching = true; //during searching phase
            
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
                        $timeout(() => {setTrackIDEnabled();}, 128);   //adjust visibility wrt track already selected 
                        
                        //GUI adjustments after response
                        $scope.searchBtnE = true; //enable searchBtn
                        $scope.searching = false; //searching phase terminated
                    },
                    (error) =>  console.error(error)
                );
        };
        
        $scope.eraseTrack = function(idOption){
            //you cannot remove it, until you re-add it
            
            switch(idOption){
                case 1: 
                    $scope.trackID1 = false;
                    $scope.track1Txt = '';
                    $scope.setTrackID1E = true;//enable trackSet btn
                    $scope.eraseTrack1E = false;//disable trackErase btn
                    $scope.newChg.trackID1 = undefined;
                    break;
                case 2: 
                    $scope.trackID2 = false;
                    $scope.track2Txt = '';
                    $scope.setTrackID2E = true;
                    $scope.eraseTrack2E = false;
                    $scope.newChg.trackID2 = undefined;
                    break;
                case 3: 
                    $scope.trackID3 = false;
                    $scope.track3Txt = '';
                    $scope.setTrackID3E = true;
                    $scope.eraseTrack3E = false;
                    $scope.newChg.trackID3 = undefined;
                    break;
                case 4: 
                    $scope.trackID4 = false;
                    $scope.track4Txt = '';
                    $scope.setTrackID4E = true;
                    $scope.eraseTrack4E = false;
                    $scope.newChg.trackID4 = undefined;
                    break;
            }
            
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

            switch(idOption){
                case 1: 
                    $scope.trackID1 = trackID;
                    $scope.setTrackID1E = false;//disable trackSet btn
                    $scope.eraseTrack1E = true;//enable erase track btn
                    $scope.track1Txt = trackName + " ("+ trackArtist +")";
                    $scope.newChg.trackID1 = trackID;
                    break;
                case 2: 
                    $scope.trackID2 = trackID;
                    $scope.setTrackID2E = false;
                    $scope.eraseTrack2E = true;
                    $scope.track2Txt = trackName + " ("+ trackArtist +")";
                    $scope.newChg.trackID2 = trackID;
                    break;
                case 3: 
                    $scope.trackID3 = trackID;
                    $scope.setTrackID3E = false;
                    $scope.eraseTrack3E = true;
                    $scope.track3Txt = trackName + " ("+ trackArtist +")";
                    $scope.newChg.trackID3 = trackID;
                    break;
                case 4: 
                    $scope.trackID4 = trackID;
                    $scope.setTrackID4E = false;
                    $scope.eraseTrack4E = true;
                    $scope.track4Txt = trackName + " ("+ trackArtist +")";
                    $scope.newChg.trackID4 = trackID;
                    break;
            }
             
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
              $scope.submitNewChlgE = true;
          else
              $scope.submitNewChlgE = false;
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
                        $scope.submitNewChlgC = 'btn-success';
                        $scope.submitNewChlgE = false;
                        $scope.submitNewChlgV = 'SENT';
                        
                        $timeout(() => $('#newChallengeModal').modal('toggle'), 3000);
                    },
                    (error) =>  console.error(error)
                );
            };
        
    }]);

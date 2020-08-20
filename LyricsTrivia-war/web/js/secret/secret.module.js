angular.module('secret', [])
    .controller('secretCtrl', ['$scope', '$http', '$httpParamSerializer', function($scope, $http, $httpParamSerializer) {
             
        /*------------------------- Search Song ---------------------*/

        $scope.sslDB = [];
        $scope.sslJMM = [];
        
        $scope.cliAdminTxt = '> _';
        
        $scope.searchSongs = function(){
            //GUI adjustments when clicking
            document.getElementById("searchBtn").disabled = true;
            $("#searchTxt").addClass('d-none');
            $("#loadFa").removeClass('d-none');
            
            let name = $scope.trackName;
            let artist = $scope.trackArtist;
            
            let url = "Songs?";
            
            if(name !== undefined && name !== '')
                url += "trackName=" + name + "&";
            if(artist !== undefined && artist !== '')
                url += "trackArtist=" + artist;
            
            $http.get(url)
                .then(
                    (response) => { 
                        //put in the scope objects an empty array in case there is just one single
                        $scope.sslDB = response.data.lyricstrivia;
                        //console.log(response.data);
                        $scope.sslJMM = response.data.jmusixmatch;
                        
                        $scope.cliAdminTxt = '> Found ' + $scope.sslDB.length + " matching songs in local dump and " + $scope.sslJMM.length + " using jmusixmatch APIs";
                        cliColor(false, false);//adjust color of displayed text
                        //
                        //GUI adjustments after response
                        document.getElementById("searchBtn").disabled = false;
                        $("#searchTxt").removeClass('d-none');
                        $("#loadFa").addClass('d-none');
                    },
                    (error) =>  console.error(error)
                );
        };
        
        /*find lyrics in a vector of songlyrics by trackID*/
        function findLyrics(trackID, ssl){
            trackID = parseInt(trackID);
            for(let sl of ssl)
                if(parseInt(sl.trackID) === trackID)
                    return sl.trackLyrics;
            
            return null;
        }
        
        $scope.showLyrics = function(trackID, title, DB, JMM){
            let lyrics = '';
            if(DB)
                lyrics = findLyrics(trackID, $scope.sslDB);
            if(JMM)
                lyrics = findLyrics(trackID, $scope.sslJMM);
            
            $scope.modalLyricsTxt = lyrics;
            $scope.modalLyricsTitle = title;
            $('#modalLyrics').modal();
        };
        
        /*-------------------- EDITING DUMP -----------------------*/
        
        /*find index of specific sl in array, given its trackID*/
        function getSlIndex(trackID, ssl){
            trackID = parseInt(trackID);
            for(let i=0; i<ssl.length; i++)
                if(parseInt(ssl[i].trackID) === trackID)
                    return i;
            
            return -1;
        }
        
        function cliColor(success, danger){
            
            $('#cliAdminTxt').removeClass('text-dark');
            $('#cliAdminTxt').removeClass('text-danger');
            $('#cliAdminTxt').removeClass('text-success');
            if(danger)
                $('#cliAdminTxt').addClass('text-danger');
            else if(success)
                $('#cliAdminTxt').addClass('text-success');
            else
                $('#cliAdminTxt').addClass('text-dark');
        }
        
        $scope.addSongLyricsDB = function(sl){
            $http({
                url: ("Songs/"), //
                method: 'POST',
                data: $httpParamSerializer(sl), // 
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    () => {
                        $scope.sslDB.push(sl);//add element in array of matching ssl in dump
                        $scope.cliAdminTxt = '> Added ' + sl.trackName + ' of ' + sl.trackArtist + '(trackID=' + sl.trackID + ') in local dump' ;
                        cliColor(true, false);//adjust color of displayed text
                    },
                    (error) =>  console.error(error)
                );
            };
            
        $scope.delSongLyricsDB = function(sl){
            let index = getSlIndex(sl.trackID, $scope.sslDB);
            console.log(index);
            if(index < 0)//index not valid
                return;
            
            $http({
                url: ("Songs/"+sl.trackID), //
                method: 'DELETE',
                data: $httpParamSerializer(sl), // 
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    () => {
                        $scope.sslDB.splice(index, 1);//delete element from array scope variable
                        $scope.cliAdminTxt = '> Removed ' + sl.trackName + ' of ' + sl.trackArtist + '(trackID=' + sl.trackID + ') from local dump' ;
                        cliColor(false, true);//adjust color of displayed text
                    }, 
                    (error) =>  console.error(error)
                );
            };
        
        
        angular.element(document).ready(function () {
            
        });
        
}]);

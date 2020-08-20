angular.module('secret', [])
    .controller('secretCtrl', ['$scope', '$http', '$httpParamSerializer', function($scope, $http) {
             
        /*------------------------- Search Song ---------------------*/

        $scope.sslDB = [];
        $scope.sslJMM = [];

        $scope.searchSongs = function(){
            //GUI adjustments when clicking
            document.getElementById("searchBtn").disabled = true;
            $("#searchTxt").addClass('d-none');
            $("#loadFa").removeClass('d-none');
            
            let name = $scope.trackName;
            let artist = $scope.trackArtist;
            
            let url = "Songs?";
            
            if(name !== undefined)
                url += "trackName=" + name + "&";
            if(artist !== undefined)
                url += "trackArtist=" + artist + "&";
            
            $http.get(url)
                .then(
                    (response) => { 
                        
                        $scope.sslDB = response.data.lyricstrivia;
                        //console.log(response.data);
                        $scope.sslJMM = response.data.jmusixmatch;
                        
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
        
        
        angular.element(document).ready(function () {
            
        });
        
}]);

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module('playersCRUD', [])
    .controller('playersCtrl', ['$scope', '$http', '$httpParamSerializer', function($scope, $http, $httpParamSerializer) {
         
        function setPlayerScope(player){
            $scope.username = player? player.username : null;
            $scope.email = player? player.email : null;
            $scope.pwd = player? player.pwd : null;
            $scope.age = player? player.age : null;
            
            $scope.isMale = false;
            $scope.isFemale = false;
            $scope.isOther = false;
            
            if(player){
                switch(player.genre){
                    case 'M': $scope.isMale = true; break;
                    case 'F': $scope.isFemale = true; break;
                    case 'O': $scope.isOther = true; break;
                }
            }
            
            
            $scope.played = player? player.played : null;
            $scope.won = player? player.won : null;
        } 
        
        $scope.checkGenre = function (){
          
            console.log($scope.genre);
            console.log($scope.isMale);
            console.log($scope.isFemale);
            console.log($scope.isOther);
        };
        
        function getPlayerScope(){
            
            let player = {
                username: $scope.username,
                email : $scope.email,
                pwd : $scope.pwd,
                age : $scope.age,
                genre : $scope.genre,
                played : $scope.played,
                won : $scope.won
            };
            
            return player;
        } 
        
        $scope.addPlayer = function() {
            let player = getPlayerScope();
            console.log("Trying to add following player:" + JSON.stringify(player));
            
            $http({
                url: "Players/",
                method: 'POST',
                data: $httpParamSerializer(player), // Make sure to inject the service you choose to the controller
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    () => {console.log("success");},
                    (error) =>  console.error(error)
                );
        };
        
        $scope.deletePlayer = function() {
            let player = getPlayerScope();
            console.log("Trying to delete following player:" + JSON.stringify(player));
            
            $http({
                url: "Players/"+player.username,
                method: 'DELETE',
                data: $httpParamSerializer(player), // Make sure to inject the service you choose to the controller
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    () => {console.log("success");},
                    (error) =>  console.error(error)
                );
        };
        
        $scope.editPlayer = function() {
            let player = getPlayerScope();
            console.log("Trying to edit following player: " + JSON.stringify(player));
            
            $http({
                url: "Players/"+player.username,
                method: 'PUT',
                data: $httpParamSerializer(player), // Make sure to inject the service you choose to the controller
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded' // Note the appropriate header
                }
              })
                .then(
                    () => {console.log("success");},
                    (error) =>  console.error(error)
                );
        };
        
        $scope.findPlayer = function() {
            console.log("Trying to find player having username: " + $scope.username);
            $http.get("Players/"+$scope.username)
                .then(
                    (response) => setPlayerScope(response.data),
                    (error) =>  console.error(error)
                );
        };
}]);

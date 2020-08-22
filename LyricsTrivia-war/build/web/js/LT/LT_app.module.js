let LTApp = angular.module("LTApp", ['ngRoute', 'ngSanitize']);

LTApp.run(function($rootScope) {
    $rootScope.username = $('#usernamePlayer').val();
});

LTApp.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/Home', {
                templateUrl: './templates/angular/home.template.html',
                controller: 'homeCtrl'
            }).
            when('/Players', {
                templateUrl: './templates/angular/players.template.html',
                controller: 'playersCtrl'
            }).
            when('/Profile', {
                templateUrl: './templates/angular/profile.template.html',
                controller: 'profileCtrl'
            }).
            otherwise({
                redirectTo: '/Home',
                controller: 'homeCtrl'
            });
    }]);



LTApp.filter('onlineStatus', function($sce) {
    return function(online) {
        let circle;
        if(online)//put a green circle
            circle = "<svg width='32' height='32'><circle cx='16' cy='16' r='12' stroke='green' stroke-width='0' fill='green' /></svg> ";
        else
            circle = "<svg width='32' height='32'><circle cx='16' cy='16' r='12' stroke='red' stroke-width='0' fill='red' /></svg> ";
        
        return $sce.trustAsHtml(circle);
    };
});

LTApp.filter('computeAge', function() {
    return function(birthdate) {
        let now = new Date();
        let birth = new Date(birthdate);
        let yearsDiff = now.getFullYear() - birth.getFullYear();
        if(isNaN(yearsDiff))//user has not inserted its birth date
            return '';
        
        if(now.getMonth() < birth.getMonth() || now.getMonth() == birth.getMonth() && now.getDate() < birth.getDate()) //he/she is still not <yearsDiff>
            yearsDiff--;
        
        return yearsDiff;
    };
});

LTApp.filter('noNewLine', function($sce) {
    return function(s) {
        if(s === undefined)
            return "";
        else
            return $sce.trustAsHtml(s.replace("\n", "<br />"));
    };
});

/*remove additional info in trackname / track artist if they occupy too much space*/
LTApp.filter('cleanTrackInfo', function() {
    return function(s) {
        if(s === undefined)
            return "";
        else{
            if(s.indexOf("(") > 0 && s.indexOf(")") > s.length/2)
                s = s.substring(0, s.indexOf("("));
            if(s.indexOf("-") > s.length/2)
                s = s.substring(0, s.indexOf("-"));
            return s;
        }
    };
});
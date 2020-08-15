<%-- 
    Document   : standings
    Created on : 15-Aug-2020, 11:53:25
    Author     : devis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
        <script src="js/playersCRUD.js"></script>
    </head>
    <body>
        
        <h1>Players</h1>
        
        <table ng-app="playersCRUD" ng-controller="playersCtrl">
            <tr>
                <td>Username</td>
                <td><input type="text" ng-model="username" name="username" value="{{username}}"/></td>
            </tr>
            <tr>
                <td>Email</td>
                <td><input type="email" ng-model="email" name="email" value="{{email}}"/></td>
            </tr>
            <tr>
                <td>Pwd</td>
                <td><input type="text" ng-model="pwd" name="pwd" value="{{pwd}}"/></td>
            </tr>
            <tr>
                <td>Age</td>
                <td><input type="number" ng-model="age" name="age" value="{{age}}"/></td>
            </tr>
            <tr>
                <td>Genre</td>
                <td>
                    <input type="radio" ng-model="gender" name="gender" value="M" ng-checked="isMale || !isFemale && !isOther"/>
                    <label for="male">Male</label><br>
                    <input type="radio" ng-model="gender" name="gender" value="F" ng-checked="isFemale"/>
                    <label for="female">Female</label><br>
                    <input type="radio" ng-model="gender" name="gender" value="O" ng-checked="isOther"/>
                    <label for="other">Other</label> 
                </td>
                <td><input type="button" ng-click="checkGenre()"></td>
            </tr>
            <tr>
                <td>Played</td>
                <td><input type="number" ng-model="played" name="played" value="0" value="{{played}}"/></td>
            </tr>
            <tr>
                <td>Won</td>
                <td><input type="number" ng-model="won" name="won" value="0" value="{{won}}"/></td>
            </tr>

            <tr>
                <td colspan="2">
                    <input type="button" name="action" value="Add" ng-click="addPlayer()"/>
                    <input type="button" name="action" value="Edit" ng-click="editPlayer()"/>
                    <input type="button" name="action" value="Delete" ng-click="deletePlayer()"/>
                    <input type="button" name="action" value="Search" ng-click="findPlayer()"/>
                </td>
            </tr>
            
        </table>
        
        <hr style="margin-top: 5%">    
            
        <table border="1">
            <thead>
                    <tr>
                        <th>Player</th>
                        <th>Age</th>
                        <th>Genre</th>
                        <th>Played Matches</th>
                        <th>Won Matches</th>
                    </tr>
                </thead>
                
                <c:forEach items="${requestScope.players}" var="p">
                    <tr>
                        <td>${p.username}</td>
                        <td>${p.age}</td>
                        <td>${p.genre}</td>
                        <td>${p.played}</td>
                        <td>${p.won}</td>
                    </tr>
                </c:forEach>
                
        </table>
        
    </body>
</html>

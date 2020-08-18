<%-- 
    Document   : home_page
    Created on : 17-Aug-2020, 15:34:17
    Author     : devis
--%>
<%-- 
    Document   : landing
    Created on : 16-Aug-2020, 11:50:58
    Author     : devis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <!--Generic meta-tag & css file to include-->
        <%@include file="generic_header.jsp" %>
        
        <!--Generic js scripts/libraries to include (for bootstrap, AngularJS, jquery)-->
        <%@include file="generic_js.jsp" %>
        
        <script src="js/LT_routing.js"></script>
        
    </head>
        
    <body class="text-center"  ng-app="LTApp" ng-controller="LTController">
        <div class="d-flex w-100 h-100 p-3 mx-auto flex-column">
            <header class="masthead mb-auto">
                <div class="inner">
                  <h3 class="masthead-brand"><i class="fas fa-music mr-4 chgColor2"></i>LyricsTrivia<i class="ml-4 fas fa-music chgColor1"></i></h3>
                  <nav class="nav nav-masthead justify-content-center">
                    <a class="nav-link active aNav" href="#Home">Home</a>
                    <a class="nav-link aNav" href="#Players">Players</a>
                    <a class="nav-link aNav" href="#Account">Account</a>
                    <a class="nav-link aNav" href="Logout">Logout</a>
                  </nav>
                </div>
            </header>

            <main role="main" class="inner cover" ng-view>

            </main>

            <footer class="mastfoot mt-auto">
                <div class="inner">
                <p class="txt1">LyricsTrivia game produced by <a href="mailto:devis.dalmoro@studenti.unitn.it">Devis Dal Moro</a></p>
                </div>
            </footer>
        </div>
                
        <!--Lyrics Trivia scripts-->
        <script src="js/LT_main.js"></script>
    </body>
    
</html>


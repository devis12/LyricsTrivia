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
        
    </head>
        
    <body class="text-center">
        <div class="d-flex w-100 h-100 p-3 mx-auto flex-column">
            <header class="mb-auto">                 
            </header>

            <main role="main">

                <div class="title">
                    <h1><i class="fas fa-music mr-4 chgColor2"></i>LyricsTrivia<i class="ml-4 fas fa-music chgColor1"></i></h1>
                </div>
                
                <p>Logged as ${sessionScope.player.username}</p> 

            </main>

            <footer class="mastfoot mt-auto">
                <div class="inner">
                <p class="txt1">LyricsTrivia game produced by <a href="mailto:devis.dalmoro@studenti.unitn.it">Devis Dal Moro</a></p>
                </div>
            </footer>
        </div>



        <!--Lyrics Trivia scripts-->
    </body>
    
</html>


<%-- 
    Document   : home_page
    Created on : 17-Aug-2020, 15:34:17
    Author     : devis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <!--Generic meta-tag & css file to include-->
        <%@include file="templates/jsp/generic_header.jsp" %>
        
        <!--Generic js scripts/libraries to include (for bootstrap, AngularJS, jquery)-->
        <%@include file="templates/jsp/generic_js.jsp" %>
        <!--Specific libs that you'll need in this page-->
        <%@include file="templates/jsp/home_js.jsp" %>
        
    </head>
        
    <body class="text-center"  ng-app="LTApp">
        
        <!-- username of the player that just logged in -->
        <input type="hidden" id="usernamePlayer" value="${sessionScope.player.username}" />
        
        <div class="d-flex w-100 h-100 p-3 mx-auto flex-column">
            <header class="masthead mb-auto">
                <div class="inner">
                  <h3 class="masthead-brand"><i class="fas fa-music mr-4 chgColor2"></i>LyricsTrivia<i class="ml-4 fas fa-music chgColor1"></i></h3>
                  <nav class="nav nav-masthead justify-content-center">
                    <a id="linkHome" class="nav-link aNav" href="#Home">Home</a>
                    <a id="linkPlayers" class="nav-link aNav" href="#Players">Players</a>
                    <a id="linkProfile" class="nav-link aNav" href="#Profile">Account</a>
                    <a class="nav-link aNav" href="Logout">Logout</a>
                  </nav>
                </div>
            </header>
            
            <main role="main" class="inner cover" ng-view>

            </main>

            <footer class="mastfoot mt-auto">
                
                <div aria-live="polite" aria-atomic="true" style="position: relative; min-height: 200px;">
                    <!-- Position it -->
                    <div id="toastStack" style="position: absolute; bottom: 0; right: 0;">

                        <!-- Then put toasts within -->
                        <div class="toast bg-dark text-light" role="alert" aria-live="assertive" aria-atomic="true" data-delay="5000">
                          <div class="toast-header">
                            <strong class="mr-auto">New Challenge!</strong>
                            <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                            </button>
                          </div>
                          <div class="toast-body">
                            from 
                          </div>
                        </div>
                    
                    </div>
                </div>
                
                <div class="inner">
                <p class="txt1">LyricsTrivia game produced by <a href="mailto:devis.dalmoro@studenti.unitn.it">Devis Dal Moro</a></p>
                </div>
            </footer>
        </div>
                
        <!--Lyrics Trivia utils scripts for resizing-->
        <script src="js/ui_utils.js"></script>
    </body>
    
</html>


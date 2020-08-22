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
        <%@include file="templates/jsp/generic_header.jsp" %>
        
        <!--Generic js scripts/libraries to include (for bootstrap, AngularJS, jquery)-->
        <%@include file="templates/jsp/generic_js.jsp" %>
        <!--Specific libs that you'll need in this page-->
        <%@include file="templates/jsp/landing_js.jsp" %>
        
    </head>
        
    <body class="text-center">
        <div class="d-flex w-100 h-100 p-3 mx-auto flex-column">
            <header class="mb-auto">                 
            </header>

            <main role="main">

                <div class="title">
                    <h1><i class="fas fa-music mr-4 chgColor2"></i>LyricsTrivia<i class="ml-4 fas fa-music chgColor1"></i></h1>
                    <h3>The game to test your lyrics' knowledge </h3>
                    <hr />
                    <p class="txt1">
                        From this web platform, you'll be able to challenge your friend in engaging trivia games testing your music culture. 
                    </p>
                </div>

                
                <div class="container-login100">
                    <div class="p-b-160 p-t-50">
                        
                        <div class="row mt-2">
                            <div class="col-1"></div>
                            <div class="col-10">
                                <c:choose>
                                    <c:when test="${requestScope.error_msg != null}">
                                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                            <strong>ERROR!</strong> ${requestScope.error_msg}
                                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                              <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:when test="${requestScope.warning_msg != null}">
                                        <div class="alert alert-warning alert-dismissible fade show" role="alert">
                                            <strong>Warning: </strong> ${requestScope.warning_msg}
                                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                              <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:when test="${requestScope.success_msg != null}">
                                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                                            <strong>Success!</strong> ${requestScope.success_msg}
                                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                              <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                    </c:when>
                                </c:choose>
                                
                            </div>
                            <div class="col-1"></div>
                        </div>
                        
                        <form class="login100-form validate-form" action="Login" method="POST">
                            <span class="login100-form-title p-b-24">
                                Login
                            </span>
                            <div class="wrap-input100 rs1 validate-input text-left" data-validate="Username is required">
                                <input class="input100" type="text" name="username" />
                                <span class="label-input100">Username</span>
                            </div>
                            <div class="wrap-input100 rs2 validate-input text-left" data-validate="Password is required">
                                <input class="input100" type="password" name="password" />
                                <span class="label-input100">Password</span>
                            </div>
                            <div class="container-login100-form-btn">
                                <input type="submit" value="Sign in" class="login100-form-btn btn-danger text-white" />
                            </div>
                            <div class="text-center w-full p-t-23">
                                <a href="#" class="txt1" data-toggle="modal" data-target="#recoverProfileModal">Forgot password?</a>
                            </div>
                        </form>
                        <div class="text-center w-full p-t-32">
                            <b> Are you not registered yet? Go on, 
                                <a href="#" class="text-danger fs-16" data-toggle="modal" data-target="#registerModal">do it now!</a>
                            </b>
                        </div>
                    </div>
                </div>


            </main>

            <footer class="mastfoot mt-auto">
                <div class="inner">
                <p class="txt1">LyricsTrivia game produced by <a href="mailto:devis.dalmoro@studenti.unitn.it">Devis Dal Moro</a></p>
                </div>
            </footer>
        </div>


        <!--MODALS for this page-->
        <div ng-app="registration_recovery" ng-controller="regRecCtrl">
            <%@include file="templates/jsp/recoverpwd_modal.jsp" %>

            <%@include file="templates/jsp/registration_modal.jsp" %>
        </div>


        <!--Lyrics Trivia scripts-->
        <script src="js/landing_main.js"></script>
    </body>
    
</html>


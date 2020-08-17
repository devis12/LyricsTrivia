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
        
        <!--reCAPTCHA google v2-->
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>
        
        <!--Registration ctrl-->
        <script src="js/registration.js"></script>
        <!--Pwd recovery ctrl-->
        <script src="js/recovery.js"></script>
        
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
        
        <!--Recover pwd modal-->
        <div ng-app="recovery" ng-controller="recoveryCtrl" class="modal fade" id="recoverProfileModal" tabindex="-1" aria-labelledby="modal for recovering profile credentials" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <form action="PwdRecovery" method="POST">
                        <div class="modal-header">
                            <h5 class="modal-title text-body">Reset your password</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="text-body fs-16 text-left">Type in your email
                                <input ng-model="emailRecovery" ng-change="checkEmail()" type="email" 
                                       class="ml-2 border border-info rounded-bottom border-top-0 border-left-0 border-right-0" 
                                       name="emailRecovery" value="" placeholder="a@a.com" required />
                                <label for="emailRecovery" class="label"></label>
                            </p>
                            <input type="text" class="text-danger fs-10" value="{{emailWrong}}" readonly/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <input id="submitRecovery" type="submit" class="btn btn-info" value="Send email" disabled/>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!--Registration modal-->
        <div ng-app="registration" ng-controller="registrationCtrl"
             class="modal fade bg-info text-center" data-backdrop="static" data-keyboard="false" id="registerModal" 
             tabindex="-1" aria-labelledby="modal for recovering profile credentials" aria-hidden="true">
            
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <form method="POST" action="Registration">
                    <div class="modal-content">
                        <div class="modal-body text-center">
                            <i class="fas fa-user text-dark"></i>
                            <h5 class="modal-title text-body">Registration</h5>
                            <hr />
                            
                            <!--Username and email-->
                            <div class="row text-body fs-18 text-center">

                                <div class="col-6" ng-init="usernameWrong=''">
                                    <label for="username" class="label">Insert your username</label>
                                    <input type="text" class="w-75 border border-info rounded-bottom border-top-0 border-left-0 border-right-0" 
                                           ng-model="username" ng-change="checkUsername()" 
                                           name="username" value="" 
                                           placeholder="username" required />
                                    <span id="iiUsername"></span>
                                    <br /><input type="text" readonly class="text-danger fs-10" value="{{usernameWrong}}"/>
                                </div>

                                <div class="col-6" ng-init="emailWrong=''">
                                    <label for="email" class="label">Type your email</label>
                                    <input type="email" class="w-75 border border-info rounded-bottom border-top-0 border-left-0 border-right-0" 
                                           ng-model="email" ng-change="checkEmail()" 
                                           name="email" value="" placeholder="a@a.com" required />
                                    <span id="iiEmail"></span>
                                    <br /><input type="text" readonly class="text-danger fs-10" value="{{emailWrong}}"/>
                                </div>

                            </div>

                            <!--Pwds-->
                            <div class="row text-body fs-18 text-center mt-5">
                                <div class="col-2"></div>
                                <div class="col-8">
                                    <label for="password1" class="label float-left mr-5">Type your password</label>
                                    <input type="password" class="w-50 float-none border border-info rounded-bottom border-top-0 border-left-0 border-right-0" 
                                           ng-model="password1" name="password1" value="" placeholder="password" ng-change="checkPwds()" required />
                                    <span id="iiPassword1"></span>
                                    <label for="password2" class="label float-left mr-4">Confirm your password</label>
                                    <input type="password" class="w-50 float-none border border-info rounded-bottom border-top-0 border-left-0 border-right-0" 
                                           ng-model="password2" name="password2" value="" placeholder="password" ng-change="checkPwds()" required />
                                    <span id="iiPassword2"></span>
                                </div>
                                <div class="col-2"></div>
                            </div>
                            <input ng-init="pwdsWrong=''" type="text" readonly class="text-danger fs-10 w-100 m-auto text-center" value="{{pwdsWrong}}"/>

                            <div class="row text-body fs-18 mt-5">
                                <div class="col-6">
                                    <label for="gender" class="label">Gender<span class="fs-12 ml-1 text-secondary">(optional)</span></label>
                                    <select class="w-75 border border-info rounded custom-select" name="gender">
                                        <option value="M">Male</option>
                                        <option value="F">Female</option>
                                        <option value="O">Other</option>
                                    </select>
                                </div>

                                <div class="col-6">
                                    <label for="email" class="label">Birth date<span class="fs-12 ml-1 text-secondary">(optional)</span></label>
                                    <input type="date" class="w-75 border border-info rounded" name="birthdate" />
                                </div>
                            </div>

                            <!--reCAPTCHA-->
                            <div class="row text-body fs-18 mt-5">
                                <div class="col-4"></div>
                                <div class="col-4"><div class="g-recaptcha" data-sitekey="6Lfal78ZAAAAANUK80a_FSdn1jewQlwhWc9KCvEE" data-callback="verifyCaptcha"></div></div>
                                <input id="recaptcha-response" type="hidden" value="-1"/>
                                <div class="col-4"></div>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-dark" data-dismiss="modal">Cancel</button>
                            <input id="submitRegister" type="submit" class="btn btn-danger text-bold" value="Register" disabled/>
                        </div>
                    </div>
                </form>
            </div>
        </div>



        <!--Lyrics Trivia scripts-->
        <script src="js/landing_main.js"></script>
    </body>
    
</html>


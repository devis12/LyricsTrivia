<%-- 
    Document   : registration_modal
    Created on : 22-Aug-2020, 11:55:53
    Author     : devis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--Registration modal-->
<div 
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
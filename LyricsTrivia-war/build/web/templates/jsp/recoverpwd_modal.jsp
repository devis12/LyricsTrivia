<%-- 
    Document   : recoverpwd_modal
    Created on : 22-Aug-2020, 11:56:25
    Author     : devis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--Recover pwd modal-->
<div class="modal fade" id="recoverProfileModal" tabindex="-1" aria-labelledby="modal for recovering profile credentials" aria-hidden="true">
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
                        <input ng-model="emailRecovery" ng-change="checkEmailRec()" type="email" 
                               class="ml-2 border border-info rounded-bottom border-top-0 border-left-0 border-right-0" 
                               name="emailRecovery" value="" placeholder="a@a.com" required />
                        <label for="emailRecovery" class="label"></label>
                    </p>
                    <input type="text" class="text-danger fs-10" value="{{emailRecWrong}}" readonly/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <input id="submitRecovery" type="submit" class="btn btn-info" value="Send email" ng-disabled="!emailRecOK"/>
                </div>
            </form>
        </div>
    </div>
</div>
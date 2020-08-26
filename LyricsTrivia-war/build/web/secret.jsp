<%-- 
    Document   : secret
    Created on : 19-Aug-2020, 21:10:38
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
        
        <!--js libraries for admin page-->
        <%@include file="templates/jsp/secret_header.jsp" %>
        
    </head>
        
    <body class="text-center"  ng-app="secret" ng-controller="secretCtrl">
        
        <div class="d-flex w-100 h-100 p-3 mx-auto flex-column">
            <header class="masthead mb-auto">
                <div class="inner">
                  <h3 class="masthead-brand"><i class="fas fa-music mr-4 chgColor2"></i>LyricsTrivia<i class="ml-4 fas fa-music chgColor1"></i></h3>
                  <nav class="nav nav-masthead justify-content-center">
                    <a class="nav-link aNav" href="Logout">Logout</a>
                  </nav>
                </div>
            </header>
            
            <h2>Admin panel</h2>
            <hr class="mb-2"/>
            
            <div class="container-fluid w-75 mt-3">
                <div class="row">

                    <div class="col">
                        <h5 class="mb-2">PRIVATE DUMP</h5>
                        <table id="songsLyricsDB" class="table table-light text-monospace fixed_header">
                            <thead class="thead-dark">
                              <tr>
                                <th scope="col">TrackID</th>
                                <th scope="col">Name</th>
                                <th scope="col">Artist</th>
                                <th scope="col"></th>
                                <th scope="col"></th>
                              </tr>
                            </thead>
                            <tbody class="d-block">
                                <tr class="" ng-repeat="sl in sslDB">
                                    <td class="">{{sl.trackID}}</td>
                                    <td class="">{{sl.trackName}}</td>
                                    <td class="">{{sl.trackArtist}}</td>
                                    <td class=""><button data-ng-click="showLyrics(sl.trackID, sl.trackName, true, false)" class="btn btn-outline-dark">Lyrics</button></td>
                                    <td class=""><button data-ng-click="delSongLyricsDB(sl)" class="btn btn-outline-danger"><i class="fas fa-trash"></i></button></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="col">
                        <h5 class="mb-2">JMUSIXMATCH</h5>
                        <table id="songsLyricsJMM" class="table table-light text-monospace fixed_header">
                            <thead class="thead-dark">
                              <tr>
                                <th scope="col">TrackID</th>
                                <th scope="col">Name</th>
                                <th scope="col">Artist</th>
                                <th scope="col"></th>
                                <th scope="col"></th>
                              </tr>
                            </thead>
                            <tbody class="d-block">
                                <tr class="" ng-repeat="sl in sslJMM">
                                    <td class="">{{sl.trackID}}</td>
                                    <td class="">{{sl.trackName}}</td>
                                    <td class="">{{sl.trackArtist}}</td>
                                    <td class=""><button data-ng-click="showLyrics(sl.trackID, sl.trackName, false, true)" class="btn btn-outline-dark">Lyrics</button></td>
                                    <td class=""><button data-ng-click="addSongLyricsDB(sl)" class="btn btn-outline-success"><i class="fas fa-database"></i></button></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <input id="cliAdminTxt" ng-model="cliAdminTxt" type="text" ng-class="cliAdminTxtColor" class="mt-2 mb-2 w-75 mr-auto ml-auto fs-16 text-monospace bg-light" readonly/>

            <div class="jumbotron bg-dark w-75 m-auto">
                <h5>Search your songs</h5>
                <div class="row">
                    <div class="col-6 col-6-12">
                        <label for="trackName" class="label">Insert name of the song</label>
                        <input id="trackName" ng-model="trackName" class="w-75 m-auto fs-16 pb-2 pt-2" type="text" name="trackName" value="" placeholder="song name" />
                    </div>

                    <div class="col-6 col-6-12"> 
                        <label for="trackArtist" class="label">Insert name of the artist</label>
                        <input id="trackArtist" ng-model="trackArtist" class="w-75 m-auto fs-16 pb-2 pt-2" type="text" name="trackArtist" value="" placeholder="artist name" />
                    </div>
                </div>
                <div class="mt-3">
                    <button id="searchBtn" type="button" class="btn btn-info text-bold w-50 m-auto" ng-click="searchSongs()" ng-disabled="searchBtnDisabled">
                        <span id="loadFa" ng-show="loadFaShow" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                        <span id="searchTxt"  ng-show="searchTxtShow" class="text-bold">SEARCH</span>
                    </button>
                </div>
            </div>

            <footer class="mastfoot mt-auto">
                <div class="inner">
                <p class="txt1">LyricsTrivia game produced by <a href="mailto:devis.dalmoro@studenti.unitn.it">Devis Dal Moro</a></p>
                </div>
            </footer>
        </div>
        
        <!-- Modal -->
        <div class="modal fade" id="modalLyrics" data-backdrop="static" data-keyboard="false" tabindex="-1" aria-labelledby="Lyrics Modal" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
              <div class="modal-header text-dark">
                <h5 class="modal-title" id="staticBackdropLabel">{{modalLyricsTitle}}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body text-dark">
                {{modalLyricsTxt}}
              </div>
            </div>
          </div>
        </div>
        
        <!--Lyrics Trivia utils scripts for resizing-->
        <script src="js/ui_utils.js"></script>
    </body>
    
</html>


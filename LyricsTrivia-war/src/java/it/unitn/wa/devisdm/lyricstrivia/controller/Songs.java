package it.unitn.wa.devisdm.lyricstrivia.controller;

import com.google.gson.Gson;
import it.unitn.wa.devisdm.lyricstrivia.dao.SongLyricsDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import it.unitn.wa.devisdm.lyricstrivia.util.RequestsUtilities;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * REST APIs for song lyrics
 * POST, PUT and DELETE can be performed only by admin
 * @author devis
 */
public class Songs extends HttpServlet {

    /*JSON object to offer as a response in GET queries*/
    private class SongLyricsJSONResponse implements Serializable{
        List<SongLyrics> lyricstrivia;
        List<SongLyrics> jmusixmatch;
        SongLyricsJSONResponse(List<SongLyrics> lyricstrivia, List<SongLyrics> jmusixmatch){
            this.lyricstrivia = lyricstrivia;
            this.jmusixmatch = jmusixmatch;
        }
        SongLyricsJSONResponse(){};
    }
    
    private SongLyricsDAORemote songLyricsDAORemote;
    
    @Override
    public void init(ServletConfig config) throws ServletException { 
        super.init(config);
        
        initEJB();
    }
    
    @Override
    public void destroy(){
        super.destroy();
        songLyricsDAORemote = null;
    }
    
    private void initEJB(){
    
        try {
            InitialContext ctx = new InitialContext();
            songLyricsDAORemote = 
                    (SongLyricsDAORemote) ctx.lookup("java:global/LyricsTrivia/LyricsTrivia-ejb/SongLyricsDAO!it.unitn.wa.devisdm.lyricstrivia.dao.SongLyricsDAORemote");
            
        } catch (NamingException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*  Utility function to decide what are the ejbean calls that has to be made to fetch the proper song havin a trackID from the requested storages
    */
    private SongLyricsJSONResponse fetchSong(int trackID, boolean DB, boolean JMM) {
        if(songLyricsDAORemote == null)
            initEJB();
        
        List<SongLyrics> sslDB = new ArrayList<>();
        List<SongLyrics> sslJMM = new ArrayList<>();
        
        if(DB) //fetch from local storage
            sslDB.add(songLyricsDAORemote.getSongLyricsDB(trackID));
        if(JMM) //fetch from jmusixmatch api
            sslJMM.add(songLyricsDAORemote.getSongLyricsJMM(trackID));
        
        /*Cleanup null singleton*/
        if(sslDB.size() > 0 && sslDB.get(0) == null)
            sslDB.clear();
        
        if(sslJMM.size() > 0 && sslJMM.get(0) == null)
            sslJMM.clear();
        
        return new SongLyricsJSONResponse(sslDB, sslJMM);
    }
    
    /*  Utility function to decide what are the ejbean calls that has to be made to fetch the proper songs from the requested storages
    */
    private SongLyricsJSONResponse fetchSongs(String trackArtist, String trackName, boolean DB, boolean JMM) {
        if(songLyricsDAORemote == null)
            initEJB();
        
        List<SongLyrics> sslDB = new ArrayList<>();
        List<SongLyrics> sslJMM = new ArrayList<>();
        
        if(trackName != null && trackArtist != null){//track artist and name -> fetch one song
            if(DB) //fetch from local storage
                sslDB.add(songLyricsDAORemote.getSongLyricsDB(trackName, trackArtist));
            if(JMM) //fetch from jmusixmatch api
                sslJMM.add(songLyricsDAORemote.getSongLyricsJMM(trackName, trackArtist));
        
        }else if(trackName != null){//just track name
            if(DB) //fetch from local storage
                sslDB = new ArrayList<>(songLyricsDAORemote.getSongsByNameDB(trackName));
            if(JMM) //fetch from jmusixmatch api
                sslJMM = new ArrayList<>(songLyricsDAORemote.getSongsByNameJMM(trackName));
        
        }else if(trackArtist != null){
            if(DB) //fetch from local storage
                sslDB = new ArrayList<>(songLyricsDAORemote.getSongsByArtistDB(trackArtist));
            if(JMM) //fetch from jmusixmatch api
                sslJMM = new ArrayList<>(songLyricsDAORemote.getSongsByArtistJMM(trackArtist));
        }
        
        /*Cleanup null singleton*/
        if(sslDB.size() > 0 && sslDB.get(0) == null)
            sslDB.clear();
        
        if(sslJMM.size() > 0 && sslJMM.get(0) == null)
            sslJMM.clear();
        
        return new SongLyricsJSONResponse(sslDB, sslJMM);
        
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(songLyricsDAORemote == null)
            initEJB();
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
        int trackID = -1;
        String trackName  = null;
        String trackArtist = null;
        String storage = null;
        
        String sTrackID = RequestsUtilities.getPathParameter(request); //extract trackID (if present)
        try {
            trackID = Integer.parseInt(sTrackID);
        } catch (final NumberFormatException e) {trackID = -1;}
        
        HashMap<String, String> queryParameters = RequestsUtilities.getQueryParameters(request);//extract query parameters
        trackArtist = request.getParameter("trackArtist");
        if(trackArtist != null)
            trackArtist = trackArtist.replaceAll("\"", "");//remove double quotes if present
        trackName = request.getParameter("trackName");
        if(trackName != null)
            trackName = trackName.replaceAll("\"", "");
        storage = request.getParameter("storage"); // "lyricstrivia", "jmusixmatch" or null for both
        if(storage != null)
            storage = storage.replaceAll("\"", "");
        
        SongLyricsJSONResponse slJSONresponse = new SongLyricsJSONResponse();
        
        if(trackID < 0){//generic search
                
            if(storage == null)//retrieve from both
                slJSONresponse = fetchSongs(trackArtist, trackName, true, true);
            else if(storage.equals("lyricstrivia"))//just retrieve from local storage
                slJSONresponse = fetchSongs(trackArtist, trackName, true, false);
            else if(storage.equals("jmusixmatch"))//just retrieve from jmusixmatch apis 
                slJSONresponse = fetchSongs(trackArtist, trackName, false, true);
        
        }else{//fetch specific song using its jmm trackID
            
            if(storage == null)//retrieve from both
                slJSONresponse = fetchSong(trackID, true, true);
            else if(storage.equals("lyricstrivia"))//just retrieve from local storage
                slJSONresponse = fetchSong(trackID, true, false);
            else if(storage.equals("jmusixmatch"))//just retrieve from jmusixmatch apis 
                slJSONresponse = fetchSong(trackID, false, true);
        }

        
        out.print(new Gson().toJson(slJSONresponse));
        out.flush();
    }
    
    private boolean isAdmin(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return (session != null && session.getAttribute("admin") != null && ((Boolean)session.getAttribute("admin")));
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(songLyricsDAORemote == null)
            initEJB();
        
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
        if(!isAdmin(request)){//security check (just admin on POST; PUT; DELETE)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            out.flush();
            return;
        }
        
        int trackID = -1;
        String trackArtist = request.getParameter("trackArtist");
        String trackName = request.getParameter("trackName");
        String trackLyrics = request.getParameter("trackLyrics");
        
        String sTrackID = request.getParameter("trackID");
        try {
            trackID = Integer.parseInt(sTrackID);
        } catch (final NumberFormatException e) {trackID = -1;}
        
        if(trackID < 0 || trackArtist == null || trackName == null || trackLyrics == null || songLyricsDAORemote.getSongLyricsDB(trackID) != null)
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400 resource bad formatted or already present
        
        else{
            //create and persist entity in db
            SongLyrics sl = new SongLyrics(trackID, trackName, trackArtist, trackLyrics);
            songLyricsDAORemote.addSongLyricsDB(sl);
            List<SongLyrics> sslDB = new ArrayList<>();
            sslDB.add(sl);
            out.print(new Gson().toJson(new SongLyricsJSONResponse(sslDB, new ArrayList<>())));
            response.setStatus(HttpServletResponse.SC_CREATED);//201
        }
        
        out.flush();
        
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(songLyricsDAORemote == null)
            initEJB();
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
                
        if(!isAdmin(request)){//security check (just admin on POST; PUT; DELETE)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            out.flush();
            return;
        }
        
        int trackID = -1;
        String trackArtist = request.getParameter("trackArtist");
        String trackName = request.getParameter("trackName");
        String trackLyrics = request.getParameter("trackLyrics");
        
        String sTrackIDPath = RequestsUtilities.getPathParameter(request); //extract trackID (if present) in path
        String sTrackIDParam = request.getParameter("trackID"); //extract trackID (if present) in payload
        try {
            trackID = Integer.parseInt(sTrackIDPath);
            trackID = (Integer.parseInt(sTrackIDParam)==trackID)? trackID : -1; //they can't be different, otherwise 400 bad request
        } catch (final NumberFormatException e) {trackID = -1;}
        
        if(trackID < 0 || trackArtist == null || trackName == null || trackLyrics == null || songLyricsDAORemote.getSongLyricsDB(trackID) == null)
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400 resource bad formatted or not present
        
        else{
            //update persisted entity in db
            SongLyrics sl = new SongLyrics(trackID, trackName, trackArtist, trackLyrics);
            songLyricsDAORemote.editSongLyricsDB(sl);
            List<SongLyrics> sslDB = new ArrayList<>();
            sslDB.add(sl);
            out.print(new Gson().toJson(new SongLyricsJSONResponse(sslDB, new ArrayList<>())));
            response.setStatus(HttpServletResponse.SC_OK);//200
        }
        
        out.flush();
    }
    
     @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(songLyricsDAORemote == null)
            initEJB();
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
                
        if(!isAdmin(request)){//security check (just admin on POST; PUT; DELETE)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            out.flush();
            return;
        }
        
        int trackID = -1;
        
        String sTrackIDPath = RequestsUtilities.getPathParameter(request); //extract trackID (if present) in path
        try {
            trackID = Integer.parseInt(sTrackIDPath);
        } catch (final NumberFormatException e) {trackID = -1;}
        
        if(trackID < 0 || songLyricsDAORemote.getSongLyricsDB(trackID) == null)
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400 request bad formatted or not present
        
        else{
            //update persisted entity in db
            songLyricsDAORemote.deleteSongLyricsDB(trackID);
            response.setStatus(HttpServletResponse.SC_OK);//200
        }
        
        out.flush();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet which offers REST APIs to fetch song lyrics from different internal and external services; additionally saving and deleting in the internal one";
    }// </editor-fold>

}

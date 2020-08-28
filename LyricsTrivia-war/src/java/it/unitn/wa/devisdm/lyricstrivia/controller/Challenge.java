package it.unitn.wa.devisdm.lyricstrivia.controller;

import com.google.gson.Gson;
import it.unitn.wa.devisdm.lyricstrivia.dao.OnlinePlayersRemote;
import it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.dao.QuestionDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.dao.SongLyricsDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import it.unitn.wa.devisdm.lyricstrivia.entity.Question;
import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import it.unitn.wa.devisdm.lyricstrivia.entity.StoredQuestion;
import java.io.IOException;
import java.io.PrintWriter;
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
 * REST API to
 * (GET) get new Challenge Question (if there any unanswered ones in the local storage) and 
 * (PUT) answer to them
 * @author devis
 */
public class Challenge extends HttpServlet {

    private QuestionDAORemote questionDAORemote;
    private SongLyricsDAORemote songLyricsDAORemote;
    private PlayerDAORemote playerDAORemote;
    
    @Override
    public void init(ServletConfig config) throws ServletException { 
        super.init(config);
        initEJB();
        
    }
    
    @Override
    public void destroy(){
        super.destroy();
        questionDAORemote = null;
        songLyricsDAORemote = null;
        playerDAORemote = null;
    }
    
    private void initEJB(){
        try {
            InitialContext ctx = new InitialContext();
            playerDAORemote = 
                    (PlayerDAORemote) ctx.lookup("java:global/LyricsTrivia-ejb/PlayerDAO!it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote");
            questionDAORemote = 
                    (QuestionDAORemote) ctx.lookup("java:global/LyricsTrivia/LyricsTrivia-ejb/QuestionDAO!it.unitn.wa.devisdm.lyricstrivia.dao.QuestionDAORemote");
            songLyricsDAORemote = 
                        (SongLyricsDAORemote) ctx.lookup("java:global/LyricsTrivia/LyricsTrivia-ejb/SongLyricsDAO!it.unitn.wa.devisdm.lyricstrivia.dao.SongLyricsDAORemote");
            
        } catch (NamingException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     * Get unanswered challenge question
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(questionDAORemote == null)
            initEJB();
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("player") == null){//this should never happen in any case (assuming proper filters)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            out.flush();
            return;
        }
        
        //extract player info from active session
        Player player = (Player) session.getAttribute("player");
        
        //see if you've already fetched some unanswered question from the db and proposed them to the player
        List<Question> challengeQuestions = (session.getAttribute("challengeQuestions") != null)? (List<Question>) session.getAttribute("challengeQuestions") : null;
        
        Question cq = null;//challenge question to propose
        
        if(challengeQuestions == null || challengeQuestions.isEmpty()){//no challenge questions in the session obj, try to fetch them from db 
            challengeQuestions = questionDAORemote.getUnansweredQuestion(player.getUsername());
            session.setAttribute("challengeQuestions", challengeQuestions);//save them in session obj, because if they are many you don't need to fetch from db again later
        
        }// else there is still unanswered questions in the session list... give the first one of them
        
        if(challengeQuestions != null && challengeQuestions.size() > 0){//there is a challenge question to propose
            cq = challengeQuestions.get(0);//answer back with the first one in the array 
            cq.setRightAnswerIndex(-1);//hidden answer index to user (so it can't check json)

            for(SongLyrics sl : cq.getOptions()){sl.setTrackLyrics("");}//hidden lyrics of the four options, so user can't cheat
            session.setAttribute("cq", cq);
        }
        
        out.print(new Gson().toJson(cq));
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
        if(questionDAORemote == null || playerDAORemote == null || songLyricsDAORemote == null)
            initEJB();
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("player") == null){//this should never happen in any case (assuming proper filters)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            out.flush();
            return;
        }
        
        //extract player info from active session
        Player player = (Player) session.getAttribute("player");
        
        try{
            //extract and check supplied parameters
            String askingPlayer = request.getParameter("askingPlayer");
            String askedPlayer = request.getParameter("askedPlayer");
            
            int trackID1 = Integer.parseInt(request.getParameter("trackID1"));
            int trackID2 = Integer.parseInt(request.getParameter("trackID2"));
            int trackID3 = Integer.parseInt(request.getParameter("trackID3"));
            int trackID4 = Integer.parseInt(request.getParameter("trackID4"));
            int rightTrackID = Integer.parseInt(request.getParameter("rightTrackID"));
            int givenTrackID = -1;//question has just been made and launched to the challenged player
            
            boolean badRequest = false;
            badRequest = badRequest || (!player.getUsername().equals(askingPlayer));//askingPlayer is not the one launching the challenge -> BAD REQUEST
            badRequest = badRequest || (playerDAORemote.getPlayer(askedPlayer) == null);//invalid username of the challenged player
            
            //rightTrackID isn't any of the four supplied one
            badRequest = badRequest || (trackID1 != rightTrackID && trackID2 != rightTrackID && trackID3 != rightTrackID && trackID4 != rightTrackID);
            
            //one of the trackIDs is not valid
            badRequest = badRequest || (songLyricsDAORemote.getSongLyricsDB(trackID1) == null);
            badRequest = badRequest || (songLyricsDAORemote.getSongLyricsDB(trackID2) == null);
            badRequest = badRequest || (songLyricsDAORemote.getSongLyricsDB(trackID3) == null);
            badRequest = badRequest || (songLyricsDAORemote.getSongLyricsDB(trackID4) == null);
            
            if(badRequest)
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            else{
                //checks passed... store the new question
                StoredQuestion sq = new StoredQuestion(askingPlayer, askedPlayer, trackID1, trackID2, trackID3, trackID4, rightTrackID, givenTrackID);
                questionDAORemote.storeNewQuestion(sq);
                response.setStatus(HttpServletResponse.SC_CREATED);//201 CREATED!
            }
        }catch(NumberFormatException nfe){//errors within parsing of trackIDs
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        
        out.flush();
    }
    
    // 
    /**
     * Handles the HTTP <code>GET</code> method.
     * PUT to answer the challenge question obtain with GET
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(questionDAORemote == null)
            initEJB();
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("player") == null || 
                session.getAttribute("cq") == null || request.getParameter("givenAnswerIndex") == null){//this should never happen in any case (assuming proper filters)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            out.flush();
            return;
        }
        
        Question cq = (Question) session.getAttribute("cq");
        
        // check compatibility of given question <-> answered one by id of the corresp. stored question
        int receivedStoredId = -1;
        try{
            receivedStoredId = Integer.parseInt(request.getParameter("storedId"));
        }catch(NumberFormatException nfe){
            receivedStoredId = -1;
        }
        if(receivedStoredId != cq.getStoredId()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
            out.flush();
            return;
        }
        
        StoredQuestion sq = questionDAORemote.getStoredQuestion(cq.getStoredId());
        
        /*compute rightAnswerIndex to deliver to the client as an index in [0, 3]*/
        int rightTrackID = sq.getRightTrackID();
        int trackID1 = sq.getTrackID1();
        int trackID2 = sq.getTrackID2();
        int trackID3 = sq.getTrackID3();
        int trackID4 = sq.getTrackID4();
        int cqRightIndex = -1;
        cqRightIndex = (rightTrackID == trackID1)? 0 : cqRightIndex;
        cqRightIndex = (rightTrackID == trackID2)? 1 : cqRightIndex;
        cqRightIndex = (rightTrackID == trackID3)? 2 : cqRightIndex;
        cqRightIndex = (rightTrackID == trackID4)? 3 : cqRightIndex;
        
        int givenAnswerIndex = Integer.parseInt(request.getParameter("givenAnswerIndex"));
        cq.setGivenAnswerIndex(givenAnswerIndex);
        cq.setRightAnswerIndex(cqRightIndex);//put again the right answer index in the challenge question, so the player will know if he/she answered right
        
        //update player stats
        Player player = (Player) session.getAttribute("player");
        int gamesPlayed = player.getPlayed() + 1;
        int gamesWon = (cqRightIndex == givenAnswerIndex)? player.getWon() + 1 : player.getWon();
        player.setPlayed(gamesPlayed);
        player.setWon(gamesWon);
        playerDAORemote.editPlayer(player);
        //edit also its entry in the onlineMap of players
        HashMap<Player, Boolean> onlinePlayers = ((OnlinePlayersRemote) this.getServletContext().getAttribute("onlinePlayersRemote")).getPlayersMap();
        ((OnlinePlayersRemote) this.getServletContext().getAttribute("onlinePlayersRemote")).setMatches(Player.erasePrivateInfo(player, true));
        onlinePlayers = ((OnlinePlayersRemote) this.getServletContext().getAttribute("onlinePlayersRemote")).getPlayersMap();
        
        //update stored question in the db (with the given answer)
        sq.setGivenTrackID(cq.getOptions().get(givenAnswerIndex).getTrackID());
        questionDAORemote.editStoredQuestion(sq);
        
        //remove session attributes
        session.removeAttribute("cq");
        
        //update session array containing unanswered challenge questions
        List<Question> challengeQuestions = (List<Question>) session.getAttribute("challengeQuestions");
        challengeQuestions.remove(cq);
        session.setAttribute("challengeQuestions", challengeQuestions);
        //IMPORTANT NOTE.... for now maintain the API very simple and assume sequence of GET -> PUT -> GET -> PUT....
        
        response.setStatus(HttpServletResponse.SC_OK);
        out.print(new Gson().toJson(cq));
        out.flush();
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for simple request - reply challenge questions";
    }// </editor-fold>

}

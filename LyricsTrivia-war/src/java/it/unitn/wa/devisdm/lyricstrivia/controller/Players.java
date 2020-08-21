/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.controller;

import it.unitn.wa.devisdm.lyricstrivia.util.RequestsUtilities;
import com.google.gson.Gson;
import it.unitn.wa.devisdm.lyricstrivia.dao.OnlinePlayersRemote;
import it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entities.PlayerOnlineStatus;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import it.unitn.wa.devisdm.lyricstrivia.util.TokenGenerator;
import it.unitn.wa.devisdm.lyricstrivia.util.UtilityCheck;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * RESTFUL operations on Players 
 * @author devis
 */
public class Players extends HttpServlet {
    
    private PlayerDAORemote playerDAORemote;
    
    @Override
    public void init(ServletConfig config) throws ServletException { 
        super.init(config);
        
        initEJB();
    }
    
    @Override
    public void destroy(){
        super.destroy();
        playerDAORemote = null;
    }
    
    private void initEJB(){
    
        try {
            InitialContext ctx = new InitialContext();
            playerDAORemote = 
                    (PlayerDAORemote) ctx.lookup("java:global/LyricsTrivia-ejb/PlayerDAO!it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote");
            
        } catch (NamingException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(playerDAORemote == null)
            initEJB();
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
        String specificUsername = RequestsUtilities.getPathParameter(request); //extract username
        HashMap<String, String> queryParameters = RequestsUtilities.getQueryParameters(request);//extract query parameters
        
        if(specificUsername != null && specificUsername.length() > 0){//user wants info about a specific player
            Player p = playerDAORemote.getPlayer(specificUsername);
            out.print(new Gson().toJson(Player.erasePrivateInfo(p, true)));//cleanup all pwd/salt/email data (even if they are cripted) before sending
        
        }else if(queryParameters.containsKey("email") && UtilityCheck.isValidEmail(queryParameters.get("email"))){//return player having a specific email
            Player p = playerDAORemote.getPlayerByEmail(queryParameters.get("email"));
            out.print(new Gson().toJson(Player.erasePrivateInfo(p, true)));
            
        }else if (queryParameters.containsKey("online_status")){//return list of all players + online status by checking that info in
            HashMap<Player, Boolean> onlinePlayers = ((OnlinePlayersRemote) this.getServletContext().getAttribute("onlinePlayersRemote")).getPlayersMap();
            List<PlayerOnlineStatus> onlinePlayersList = new ArrayList<>();
            for(Player p : onlinePlayers.keySet())
                onlinePlayersList.add(new PlayerOnlineStatus(p, onlinePlayers.get(p), false));//TODO this need to be fixed, because the challenge status changes as well

            out.print(new Gson().toJson(onlinePlayersList));
        
        }else{//just return list of all players fetched from the db in a JSON Array
            List<Player> playersPrivate = playerDAORemote.getAllPlayers();
            List<Player> players = new ArrayList<>();
            for(Player p : playersPrivate){
                players.add(Player.erasePrivateInfo(p, true));//cleanup all pwd/salt/email data (even if they are cripted)
            }
            out.print(new Gson().toJson(players));
        }
        
        out.flush();
        
    }
    
    /*  Check if PUT, DELETE operations are authorized */
    private boolean isAuthorized(HttpServletRequest request){
        String specificUsername = RequestsUtilities.getPathParameter(request); //extract username
        
        String username = request.getParameter("username");
        
        if(!username.equals(specificUsername))//username in the path do not match username passed in the obj
            return false;
        
        
        HttpSession session = request.getSession(false);
        if(session==null)//session not present
            return false;
        
        Player player = (Player)session.getAttribute("player");
        
        return player!=null && player.getUsername().equals(request.getParameter("username"));//just logged player can change just its value
    }
    
    /*Only operation supported in PUT (for now) is password update*/
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String[]> parameters = request.getParameterMap();//TODO delete when development is ended
        String specificUsername = RequestsUtilities.getPathParameter(request); //extract username
        String newPwd = request.getParameter("pwd");
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
        try{
            if(!isAuthorized(request)){
                //unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }else if(parameters.containsKey("updPwd") 
                    && UtilityCheck.isStrong(newPwd) ){

                Player updP = (Player) request.getSession(false).getAttribute("player");
                updP.setPwd(TokenGenerator.getEncryptedPassword(newPwd, updP.getSalt()));
                playerDAORemote.editPlayer(updP);

                out.print(new Gson().toJson(updP));
            
            }else{
                //bad request
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            
        }catch(NoSuchAlgorithmException | InvalidKeySpecException nse){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        
        }finally{
            out.flush();
        }

    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String[]> parameters = request.getParameterMap();//TODO delete when development is ended
        String delPwd = request.getParameter("pwd");
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
        try{
            if(!isAuthorized(request)){
                //unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }else {
                Player player = (Player) request.getSession(false).getAttribute("player");
                if(!Arrays.equals(player.getPwd(), TokenGenerator.getEncryptedPassword(delPwd, player.getSalt())))//still unauthorized
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                else{
                    Player delP = playerDAORemote.deletePlayer(player.getUsername());
                    out.print(new Gson().toJson(delP));
                    //delete player from the map and destroy session
                    ((OnlinePlayersRemote) this.getServletContext().getAttribute("onlinePlayersRemote")).deletePlayer(Player.erasePrivateInfo(player, true));
                    request.getSession(false).invalidate();
                }
            }
            
        }catch(NoSuchAlgorithmException | InvalidKeySpecException nse){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        
        }finally{
            out.flush();
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "CRUD operations' end-points on Player entity";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.controller;

import it.unitn.wa.devisdm.lyricstrivia.util.RequestsUtilities;
import com.google.gson.Gson;
import it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import it.unitn.wa.devisdm.lyricstrivia.util.UtilityCheck;
import java.io.IOException;
import java.io.PrintWriter;
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
            if(p != null){    
                p.setSalt("".getBytes());p.setPwd("".getBytes());p.setEmail("");//cleanup all pwd/salt/email data (even if they are cripted)
            }
            out.print(new Gson().toJson(p));
        
        }else if(queryParameters.containsKey("email") && UtilityCheck.isValidEmail(queryParameters.get("email"))){//return player having a specific email
            Player p = playerDAORemote.getPlayerByEmail(queryParameters.get("email"));
            if(p != null){  
                p.setSalt("".getBytes());p.setPwd("".getBytes());//cleanup all pwd/salt data (even if they are cripted)
            }
            out.print(new Gson().toJson(p));
            
        }else{//return list of all players in a JSON Array
            List<Player> players = playerDAORemote.getAllPlayers();
            for(Player p : players){p.setSalt("".getBytes());p.setPwd("".getBytes());p.setEmail("");}//cleanup all pwd/salt/email data (even if they are cripted)
            out.print(new Gson().toJson(players));
        }
        
        out.flush();
        
    }
    
    /*
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Map<String, String[]> parameters = request.getParameterMap();//TODO delete when development is ended
        
        String specificUsername = RequestsUtilities.getPathParameter(request); //extract username
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String pwd = request.getParameter("pwd");
        Date birthdate = new Date(Long.parseLong(request.getParameter("birthdate")));
        char gender = (request.getParameter("gender")).charAt(0);
        int played = Integer.parseInt(request.getParameter("played"));
        int won = Integer.parseInt(request.getParameter("won"));
        
        if(!username.equals(specificUsername)){
            //bad request
            return;
        }
        
        Player updP = new Player(username, email, pwd.getBytes(), "salt".getBytes(), birthdate, gender, played, won, false);
        playerDAORemote.editPlayer(updP);
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(updP));
        out.flush();
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String[]> parameters = request.getParameterMap();//TODO delete when development is ended
        
        String specificUsername = RequestsUtilities.getPathParameter(request); //extract username
        
        String username = request.getParameter("username");
        
        if(!username.equals(specificUsername)){
            //bad request
            return;
        }
        
        Player delP = playerDAORemote.deletePlayer(username);
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(delP));
        out.flush();
    }
    */

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

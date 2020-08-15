/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.controller;

import com.google.gson.Gson;
import it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.io.IOException;
import java.io.PrintWriter;
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
 *
 * @author devis
 */
public class Players extends HttpServlet {

    private PlayerDAORemote playerDAORemote;
    private Gson gson;
    
    @Override
    public void init(ServletConfig config) throws ServletException { 
        super.init(config);
        
        gson = new Gson();
        
        try {
            InitialContext ctx = new InitialContext();
            playerDAORemote = 
                    (PlayerDAORemote) ctx.lookup("java:global/LyricsTrivia-ejb/PlayerDAO!it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote");
            
        } catch (NamingException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void destroy(){
        super.destroy();
        playerDAORemote = null;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String specificUsername = Utilities.getPathParameter(request); //extract username
        HashMap<String, String> queryParameters = Utilities.getQueryParameters(request);//extract query parameters
        
        if(specificUsername != null && specificUsername.length() > 0){//user wants info about a specific player
            Player p = playerDAORemote.getPlayer(specificUsername);
            request.setAttribute("Player", p);
            response.setContentType("application/json"); //TODO move it above
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(p));
            out.flush();
        
        }else{//just print out table with all the users //TODO fix it with JSON Array
            List<Player> players = playerDAORemote.getAllPlayers();
            request.setAttribute("players", players);
            request.getRequestDispatcher("players.jsp").forward(request, response);
        }
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String pwd = request.getParameter("pwd");
        int age = Integer.parseInt(request.getParameter("age"));
        char genre = (request.getParameter("genre")).charAt(0);
        int played = Integer.parseInt(request.getParameter("played"));
        int won = Integer.parseInt(request.getParameter("won"));
        
        Player newP = new Player(username, email, pwd, "salt", age, genre, played, won);
        playerDAORemote.addPlayer(newP);
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(newP));
        out.flush();
        
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String[]> parameters = request.getParameterMap();//TODO delete when development is ended
        
        String specificUsername = Utilities.getPathParameter(request); //extract username
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String pwd = request.getParameter("pwd");
        int age = Integer.parseInt(request.getParameter("age"));
        char genre = (request.getParameter("genre")).charAt(0);
        int played = Integer.parseInt(request.getParameter("played"));
        int won = Integer.parseInt(request.getParameter("won"));
        
        if(!username.equals(specificUsername)){
            //bad request
            return;
        }
        
        Player updP = new Player(username, email, pwd, "salt", age, genre, played, won);
        playerDAORemote.editPlayer(updP);
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(updP));
        out.flush();
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String[]> parameters = request.getParameterMap();//TODO delete when development is ended
        
        String specificUsername = Utilities.getPathParameter(request); //extract username
        
        String username = request.getParameter("username");
        
        if(!username.equals(specificUsername)){
            //bad request
            return;
        }
        
        Player delP = playerDAORemote.deletePlayer(username);
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(delP));
        out.flush();
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

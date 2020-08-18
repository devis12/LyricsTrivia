/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.listener;

import it.unitn.wa.devisdm.lyricstrivia.dao.OnlinePlayersRemote;
import it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author devis
 */
public class WebAppContextListener implements ServletContextListener {
    
    
    private OnlinePlayersRemote onlinePlayersRemote;
    private PlayerDAORemote playerDAORemote;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //set attribute containing hashmap of all player marking online/offline status (in a stateful bean saved in the webapp context)
        
        initEJB();//init onlinePlayersRemote
        
        if(playerDAORemote!= null && onlinePlayersRemote != null){
            sce.getServletContext().setAttribute("onlinePlayersRemote", onlinePlayersRemote);
            for(Player p : playerDAORemote.getAllPlayers()){
                p = Player.erasePrivateInfo(p, true);//cleanup secret info... email too
                onlinePlayersRemote.addPlayer(p);//add player in the map
            }
        }
    }
    
    private void initEJB(){
        try {
            InitialContext ctx = new InitialContext();
            playerDAORemote = 
                    (PlayerDAORemote) ctx.lookup("java:global/LyricsTrivia-ejb/PlayerDAO!it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote");
            onlinePlayersRemote = 
                    (OnlinePlayersRemote) ctx.lookup("java:global/LyricsTrivia-ejb/OnlinePlayers!it.unitn.wa.devisdm.lyricstrivia.dao.OnlinePlayersRemote");
            
        } catch (NamingException ex) {
            Logger.getLogger(WebAppContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}

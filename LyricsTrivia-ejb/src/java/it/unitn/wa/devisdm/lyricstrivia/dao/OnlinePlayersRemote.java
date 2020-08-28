package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.util.HashMap;
import javax.ejb.Remote;

/**
 * Hashmap maintaining stats and online status of registered Players
 * @author devis
 */
@Remote
public interface OnlinePlayersRemote {
    
    /*Add new player to the hashmap*/
    void addPlayer(Player p);
    /*Remove player from the hashmap*/
    void deletePlayer(Player p);
    
    /*Set player in the hashmap as online*/
    void setOnline(Player p);
    /*Update player's matches info*/
    void setMatches(Player p);
    /*Set player in the hashmap as offline*/
    void setOffline(Player p);
    
    HashMap<Player, Boolean> getPlayersMap();//map with players and their online/offline status
}

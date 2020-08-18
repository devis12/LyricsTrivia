/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.util.HashMap;
import javax.ejb.Remote;

/**
 *
 * @author devis
 */
@Remote
public interface OnlinePlayersRemote {
    
    void addPlayer(Player p);
    void deletePlayer(Player p);
    
    void setOnline(Player p);
    void setOffline(Player p);
    
    HashMap<Player, Boolean> getPlayersMap();//map with players and their online/offline status
}

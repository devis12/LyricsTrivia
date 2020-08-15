/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author devis
 */
@Remote
public interface PlayerDAORemote {
    
    void addPlayer(Player player);

    void editPlayer(Player player);

    Player deletePlayer(String username);
    
    Player getPlayer(String username);

    List<Player> getAllPlayers();
}

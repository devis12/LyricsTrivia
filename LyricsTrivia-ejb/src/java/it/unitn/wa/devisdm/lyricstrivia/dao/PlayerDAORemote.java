package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.util.List;
import javax.ejb.Remote;

/**
 * CRUD operation on Player entity
 * @author devis
 */
@Remote
public interface PlayerDAORemote {
    
    void addPlayer(Player player);

    void editPlayer(Player player);

    Player deletePlayer(String username);
    
    Player getPlayer(String username);

    List<Player> getAllPlayers();

    Player getPlayerByEmail(String email);
}

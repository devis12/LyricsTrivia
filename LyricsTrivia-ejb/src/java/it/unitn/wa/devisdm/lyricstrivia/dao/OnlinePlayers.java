/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.util.HashMap;
import javax.ejb.Stateful;

/**
 *
 * @author devis
 */
@Stateful
public class OnlinePlayers implements OnlinePlayersRemote {

    public HashMap<Player, Boolean> playerMap = new HashMap<>();

    @Override
    public void addPlayer(Player p) {
        playerMap.put(p, Boolean.FALSE);
    }

    @Override
    public void deletePlayer(Player p) {
        playerMap.remove(p);
    }

    @Override
    public void setOnline(Player p) {
        playerMap.put(p, Boolean.TRUE);
    }

    @Override
    public void setOffline(Player p) {
        playerMap.put(p, Boolean.FALSE);
    }

    @Override
    public HashMap<Player, Boolean> getPlayersMap() {
        return playerMap;
    }
}

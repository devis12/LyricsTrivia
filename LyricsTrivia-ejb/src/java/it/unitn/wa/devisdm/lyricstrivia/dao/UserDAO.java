/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author devis
 */
@Stateful
public class UserDAO implements UserDAORemote {
    
        
    @PersistenceContext
    private EntityManager manager;
    
    @Override
    public void addPlayer(Player player) {
        manager.persist(player);
    }

    @Override
    public void editPlayer(Player player) {
        manager.merge(player);
    }

    @Override
    public void deletePlayer(String email) {
        manager.remove(getPlayer(email));
    }

    @Override
    public Player getPlayer(String email) {
        return manager.find(Player.class, email);
    }

    @Override
    public List<Player> getAllPlayers() {
        Query query = manager.createQuery("from Player", Player.class);
        return query.getResultList();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

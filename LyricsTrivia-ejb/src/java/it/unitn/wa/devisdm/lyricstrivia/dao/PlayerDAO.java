package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * CRUD operation on Player entity
 * @author devis
 */
@Stateless
public class PlayerDAO implements PlayerDAORemote {
    
        
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
    public Player deletePlayer(String username) {
        Player delP = getPlayer(username);
        manager.remove(delP);
        return delP;
    }

    @Override
    public Player getPlayer(String username) {
        return manager.find(Player.class, username);
    }

    @Override
    public List<Player> getAllPlayers() {
        Query query = manager.createQuery("from Player P", Player.class);
        return query.getResultList();
        
    }

    @Override
    public Player getPlayerByEmail(String email) {
        if(email == null)
            return null;
        Query query = manager.createQuery("from Player P where P.email = :email", Player.class);
        query.setParameter("email", email);
        if(query.getResultList().size()>0)
            return ((List<Player>)query.getResultList()).get(0);
        
        return null;
    }

}

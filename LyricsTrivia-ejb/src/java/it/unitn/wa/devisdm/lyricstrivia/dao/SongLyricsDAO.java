/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import it.unitn.wa.devisdm.lyricstrivia.module.MusixMatchLyricsLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author devis
 */
@Stateless
public class SongLyricsDAO implements SongLyricsDAORemote, SongLyricsDAOLocal {
        
    @EJB
    private MusixMatchLyricsLocal musixMatchLocal;
    
    @PersistenceContext
    private EntityManager manager;
    
    @Override
    public void addSongLyrics(SongLyrics songLyrics) {
        manager.persist(songLyrics);
    }

    @Override
    public void editSongLyrics(SongLyrics songLyrics) {
        manager.merge(songLyrics);
    }

    @Override
    public void deleteSongLyrics(int trackID) {
        manager.remove(getSongLyrics(trackID));
    }

    @Override
    public SongLyrics getSongLyrics(int trackID) {
        return manager.find(SongLyrics.class, trackID);
    }
    
    @Override
    public SongLyrics getSongLyrics(String trackName, String trackArtist) {
        //check if already present in the current db dump, otherwise call jmusixmatch apis
        SongLyrics sl = manager.createQuery("SELECT s FROM SongLyrics s WHERE s.trackName = " + trackName + " AND s.trackArtist = " + trackArtist, SongLyrics.class).getSingleResult();
        if(sl != null)
            return sl;
        
        sl = musixMatchLocal.getSongLyrics(trackName, trackArtist);
        return sl;
    }

    @Override
    public List<SongLyrics> getSongsByArtist(String trackArtist) {
        //do NOT check in the db, there can be new songs having this name if you consult jmusixmatch apis
        return  musixMatchLocal.getSongsByArtist(trackArtist);
    }

    @Override
    public List<SongLyrics> getSongsByName(String trackName) {
        //do NOT check in the db, there can be new songs having this name if you consult jmusixmatch apis
        return musixMatchLocal.getSongsByName(trackName);
    }

    @Override
    public List<SongLyrics> getRandomSongLyrics(int n) {
        //select n random songlyrics from the db
        Query query = manager.createQuery("SELECT s FROM SongLyrics s ORDER BY random()", SongLyrics.class);
        query = query.setMaxResults(n);
        return query.getResultList();
   }
    
}

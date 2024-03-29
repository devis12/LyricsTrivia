package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import it.unitn.wa.devisdm.lyricstrivia.module.MusixMatchLyricsLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * CRUD operation on SongLyrics Entity
 * Interaction with MusixMatch beans' methods to retrieve SongLyrics from external service and format them in a LyricsTrivia like fashion
 * Utilities that will come handy to automatically generate and build up questions
 * @author devis
 */
@Stateless
public class SongLyricsDAO implements SongLyricsDAORemote, SongLyricsDAOLocal {
        
    @EJB
    private MusixMatchLyricsLocal musixMatchLocal;
    
    @PersistenceContext
    private EntityManager manager;
    
    @Override
    public void addSongLyricsDB(SongLyrics songLyrics) {
        songLyrics.escapeHtml();
        manager.persist(songLyrics);
    }

    @Override
    public void editSongLyricsDB(SongLyrics songLyrics) {
        songLyrics.escapeHtml();
        manager.merge(songLyrics);
    }

    @Override
    public void deleteSongLyricsDB(int trackID) {
        manager.remove(getSongLyricsDB(trackID));
    }

    @Override
    public SongLyrics getSongLyricsDB(int trackID) {
        return manager.find(SongLyrics.class, trackID);
    }
    
    @Override
    public SongLyrics getSongLyricsJMM(int trackID) {
        return musixMatchLocal.getSongLyrics(trackID);
    }
    
    @Override
    public SongLyrics getSongLyricsDB(String trackName, String trackArtist) {
        Query query = manager.createQuery("SELECT s FROM SongLyrics s WHERE lower(s.trackName) LIKE lower(:track) AND lower(s.trackArtist) LIKE lower(:artist)", SongLyrics.class);
        query.setParameter("track", "%"+trackName+"%");
        query.setParameter("artist", "%"+trackArtist+"%");
        SongLyrics sl;
        try{ 
            sl = (SongLyrics) query.getSingleResult();
        }catch(NoResultException nre){
            sl = null;
        }
        return sl;
    }
    
    /*Get song by artist and song name using musix match bean*/    
    @Override
    public SongLyrics getSongLyricsJMM(String trackName, String trackArtist) {
        SongLyrics sl = musixMatchLocal.getSongLyrics(trackName, trackArtist);
        return sl;
    }
    
    /*Get songs by artist using musix match bean*/
    @Override
    public List<SongLyrics> getSongsByArtistJMM(String trackArtist) {
        return  musixMatchLocal.getSongsByArtist(trackArtist);
    }
    
    @Override
    public List<SongLyrics> getSongsByArtistDB(String trackArtist) {
        Query query = manager.createQuery("SELECT s FROM SongLyrics s WHERE lower(s.trackArtist) LIKE lower(:artist)", SongLyrics.class);
        query.setParameter("artist", "%"+trackArtist+"%");
        List<SongLyrics> ssl;
        try{ 
             ssl = query.getResultList();
        }catch(NoResultException nre){
            ssl = new ArrayList<>();
        }
        return ssl;
    }
    
    /*Get songs by name using musix match bean*/
    @Override
    public List<SongLyrics> getSongsByNameJMM(String trackName) {
        return musixMatchLocal.getSongsByName(trackName);
    }
    
     @Override
    public List<SongLyrics> getSongsByNameDB(String trackName) {
        Query query = manager.createQuery("SELECT s FROM SongLyrics s WHERE lower(s.trackName) LIKE lower(:name)", SongLyrics.class);
        query.setParameter("name", "%"+trackName+"%");
        List<SongLyrics> ssl;
        try{ 
             ssl = query.getResultList();
        }catch(NoResultException nre){
            ssl = new ArrayList<>();
        }
        return ssl;
    }

    @Override
    public List<SongLyrics> getRandomSongLyricsDB(int n) {
        //select n random songlyrics from the db
        Query query = manager.createQuery("SELECT s FROM SongLyrics s ORDER BY random()", SongLyrics.class);
        query = query.setMaxResults(n);
        return query.getResultList();
   }

    @Override
    public List<SongLyrics> getAllSongsDB() {
        Query query = manager.createQuery("SELECT s FROM SongLyrics s", SongLyrics.class);
        return query.getResultList();
    }
    
}

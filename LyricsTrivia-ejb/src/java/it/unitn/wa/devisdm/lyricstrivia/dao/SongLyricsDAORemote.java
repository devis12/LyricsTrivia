/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author devis
 */
@Remote
public interface SongLyricsDAORemote {
    
   void addSongLyrics(SongLyrics songLyrics);

   void editSongLyrics(SongLyrics songLyrics);

   void deleteSongLyrics(int trackID);

    SongLyrics getSongLyrics(int trackID);
    
    SongLyrics getSongLyrics(String trackName, String trackArtist);
    
    List<SongLyrics> getSongsByArtist(String trackArtist);
    
    List<SongLyrics> getSongsByName(String trackName);
    
    List<SongLyrics> getAllSongs();
}

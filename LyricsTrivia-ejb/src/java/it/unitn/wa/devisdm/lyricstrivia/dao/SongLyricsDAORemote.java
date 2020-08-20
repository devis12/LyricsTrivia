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
    
   void addSongLyricsDB(SongLyrics songLyrics);

   void editSongLyricsDB(SongLyrics songLyrics);

   void deleteSongLyricsDB(int trackID);

    SongLyrics getSongLyricsDB(int trackID);
    SongLyrics getSongLyricsJMM(int trackID);
    
    SongLyrics getSongLyricsDB(String trackName, String trackArtist);
    SongLyrics getSongLyricsJMM(String trackName, String trackArtist);
    
    List<SongLyrics> getSongsByArtistDB(String trackArtist);
    List<SongLyrics> getSongsByArtistJMM(String trackArtist);
    
    List<SongLyrics> getSongsByNameDB(String trackName);
    List<SongLyrics> getSongsByNameJMM(String trackName);
    
    List<SongLyrics> getAllSongsDB();
}

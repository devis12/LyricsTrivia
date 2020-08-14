/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.module;

import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author devis
 */
@Local
public interface MusixMatchLyricsLocal{

    SongLyrics getSongLyrics(String trackName, String trackArtist);
    
    List<SongLyrics> getSongsByArtist(String trackArtist);
    
    List<SongLyrics> getSongsByName(String trackName);
}

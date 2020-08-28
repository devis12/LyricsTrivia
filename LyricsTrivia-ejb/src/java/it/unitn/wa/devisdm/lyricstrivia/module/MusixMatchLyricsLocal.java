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

    SongLyrics getSongLyrics(int trackID);
    SongLyrics getSongLyrics(String trackName, String trackArtist);
    
    List<SongLyrics> getSongsByArtist(String trackArtist);
    
    List<SongLyrics> getSongsByName(String trackName);
}

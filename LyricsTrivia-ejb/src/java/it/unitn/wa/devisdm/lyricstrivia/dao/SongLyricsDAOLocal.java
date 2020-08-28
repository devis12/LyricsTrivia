package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author devis
 */
@Local
public interface SongLyricsDAOLocal {
    
   List<SongLyrics> getRandomSongLyricsDB(int n);
   
   SongLyrics getSongLyricsDB(int trackID);
}

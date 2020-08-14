/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
   List<SongLyrics> getRandomSongLyrics(int n);
}

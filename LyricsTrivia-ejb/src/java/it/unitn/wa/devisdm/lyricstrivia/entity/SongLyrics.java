package it.unitn.wa.devisdm.lyricstrivia.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.text.StringEscapeUtils;

/**
 *
 * @author devis
 */
@Entity
@Table(name = "SongLyrics")
public class SongLyrics implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column
    /*MusixMatch trackID*/
    private int trackID;
    
    @Column
    /*MusixMatch track's name*/
    private String trackName;
    
    @Column
    /*MusixMatch track's  artist name*/
    private String trackArtist;
    
    @Column
    /*MusixMatch track's lyrics (30% of them if you're on a free tier)*/
    private String trackLyrics;

    public int getTrackID() {
        return trackID;
    }

    public void setTrackID(int trackID) {
        this.trackID = trackID;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackArtist() {
        return trackArtist;
    }

    public void setTrackArtist(String trackArtist) {
        this.trackArtist = trackArtist;
    }

    public String getTrackLyrics() {
        return trackLyrics;
    }

    public void setTrackLyrics(String trackLyrics) {
        this.trackLyrics = trackLyrics;
    }
    
    /*escape html characters within track info*/
    public void escapeHtml(){
        this.trackArtist = StringEscapeUtils.escapeHtml4(trackArtist);
        this.trackName = StringEscapeUtils.escapeHtml4(trackName);
        this.trackLyrics = StringEscapeUtils.escapeHtml4(trackLyrics);
    }

    public SongLyrics(int trackID, String trackName, String trackArtist, String trackLyrics) {
        this.trackID = trackID;
        this.trackName = trackName;
        this.trackArtist = trackArtist;
        this.trackLyrics = trackLyrics;
    }
    
    public SongLyrics(){}

    @Override
    public int hashCode() {
        int hash = 3 + trackID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SongLyrics other = (SongLyrics) obj;
        if (this.trackID != other.trackID) {
            return false;
        }
        
        return trackName.equals(other.trackName) && trackArtist.equals(other.trackArtist) && trackLyrics.equals(other.trackLyrics);
    }

    @Override
    public String toString() {
        return "it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics[ id=" + trackID + " ]";
    }
    
}

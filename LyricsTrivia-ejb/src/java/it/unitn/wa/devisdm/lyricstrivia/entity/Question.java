package it.unitn.wa.devisdm.lyricstrivia.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author devis
 */
public class Question implements Serializable{
    
    /*id of the corresponding stored question 
    (not a mandatory field, since question could also be a rnd one, supplied by the system, thus not stored)*/
    private int storedId;
    
    private List<SongLyrics> options;
      
    //index of the "right" songlyrics obj: the one that should be chosen by the user
    private int rightAnswerIndex;
 
    //lyrics of option rightAnswerIndex
    private String lyrics;
    
    //index of the lyrics within the options chosen by the user
    private int givenAnswerIndex;

    public int getStoredId() {
        return storedId;
    }

    public void setStoredId(int storedId) {
        this.storedId = storedId;
    }
    
    public List<SongLyrics> getOptions() {
        return options;
    }

    public void setOptions(List<SongLyrics> options) {
        this.options = options;
    }

    public int getRightAnswerIndex() {
        return rightAnswerIndex;
    }

    public void setRightAnswerIndex(int rightAnswerIndex) {
        this.rightAnswerIndex = rightAnswerIndex;
    }
    
    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public int getGivenAnswerIndex() {
        return givenAnswerIndex;
    }

    public void setGivenAnswerIndex(int givenAnswerIndex) {
        this.givenAnswerIndex = givenAnswerIndex;
    }

    public Question(List<SongLyrics> options, int rightAnswerIndex) {
        this.options = options;
        this.lyrics = options.get(rightAnswerIndex).getTrackLyrics();
        this.rightAnswerIndex = rightAnswerIndex;
        this.givenAnswerIndex = -1; //when you instantiate it, user has'nt decided yet
        this.storedId = -1;//question not stored
    }
    
    public Question(int storedId, List<SongLyrics> options, int rightAnswerIndex) {
        this.options = options;
        this.lyrics = options.get(rightAnswerIndex).getTrackLyrics();
        this.rightAnswerIndex = rightAnswerIndex;
        this.givenAnswerIndex = -1; //when you instantiate it, user has'nt decided yet
        this.storedId = storedId;//question stored
    }
    
    public Question(){}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.options);
        hash = 23 * hash + this.rightAnswerIndex;
        hash = 23 * hash + this.givenAnswerIndex;
        hash = 23 * hash + this.storedId;
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
        
        final Question other = (Question) obj;
        if (this.storedId != other.storedId) {
            return false;
        }
        if (this.rightAnswerIndex != other.rightAnswerIndex) {
            return false;
        }
        if (this.givenAnswerIndex != other.givenAnswerIndex) {
            return false;
        }
        if (!Objects.equals(this.options, other.options)) {
            return false;
        }
        return true;
    }
    
    
    
}

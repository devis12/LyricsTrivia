/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.entity;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author devis
 */
public class Question {
    
    private List<SongLyrics> options;
    
    //index of the "right" songlyrics obj: the one that should be chosen by the user
    private int rightAnswerIndex;
    
    //index of the lyrics within the options chosen by the user
    private int givenAnswerIndex;

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

    public int getGivenAnswerIndex() {
        return givenAnswerIndex;
    }

    public void setGivenAnswerIndex(int givenAnswerIndex) {
        this.givenAnswerIndex = givenAnswerIndex;
    }

    public Question(List<SongLyrics> options, int rightAnswerIndex, int givenAnswerIndex) {
        this.options = options;
        this.rightAnswerIndex = rightAnswerIndex;
        this.givenAnswerIndex = givenAnswerIndex;
    }
    
    public Question(){}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.options);
        hash = 23 * hash + this.rightAnswerIndex;
        hash = 23 * hash + this.givenAnswerIndex;
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

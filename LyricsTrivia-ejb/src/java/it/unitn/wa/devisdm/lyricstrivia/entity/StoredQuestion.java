package it.unitn.wa.devisdm.lyricstrivia.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Question to be stored in the db
 * @author devis
 */
@Entity
@Table(name="StoredQuestion")
public class StoredQuestion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;//automatic internal id related to the question
    
    /*Player from which the question is coming*/
    @Column
    private String askingPlayer;
    
    /*Player to which the question is directed towards*/
    @Column
    private String askedPlayer;
    
    /*List 4 options (just take into account their musix match trackID)*/
    @Column
    private int trackID1;
    
    @Column
    private int trackID2;

    @Column
    private int trackID3;

    @Column
    private int trackID4;
    
    /*right answer trackID: belongs to set {trackID1,trackID2,trackID3,trackID4}*/
    @Column
    private int rightTrackID;
    
    /*answered trackID: belongs to set {trackID1,trackID2,trackID3,trackID4}*/
    @Column
    private int givenTrackID;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAskingPlayer() {
        return askingPlayer;
    }

    public void setAskingPlayer(String askingPlayer) {
        this.askingPlayer = askingPlayer;
    }

    public String getAskedPlayer() {
        return askedPlayer;
    }

    public void setAskedPlayer(String askedPlayer) {
        this.askedPlayer = askedPlayer;
    }

    public int getTrackID1() {
        return trackID1;
    }

    public void setTrackID1(int trackID1) {
        this.trackID1 = trackID1;
    }

    public int getTrackID2() {
        return trackID2;
    }

    public void setTrackID2(int trackID2) {
        this.trackID2 = trackID2;
    }

    public int getTrackID3() {
        return trackID3;
    }

    public void setTrackID3(int trackID3) {
        this.trackID3 = trackID3;
    }

    public int getTrackID4() {
        return trackID4;
    }

    public void setTrackID4(int trackID4) {
        this.trackID4 = trackID4;
    }

    public int getRightTrackID() {
        return rightTrackID;
    }

    public void setRightTrackID(int rightTrackID) {
        this.rightTrackID = rightTrackID;
    }

    public int getGivenTrackID() {
        return givenTrackID;
    }

    public void setGivenTrackID(int givenTrackID) {
        this.givenTrackID = givenTrackID;
    }

    public StoredQuestion(String askingPlayer, String askedPlayer, int trackID1, int trackID2, int trackID3, int trackID4, int rightTrackID, int givenTrackID) {
        this.askingPlayer = askingPlayer;
        this.askedPlayer = askedPlayer;
        this.trackID1 = trackID1;
        this.trackID2 = trackID2;
        this.trackID3 = trackID3;
        this.trackID4 = trackID4;
        this.rightTrackID = rightTrackID;
        this.givenTrackID = givenTrackID;
    }
    
    public StoredQuestion(){}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final StoredQuestion other = (StoredQuestion) obj;
        
        if (this.id != other.id) {
            return false;
        }
        if (this.trackID1 != other.trackID1) {
            return false;
        }
        if (this.trackID2 != other.trackID2) {
            return false;
        }
        if (this.trackID3 != other.trackID3) {
            return false;
        }
        if (this.trackID4 != other.trackID4) {
            return false;
        }
        if (this.rightTrackID != other.rightTrackID) {
            return false;
        }
        if (this.givenTrackID != other.givenTrackID) {
            return false;
        }
        if (!Objects.equals(this.askingPlayer, other.askingPlayer)) {
            return false;
        }
        if (!Objects.equals(this.askedPlayer, other.askedPlayer)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "it.unitn.wa.devisdm.lyricstrivia.entity.QuestionStored[ id=" + id + " ]";
    }
    
}

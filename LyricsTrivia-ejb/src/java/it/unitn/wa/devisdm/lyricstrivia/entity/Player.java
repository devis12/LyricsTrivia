package it.unitn.wa.devisdm.lyricstrivia.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author devis
 */
@Entity
@Table(name="Player")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column
    private String username; 
    
    @Column
    private String email;
    
    @Column
    private byte[] pwd;//encrypted pwd
    
    @Column
    private byte[] salt;//used to compute password and hide the fact that different users can have same passwords
    
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthdate;
    
    @Column
    private char gender;
    
    @Column
    private int played;//number of played challenges
    
    @Column
    private boolean confirmed;//account has been confirmed
    
    @Column
    private int won;//number of challenges won
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPwd() {
        return pwd;
    }

    public void setPwd(byte[] pwd) {
        this.pwd = pwd;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }
    
    public boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Player(String username, String email, byte[] pwd, byte[] salt, Date birthdate, char gender, int played, int won, boolean confirmed) {
        this.username = username;
        this.email = email;
        this.pwd = pwd;
        this.salt = salt;
        this.birthdate = birthdate;
        this.gender = gender;
        this.played = played;
        this.won = won;
        this.confirmed = confirmed;
    }

    public Player(){}

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + this.email.hashCode();
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
        final Player other = (Player) obj;

        boolean intCheck = this.gender == other.gender;
        
        if(this.birthdate == null && other.birthdate != null || this.birthdate != null && other.birthdate == null)
            return false;
        
        return intCheck && this.confirmed == other.confirmed && (this.birthdate==null && other.birthdate==null || this.birthdate.compareTo(other.birthdate)==0) && this.username.equals(other.username) && 
                this.email.equals(other.email) && Arrays.equals(this.pwd, other.pwd) && Arrays.equals(this.salt, other.salt);
    }
    
    
    
    @Override
    public String toString() {
        return "it.unitn.wa.devisdm.lyricstrivia.entity.Player[ id=" + username + " ]";
    }
    
    /* Erase private information and create a new user without them... flag to hide also the email
     *
    */
    public static Player erasePrivateInfo(Player player, boolean eraseAlsoEmail){
        
        if(player == null)
            return null;
        
        String email = eraseAlsoEmail? "" : player.email;
        
        return new Player(player.username, email, "".getBytes(), "".getBytes(), player.birthdate, player.gender, player.played, player.won, player.confirmed);
    }
}

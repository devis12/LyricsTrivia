/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private String email;
    
    @Column
    private String pwd;
    
    @Column
    private String salt;
    
    @Column
    private int age;
    
    @Column
    private char genre;
    
    @Column
    private int played;
    
    @Column
    private int won;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getGenre() {
        return genre;
    }

    public void setGenre(char genre) {
        this.genre = genre;
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

    public Player(String email, String pwd, String salt, int age, char genre, int played, int won) {
        this.email = email;
        this.pwd = pwd;
        this.salt = salt;
        this.age = age;
        this.genre = genre;
        this.played = played;
        this.won = won;
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

        boolean intCheck = this.age == other.age && this.genre == other.genre && this.played != other.played && this.won == other.won;

        return intCheck && this.email.equals(other.email) && this.pwd.equals(other.pwd) && this.salt.equals(other.salt);
    }
    
    
    
    @Override
    public String toString() {
        return "it.unitn.wa.devisdm.lyricstrivia.entity.Player[ id=" + email + " ]";
    }
    
}

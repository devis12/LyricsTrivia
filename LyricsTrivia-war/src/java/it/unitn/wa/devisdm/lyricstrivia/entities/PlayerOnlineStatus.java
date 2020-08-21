/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.entities;

import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.io.Serializable;

/**
 *
 * @author devis
 */
public class PlayerOnlineStatus extends Player implements Serializable{
    private boolean online;
    
    
    public PlayerOnlineStatus(Player player, boolean online){
        
        super(player.getUsername(), player.getEmail(), player.getPwd(), player.getSalt(), 
                player.getBirthdate(),player.getGender(), player.getPlayed(), player.getWon(), player.getConfirmed());
        this.online=online;
    }
    
    public PlayerOnlineStatus(){}

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    
    /*For now it's better to keep hashcode and equals of super class Player*/
    
}

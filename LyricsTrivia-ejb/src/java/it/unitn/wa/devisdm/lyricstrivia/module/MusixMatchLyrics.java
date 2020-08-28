package it.unitn.wa.devisdm.lyricstrivia.module;

import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

/**
 * Wrapper beans useful to make usage of the MusixMatch API and unofficial SDK https://github.com/sachin-handiekar/jMusixMatch
 * for delivering SongLyrics data in a LyricsTrivia like fashion to other beans
 * @author devis
 */
@Stateless
public class MusixMatchLyrics implements MusixMatchLyricsLocal{
    
    private static final String APIKEY = "713ed43cd74710c3f654538b051c0ca8";
    private final MusixMatch musixMatch = new MusixMatch(APIKEY);
    
    
    private SongLyrics convertTrackToSongLyrics(TrackData data) throws MusixMatchException{
        if(data == null || data.getHasLyrics() == 0)//song not valid or without valid lyrics in musixmatch db
            return null;

        int trackID = data.getTrackId();
        Lyrics lyrics = musixMatch.getLyrics(trackID);//recover lyrics using musixmatch's trackid

        int endIndex = lyrics.getLyricsBody().indexOf("******* This Lyrics is NOT for Commercial use *******");//take away this part of the lyrics, if present
        endIndex = endIndex != -1 ? endIndex : lyrics.getLyricsBody().length();
        
        return new SongLyrics(trackID, data.getTrackName(), data.getArtistName(), lyrics.getLyricsBody().substring(0, endIndex));
    }
    
    @Override
    public SongLyrics getSongLyrics(String trackArtist, String trackName) {
        Track track;
        try {
            //recover lyrics data of a specific song written by artist with name @trackArtist using musxmatch sdk
            track = musixMatch.getMatchingTrack(trackName, trackArtist);
            TrackData data = track.getTrack();
            
            return convertTrackToSongLyrics(data);
            
        } catch (MusixMatchException ex) {
            Logger.getLogger(MusixMatchLyrics.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    @Override
    public SongLyrics getSongLyrics(int trackID) {
        Track track;
        try {
            //recover lyrics data of a specific song written by artist with name @trackArtist using musxmatch sdk
            track = musixMatch.getTrack(trackID);
            TrackData data = track.getTrack();
            
            return convertTrackToSongLyrics(data);
            
        } catch (MusixMatchException ex) {
            Logger.getLogger(MusixMatchLyrics.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
       
    @Override
    public List<SongLyrics> getSongsByArtist(String trackArtist) {
        try {
            
            List<Track> tracks = musixMatch.searchTracks("", trackArtist, "", 1, 64, true);
            ArrayList<SongLyrics> slList = new ArrayList<>();

            for(Track track : tracks){
                //recover lyrics data of a specific song written by artist with name @trackArtist using musxmatch sdk
                TrackData data = track.getTrack();
                
                try{
                    SongLyrics sl = convertTrackToSongLyrics(data);
                    if(sl != null)
                        slList.add(sl);
                }catch(com.google.gson.JsonSyntaxException exception){
                    //just skip to add this song, if response in the info are bad formatted
                }
            }
            
            return slList;
            
        } catch (MusixMatchException ex) {
            Logger.getLogger(MusixMatchLyrics.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    @Override
    public List<SongLyrics> getSongsByName(String trackName) {
        try {
            
            List<Track> tracks = musixMatch.searchTracks(trackName, "", "", 1, 32, true);
            ArrayList<SongLyrics> slList = new ArrayList<>();

            for(Track track : tracks){
                //recover lyrics data of a specific song written by artist with name @trackArtist using musxmatch sdk
                TrackData data = track.getTrack();
                
                try{
                    SongLyrics sl = convertTrackToSongLyrics(data);
                    if(sl != null)
                        slList.add(sl);
                }catch(com.google.gson.JsonSyntaxException exception){
                    //just skip to add this song, if response in the info are bad formatted
                }
            }
            
            return slList;
            
        } catch (MusixMatchException ex) {
            Logger.getLogger(MusixMatchLyrics.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
}

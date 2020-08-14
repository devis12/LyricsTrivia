/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Question;
import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateful;

/**
 *
 * @author devis
 */
@Stateful
public class QuestionDAO implements QuestionDAORemote {
    
    private static final int N_OPTIONS = 4;//options for each question created
    
    @EJB
    private SongLyricsDAOLocal songLyricsDAOLocal;
    
    @Override
    public Question getNewQuestion() {
        List<SongLyrics> slList = songLyricsDAOLocal.getRandomSongLyrics(N_OPTIONS);//question has 4 options
        int rightAnswerIndex = new Random(System.currentTimeMillis()).nextInt(N_OPTIONS);//select 1 option as the answer within the pool of options
        return new Question(slList, rightAnswerIndex, -1);//answer still not given, so -1 as index
    }
}

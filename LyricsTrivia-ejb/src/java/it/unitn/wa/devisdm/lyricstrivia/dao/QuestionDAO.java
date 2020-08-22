/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Question;
import it.unitn.wa.devisdm.lyricstrivia.entity.StoredQuestion;
import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author devis
 */
@Stateful
public class QuestionDAO implements QuestionDAORemote {
            
    @PersistenceContext
    private EntityManager manager;
    
    private static final int N_OPTIONS_RND = 4;//options for each question created randomly
    
    @EJB
    private SongLyricsDAOLocal songLyricsDAOLocal;
    
    @Override
    public Question getNewRndQuestion() {
        List<SongLyrics> slList = songLyricsDAOLocal.getRandomSongLyricsDB(N_OPTIONS_RND);//question has 4 options
        int rightAnswerIndex = new Random(System.currentTimeMillis()).nextInt(N_OPTIONS_RND);//select 1 option as the answer within the pool of options
        return new Question(slList, rightAnswerIndex);//answer still not given, so -1 as index
    }

    @Override
    public void storeNewQuestion(StoredQuestion sq) {
        manager.persist(sq);
    }

    @Override
    public void editStoredQuestion(StoredQuestion sq) {
        manager.merge(sq);
    }

    @Override
    public StoredQuestion deleteStoredQuestion(int id) {
        StoredQuestion sqDel = getStoredQuestion(id);
        manager.remove(sqDel);
        return sqDel;
    }

    @Override
    public StoredQuestion getStoredQuestion(int id) {
        return manager.find(StoredQuestion.class, id);
    }

    @Override
    public List<Question> getUnansweredQuestion(String askedPlayerU) {
        if(askedPlayerU == null)
            return null;
        
        Query query = manager.createQuery("from StoredQuestion Q where Q.askedPlayer = :askedPlayerU AND Q.givenTrackID = -1", StoredQuestion.class);
        query.setParameter("askedPlayerU", askedPlayerU);
        List<StoredQuestion> storedQuestions = query.getResultList();
        
        List<Question> questions = new ArrayList<>();
        for(StoredQuestion sq : storedQuestions){
            List<SongLyrics> options = new ArrayList<>();
            int rightAnswerIndex = -1;
            
            options.add(songLyricsDAOLocal.getSongLyricsDB(sq.getTrackID1()));
            rightAnswerIndex = (rightAnswerIndex == -1 && sq.getRightTrackID() == sq.getTrackID1())? 0 : rightAnswerIndex;
            
            options.add(songLyricsDAOLocal.getSongLyricsDB(sq.getTrackID2()));
            rightAnswerIndex = (rightAnswerIndex == -1 && sq.getRightTrackID() == sq.getTrackID2())? 1 : rightAnswerIndex;
            
            options.add(songLyricsDAOLocal.getSongLyricsDB(sq.getTrackID3()));
            rightAnswerIndex = (rightAnswerIndex == -1 && sq.getRightTrackID() == sq.getTrackID3())? 2 : rightAnswerIndex;
            
            options.add(songLyricsDAOLocal.getSongLyricsDB(sq.getTrackID4()));
            rightAnswerIndex = (rightAnswerIndex == -1 && sq.getRightTrackID() == sq.getTrackID4())? 3 : rightAnswerIndex;
            
            questions.add(new Question(sq.getId(), options, rightAnswerIndex));
        }
        
        return questions;
    }
}

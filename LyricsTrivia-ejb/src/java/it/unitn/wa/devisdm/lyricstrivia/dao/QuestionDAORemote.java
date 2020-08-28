package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Question;
import it.unitn.wa.devisdm.lyricstrivia.entity.StoredQuestion;
import java.util.List;
import javax.ejb.Remote;

/**
 * CRUD operation on StoredQuestion Entity 
 * plus interaction with SongLyrics Beans to generate random questions (that are not supposed to be stored)
 * @author devis
 */
@Remote
public interface QuestionDAORemote {
    
    Question getNewRndQuestion();
    
    void storeNewQuestion(StoredQuestion sq);
    
    StoredQuestion getStoredQuestion(int id);
    
    void editStoredQuestion(StoredQuestion sq);
    
    StoredQuestion deleteStoredQuestion(int id);
    
    List<Question> getUnansweredQuestion(String askedPlayer);
    
}

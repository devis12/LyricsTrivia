/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.dao;

import it.unitn.wa.devisdm.lyricstrivia.entity.Question;
import it.unitn.wa.devisdm.lyricstrivia.entity.StoredQuestion;
import java.util.List;
import javax.ejb.Remote;

/**
 *
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

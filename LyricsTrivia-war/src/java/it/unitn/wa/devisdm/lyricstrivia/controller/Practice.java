/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.controller;

import com.google.gson.Gson;
import it.unitn.wa.devisdm.lyricstrivia.dao.QuestionDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Question;
import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author devis
 */
public class Practice extends HttpServlet {
    
    private QuestionDAORemote questionDAORemote;
    
    @Override
    public void init(ServletConfig config) throws ServletException { 
        super.init(config);
        
        initEJB();
    }
    
    @Override
    public void destroy(){
        super.destroy();
        questionDAORemote = null;
    }
    
    private void initEJB(){
    
        try {
            InitialContext ctx = new InitialContext();
            questionDAORemote = 
                    (QuestionDAORemote) ctx.lookup("java:global/LyricsTrivia/LyricsTrivia-ejb/QuestionDAO!it.unitn.wa.devisdm.lyricstrivia.dao.QuestionDAORemote");
            
        } catch (NamingException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // 
    /**
     * Handles the HTTP <code>GET</code> method.
     * GET a NEW practice question
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(questionDAORemote == null)
            initEJB();
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("player") == null){//this should never happen in any case (assuming proper filters)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            out.flush();
            return;
        }
        
        Question pq = questionDAORemote.getNewRndQuestion();
        
        int pqRightIndex = pq.getRightAnswerIndex();//save right index
        pq.setRightAnswerIndex(-1);//hidden answer index to user (so it can't check json)
        
        for(SongLyrics sl : pq.getOptions()){sl.setTrackLyrics("");}//hidden lyrics of the four options, so user can't cheat
        session.setAttribute("pq", pq);
        session.setAttribute("pqRightIndex", pqRightIndex);
        
        out.print(new Gson().toJson(pq));
        
    }
    
    // 
    /**
     * Handles the HTTP <code>GET</code> method.
     * PUT to answer the practice question obtain with GET
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(questionDAORemote == null)
            initEJB();
        
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("player") == null || 
                session.getAttribute("pq") == null || request.getParameter("givenAnswerIndex") == null){//this should never happen in any case (assuming proper filters)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            out.flush();
            return;
        }
        
        Question pq = (Question) session.getAttribute("pq");
        int pqRightIndex = (int) session.getAttribute("pqRightIndex");
        int givenAnswerIndex = Integer.parseInt(request.getParameter("givenAnswerIndex"));
        pq.setGivenAnswerIndex(givenAnswerIndex);
        pq.setRightAnswerIndex(pqRightIndex);//put again the right answer index in the practice question, so the player will know if he/she answered right
        
        //remove session attributes
        session.removeAttribute("pq");
        session.removeAttribute("pqRightIndex");
        
        out.print(new Gson().toJson(pq));
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet will return random question that has to be asked to online user";
    }// </editor-fold>

}

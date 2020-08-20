/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.controller;

import it.unitn.wa.devisdm.lyricstrivia.dao.SongLyricsDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.SongLyrics;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author devis
 */
@WebServlet(name = "Test", urlPatterns = {"/Test"})
public class Test extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Test</title>");            
            out.println("</head>");
            out.println("<body>");
            SongLyricsDAORemote songLyricsDAORemote;
            try {
                InitialContext ctx = new InitialContext();
                songLyricsDAORemote = 
                        (SongLyricsDAORemote) ctx.lookup("java:global/LyricsTrivia/LyricsTrivia-ejb/SongLyricsDAO!it.unitn.wa.devisdm.lyricstrivia.dao.SongLyricsDAORemote");
                List<SongLyrics> list = new ArrayList<>();
                if(request.getParameter("artist")!=null)
                    list = songLyricsDAORemote.getSongsByArtistJMM(request.getParameter("artist"));
                if(request.getParameter("song")!=null)
                    list = songLyricsDAORemote.getSongsByNameJMM(request.getParameter("song"));

                for(SongLyrics sl : list){       
                    out.println("<br /><br /><br /><br /><b>ID</b>: " + sl.getTrackID());
                    out.println("<br /><b>Name</b>: " + sl.getTrackName());
                    out.println("<br /><b>Artist</b>: " + sl.getTrackArtist());
                    out.println("<br /><b>Lyrics</b>: " + sl.getTrackLyrics());
                    songLyricsDAORemote.addSongLyricsDB(sl);
                }
                
            } catch (NamingException ex) {
                Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace(out);
            }
        
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

package it.unitn.wa.devisdm.lyricstrivia.controller;

import it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import it.unitn.wa.devisdm.lyricstrivia.util.TokenGenerator;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author devis
 */
public class Login extends HttpServlet {
    
    private PlayerDAORemote playerDAORemote;
    
    @Override
    public void init(ServletConfig config) throws ServletException { 
        super.init(config);
        initEJB();
        
    }
    
    @Override
    public void destroy(){
        super.destroy();
        playerDAORemote = null;
    }
    
    private void initEJB(){
        try {
            InitialContext ctx = new InitialContext();
            playerDAORemote = 
                    (PlayerDAORemote) ctx.lookup("java:global/LyricsTrivia-ejb/PlayerDAO!it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote");
            
        } catch (NamingException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        String username = request.getParameter("username");
        String pwd = request.getParameter("password");
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        if(playerDAORemote == null)
            initEJB();
        
        Player player = playerDAORemote.getPlayer(username);
        
        try{
            if(player != null && Arrays.equals(player.getPwd(), TokenGenerator.getEncryptedPassword(pwd, player.getSalt()))){//login success: set new session with player attribute 
                //user found
                request.getSession().setAttribute("player", player);
                response.sendRedirect(response.encodeRedirectURL(contextPath + "home_page.jsp"));
            }else{
                request.setAttribute("error_msg", "Wrong credentials!");
                request.getRequestDispatcher("landing.jsp").forward(request, response);
            }
        }catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error_msg", "Something went wrong and it's our fault! We're terribly sorry... repeat the registration or contact our staff");
        }
  
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Login servlet for players";
    }// </editor-fold>

}
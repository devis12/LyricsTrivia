package it.unitn.wa.devisdm.lyricstrivia.controller;

import it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import it.unitn.wa.devisdm.lyricstrivia.util.Mail;
import it.unitn.wa.devisdm.lyricstrivia.util.MailException;
import it.unitn.wa.devisdm.lyricstrivia.util.TokenGenerator;
import it.unitn.wa.devisdm.lyricstrivia.util.UtilityCheck;
import java.io.IOException;
import java.time.LocalDateTime;
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
public class PwdRecovery extends HttpServlet {
    
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
     * Handles the HTTP <code>GET</code> method for pwdRecovery
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(playerDAORemote == null)
            initEJB();
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        String username = request.getParameter("player");//username of the account that has to be recovered
        String token = request.getParameter("token");//email of the account that has to be recovered
        
        if(username == null || token == null || request.getParameter("h") == null || request.getParameter("d") == null){//invalid parameters check1 (just if they are present)
            request.setAttribute("error_msg", "Invalid operation: bad request!");
            request.getRequestDispatcher("landing.jsp").forward(request, response);
            return;
        }
        
        int hour = Integer.parseInt(request.getParameter("h"));
        int day = Integer.parseInt(request.getParameter("d"));
        
        Player player = playerDAORemote.getPlayer(username);
        if(player == null || !token.equals(TokenGenerator.generateRecoveryToken(player.getSalt(), player.getPwd(), hour, day))){//invalid parameters check2 (username & token integrity)
            request.setAttribute("error_msg", "Invalid operation: bad request!");
            request.getRequestDispatcher("landing.jsp").forward(request, response);
            
        }else{
            //log in the user
            request.getSession().setAttribute("player", player);
            request.getSession(false).setAttribute("set_new_pwd", true);//force the user to insert new pwd with proper modal
            response.sendRedirect(response.encodeRedirectURL(contextPath + "home_page.jsp"));
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method for pwdRecovery
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(playerDAORemote == null)
            initEJB();
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        String email = request.getParameter("emailRecovery");//email of the account that has to be recovered
        
        Player player = playerDAORemote.getPlayerByEmail(email);
        if(email == null || !UtilityCheck.isValidEmail(email) || player == null){//bad request 
            request.setAttribute("error_msg", "Invalid operation: bad request!");
            request.getRequestDispatcher("landing.jsp").forward(request, response);
            return;
        }
        
        try{
            int hour = LocalDateTime.now().getHour(); //if everithing is correct the servlet generate a tokek and send it to the user
            int day = LocalDateTime.now().getDayOfYear();
            String link = request.getServerName() + ":" + request.getServerPort()
                    + contextPath
                    + this.getServletContext().getServletRegistrations().get("PwdRecovery").getMappings().iterator().next()
                    + "?player=" + player.getUsername() + "&token=" + TokenGenerator.generateRecoveryToken(player.getSalt(), player.getPwd(), hour, day)
                    + "&h=" + hour + "&d=" + day;
            
            String content = "Hi " + player.getUsername() + "!\n"
                    + "It seems like you have requested the recovery of your password, click on the link below to login and set up a new password"
                    + "\n\n<a href=\"http://" + link + "\">" + link + "</a>"
                    + "\n\n\n<p style='font-weight:bold'>WARNING</p>If you've received this mail and you've not requested it, contact our staff.\n";
            
            Mail mail = new Mail(email, "LyricsTrivia - Password recovery", content);
            mail.send();
            request.setAttribute("success_msg", "We've sent you an email with the token to login again in your account");
            
        
        }catch(MailException ex) {//if occur some error while sending mail
            Logger.getLogger(PwdRecovery.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error_msg", "Internal server error: try again or contact the staff!");
        
        }finally{
            request.getRequestDispatcher("landing.jsp").forward(request, response);
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Send email for pwd recovery (doPost) and process click on that very link (doGet)";
    }// </editor-fold>

}

package it.unitn.wa.devisdm.lyricstrivia.controller;

import it.unitn.wa.devisdm.lyricstrivia.dao.OnlinePlayersRemote;
import it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import it.unitn.wa.devisdm.lyricstrivia.util.Mail;
import it.unitn.wa.devisdm.lyricstrivia.util.MailException;
import it.unitn.wa.devisdm.lyricstrivia.util.TokenGenerator;
import java.io.IOException;
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
 * Servlet for validating player account with token sent in registration process
 * @author devis
 */
public class Validation extends HttpServlet {

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
        if(playerDAORemote == null)
            initEJB();
        
        String username = request.getParameter("player");//the servlet check if the parameters are setted
        String token = request.getParameter("token");
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            Player player = playerDAORemote.getPlayer(username);//the servlet tries to get the user by his username
            
            if(player == null){
                request.setAttribute("error_msg", "This link is not valid at all... sorry :-( ");
                
            }else if (player.getConfirmed()) { //if the user has already confirm his/her account
                request.getSession().setAttribute("player", player); //the servlet log the user and send a message that confirm that the account is already verified
                request.setAttribute("warning_msg", "Your account has already been confirmed!");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "home_page.jsp"));
                return;
            
            } else if (token.equals(TokenGenerator.generateValidationToken(player.getSalt(), player.getPwd()))) { //if the token is correct then validate the user and update the data to the DB
                player.setConfirmed(true);
                playerDAORemote.editPlayer(player);
                
                request.getSession().setAttribute("player", player); //the servlet log the user and send a message that confirm that the account is already verified 
                ((OnlinePlayersRemote) this.getServletContext().getAttribute("onlinePlayersRemote")).setOnline(Player.erasePrivateInfo(player, true));
                request.setAttribute("success_msg", "Account confirmed correctly: now go on and play!");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "home_page.jsp"));
                return;
                
            } else { //if token is not correct, cannot validate account, the servlet comunicate the errors to the user and send another mail with another token 
                              
                //generate a link to activate the account
                String link = request.getServerName() + ":" + request.getServerPort()
                        + contextPath
                        + this.getServletContext().getServletRegistrations().get("Validation").getMappings().iterator().next()
                        + "?player=" + player.getUsername() + "&token=" + TokenGenerator.generateValidationToken(player.getSalt(), player.getPwd());

                //prepare the content of the email to verify the account
                String content = "<b> Hey " + player.getUsername() + "!</b> <br />"
                        + "Soory, it seems that there has been some trouble in the validation process...<br /> "
                        + "Do not worry about it, we've generated again your token and you're going to be able to activate your account by just clicking on the following link:"
                        + "<a href=\"http://" + link + "\">" + link + "</a>";
                Mail mail = new Mail(player.getEmail(), "LyricsTrivia - Confirm Registration", content);
                mail.send();//send the email to the user
                
                request.setAttribute("warning_msg", "Something went wrong along the process, but do not worry, we've sent you another email... go on: try again!");
            }
            
        } catch (MailException ex) {//if occur some error with the email
            Logger.getLogger(Validation.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error_msg", "Something went wrong while sending your email... repeat the registration or contact our staff");
        }
        
        request.getRequestDispatcher("landing.jsp").forward(request, response);

    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Validation Servlet to update new user validate flag in db in order to confirm his/her account";
    }// </editor-fold>

}

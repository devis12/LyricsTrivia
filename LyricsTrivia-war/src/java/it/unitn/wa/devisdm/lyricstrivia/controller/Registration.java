package it.unitn.wa.devisdm.lyricstrivia.controller;


import it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import it.unitn.wa.devisdm.lyricstrivia.util.InvalidArgumentException;
import it.unitn.wa.devisdm.lyricstrivia.util.Mail;
import it.unitn.wa.devisdm.lyricstrivia.util.MailException;
import it.unitn.wa.devisdm.lyricstrivia.util.TokenGenerator;
import it.unitn.wa.devisdm.lyricstrivia.util.UtilityCheck;
import it.unitn.wa.devisdm.lyricstrivia.util.VerifyRecaptcha;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class Registration extends HttpServlet {
    
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
    
    /*  Check validity of mandatory parameters*/
    private boolean validParameters(String username, String email, String pwd1, String pwd2) throws InvalidArgumentException {
        String err_text = "There is something wrong with the";
        if (username == null || username.length() < 3 || playerDAORemote.getPlayer(username) != null) {
            throw new InvalidArgumentException(err_text + " username");
        }
        if (!UtilityCheck.isValidEmail(email)) {
            throw new InvalidArgumentException(err_text + " email");
        }
        if (!pwd1.equals(pwd2)) {
            throw new InvalidArgumentException(err_text + " passwords are not equals!");
        }
        if (!UtilityCheck.isStrong(pwd1)) {
            throw new InvalidArgumentException(err_text + " password: not strong enough!");
        }
        return true;
    }

    
    /**
     * Handles the HTTP request for registration
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

        //mandatory fields
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String pwd1 = request.getParameter("password1");
        String pwd2 = request.getParameter("password2");
        
        //optional fields
        Date birthdate = null;
        try{
            if(request.getParameter("birthdate") != null)
                birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("birthdate"));
        }catch(ParseException pEx){
            birthdate = null;
        }
        
        char gender = (request.getParameter("gender") != null && request.getParameter("gender").length()>0)? request.getParameter("gender").charAt(0) : null;
        gender = (gender == 'M' || gender == 'F')? gender : 'O';
        
        //At registration, players cannot have played any games!
        int played = 0;
        int won = 0;
        
        String contextPath = getServletContext().getContextPath();
        try{    
            // get reCAPTCHA request param
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            System.out.println(gRecaptchaResponse);
            boolean verifyCaptcha = VerifyRecaptcha.verify(gRecaptchaResponse);
            
            if(verifyCaptcha && validParameters(username, email, pwd1, pwd2)){//generate exception if something is wrong with info about it, i.e. else case handled in catch below with add. info
                byte[] salt = TokenGenerator.generateSalt();//if everithing is correct it create the user
                byte[] pwdHash = TokenGenerator.getEncryptedPassword(pwd1, salt);

                Player newP = new Player(username, email, pwdHash, salt, birthdate, gender, played, won, false);//false stands for not confirmed account (nned to click on token within email)

                playerDAORemote.addPlayer(newP);
                
                //generate a link to activate the account
                String link = request.getServerName() + ":" + request.getServerPort()
                        + contextPath
                        + this.getServletContext().getServletRegistrations().get("Validation").getMappings().iterator().next()
                        + "?player=" + newP.getUsername() + "&token=" + TokenGenerator.generateValidationToken(salt, pwdHash);

                //prepare the content of the email to verify the account
                String content = "<b> Hey " + newP.getUsername() + "!</b> <br />"
                        + "We welcome you in our website, which will allow you to challenge other lyrics' fanatics,<br /> "
                        + "you're going to be able to activate your account by just clicking on the following link:"
                        + "<a href=\"http://" + link + "\">" + link + "</a>";
                Mail mail = new Mail(email, "LyricsTrivia - Confirm Registration", content);
                mail.send();//send the email to the user
                
                request.setAttribute("success_msg", "Awesome! The email with the confirmation link has been sent");
            
            }
            
        }catch(MailException emailEx){
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, emailEx);
            request.setAttribute("error_msg", "Something went wrong while sending your email... repeat the registration or contact our staff");
        
        }catch(InvalidArgumentException iEx){
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, iEx);
            request.setAttribute("error_msg", "Inserted parameters are not valid!");

        }catch(NoSuchAlgorithmException | InvalidKeySpecException iEx){
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, iEx);
            request.setAttribute("error_msg", "Something went wrong and it's our fault! We're terribly sorry... repeat the registration or contact our staff");
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
        return "Short description";
    }// </editor-fold>

}

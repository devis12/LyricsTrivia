package it.unitn.wa.devisdm.lyricstrivia.controller;

import it.unitn.wa.devisdm.lyricstrivia.dao.OnlinePlayersRemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author devis
 */
public class Logout extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);//do not create a new session
        if(session == null)//no need for logout process
            return;
        
        Player player = (Player) session.getAttribute("player");
        
        if(player != null){
            //set the user as effectively offline
            //set the user as effectively online in the stateful bean handled by the app context
            ((OnlinePlayersRemote) this.getServletContext().getAttribute("onlinePlayersRemote")).setOffline(Player.erasePrivateInfo(player, true));
            /*debug
            HashMap<Player, Boolean> map = ((OnlinePlayersRemote) this.getServletContext().getAttribute("onlinePlayersRemote")).getPlayersMap();
            System.out.print("");*/
        }
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        session.invalidate();
        
        response.sendRedirect(response.encodeRedirectURL(contextPath));//just go back to home
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
        return "Servlet to perform http session invalidation";
    }// </editor-fold>

}

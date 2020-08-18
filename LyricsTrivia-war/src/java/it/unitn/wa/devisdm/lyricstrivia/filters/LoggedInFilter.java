/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.filters;

import it.unitn.wa.devisdm.lyricstrivia.controller.Players;
import it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote;
import it.unitn.wa.devisdm.lyricstrivia.entity.Player;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

/**
 *
 * @author devis
 */
public class LoggedInFilter implements Filter {
    
    private PlayerDAORemote playerDAORemote;
    
    private static final boolean debug = false;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    public LoggedInFilter() {
    }    
    
    private void doBeforeProcessing(RequestWrapper request, ResponseWrapper response)
            throws IOException, ServletException {
        if (debug) {
            log("NewFilter:DoBeforeProcessing");
        }
        HttpSession session = request.getSession(false);
        Player loggedPlayer = (session!=null)? (Player) session.getAttribute("player") : null;
        Player fetchedPlayer = (loggedPlayer!= null)? playerDAORemote.getPlayer(loggedPlayer.getUsername()) : null;
        if(loggedPlayer != null && fetchedPlayer.equals(loggedPlayer)){
            //user already logged, it can proceed to home page as long as its account is confirmed
            if(!loggedPlayer.getConfirmed()){
                request.setAttribute("warning_msg", "You still haven't confirmed your account!");
                request.getRequestDispatcher("landing.jsp").forward(request, response);
            }
            
        }else{
            if(loggedPlayer != null)//user logged with a non valid account... forward it to landing.jsp for security reason
                request.setAttribute("error_msg", "Action not authorized!");           
            
            request.getRequestDispatcher("landing.jsp").forward(request, response);
        }
    }    
    
    private void doAfterProcessing(RequestWrapper request, ResponseWrapper response)
            throws IOException, ServletException {
        if (debug) {
            log("NewFilter:DoAfterProcessing");
        }
        
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        if (debug) {
            log("NewFilter:doFilter()");
        }

        // Create wrappers for the request and response objects.
        // Using these, you can extend the capabilities of the
        // request and response, for example, allow setting parameters
        // on the request before sending the request to the rest of the filter chain,
        // or keep track of the cookies that are set on the response.
        //
        // Caveat: some servers do not handle wrappers very well for forward or
        // include requests.
        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) request);
        ResponseWrapper wrappedResponse = new ResponseWrapper((HttpServletResponse) response);
        
        doBeforeProcessing(wrappedRequest, wrappedResponse);
        
        Throwable problem = null;
        
        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }
        
        doAfterProcessing(wrappedRequest, wrappedResponse);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {                
                log("NewFilter: Initializing filter");
            }
        }
        
        try {
            InitialContext ctx = new InitialContext();
            playerDAORemote = 
                    (PlayerDAORemote) ctx.lookup("java:global/LyricsTrivia-ejb/PlayerDAO!it.unitn.wa.devisdm.lyricstrivia.dao.PlayerDAORemote");
            
        } catch (NamingException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("NewFilter()");
        }
        StringBuffer sb = new StringBuffer("NewFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
        
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);        
        
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);                
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");                
                pw.print(stackTrace);                
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);        
    }

    /**
     * This request wrapper class extends the support class
     * HttpServletRequestWrapper, which implements all the methods in the
     * HttpServletRequest interface, as delegations to the wrapped request. You
     * only need to override the methods that you need to change. You can get
     * access to the wrapped request using the method getRequest()
     */
    class RequestWrapper extends HttpServletRequestWrapper {
        
        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }

        // You might, for example, wish to add a setParameter() method. To do this
        // you must also override the getParameter, getParameterValues, getParameterMap,
        // and getParameterNames methods.
        protected Hashtable localParams = null;
        
        public void setParameter(String name, String[] values) {
            if (debug) {
                System.out.println("NewFilter::setParameter(" + name + "=" + values + ")" + " localParams = " + localParams);
            }
            
            if (localParams == null) {
                localParams = new Hashtable();
                // Copy the parameters from the underlying request.
                Map wrappedParams = getRequest().getParameterMap();
                Set keySet = wrappedParams.keySet();
                for (Iterator it = keySet.iterator(); it.hasNext();) {
                    Object key = it.next();
                    Object value = wrappedParams.get(key);
                    localParams.put(key, value);
                }
            }
            localParams.put(name, values);
        }
        
        @Override
        public String getParameter(String name) {
            if (debug) {
                System.out.println("NewFilter::getParameter(" + name + ") localParams = " + localParams);
            }
            if (localParams == null) {
                return getRequest().getParameter(name);
            }
            Object val = localParams.get(name);
            if (val instanceof String) {
                return (String) val;
            }
            if (val instanceof String[]) {
                String[] values = (String[]) val;
                return values[0];
            }
            return (val == null ? null : val.toString());
        }
        
        @Override
        public String[] getParameterValues(String name) {
            if (debug) {
                System.out.println("NewFilter::getParameterValues(" + name + ") localParams = " + localParams);
            }
            if (localParams == null) {
                return getRequest().getParameterValues(name);
            }
            return (String[]) localParams.get(name);
        }
        
        @Override
        public Enumeration getParameterNames() {
            if (debug) {
                System.out.println("NewFilter::getParameterNames() localParams = " + localParams);
            }
            if (localParams == null) {
                return getRequest().getParameterNames();
            }
            return localParams.keys();
        }        
        
        @Override
        public Map getParameterMap() {
            if (debug) {
                System.out.println("NewFilter::getParameterMap() localParams = " + localParams);
            }
            if (localParams == null) {
                return getRequest().getParameterMap();
            }
            return localParams;
        }
    }

    /**
     * This response wrapper class extends the support class
     * HttpServletResponseWrapper, which implements all the methods in the
     * HttpServletResponse interface, as delegations to the wrapped response.
     * You only need to override the methods that you need to change. You can
     * get access to the wrapped response using the method getResponse()
     */
    class ResponseWrapper extends HttpServletResponseWrapper {
        
        public ResponseWrapper(HttpServletResponse response) {
            super(response);            
        }

        // You might, for example, wish to know what cookies were set on the response
        // as it went throught the filter chain. Since HttpServletRequest doesn't
        // have a get cookies method, we will need to store them locally as they
        // are being set.
        /*
	protected Vector cookies = null;
	
	// Create a new method that doesn't exist in HttpServletResponse
	public Enumeration getCookies() {
		if (cookies == null)
		    cookies = new Vector();
		return cookies.elements();
	}
	
	// Override this method from HttpServletResponse to keep track
	// of cookies locally as well as in the wrapped response.
	public void addCookie (Cookie cookie) {
		if (cookies == null)
		    cookies = new Vector();
		cookies.add(cookie);
		((HttpServletResponse)getResponse()).addCookie(cookie);
	}
         */
    }
    
}

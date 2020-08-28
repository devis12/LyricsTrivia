package it.unitn.wa.devisdm.lyricstrivia.util;

import java.util.HashMap;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;

/**
 * Useful functions to extract Path or Query parameters
 * Comes handy in combination with Gson library to develop quick REST services out of a servlet
 * @author devis
 */
public class RequestsUtilities {
    
    /*  e.g. Given request http://ciao.com/example/stud/18 returns 18*/
    public static String getPathParameter(HttpServletRequest request){
        String uri = request.getRequestURI();
        String servletPath = request.getServletPath();
        String parameter = uri.substring(uri.lastIndexOf(servletPath)+servletPath.length());
        if(parameter.startsWith("/"))
            parameter = parameter.substring(1);
        return parameter;
    }
    
    /*  e.g. Given request http://ciao.com/example/stud?filterByName=Gianni returns map with {filterByName => "Gianni"}*/
    public static HashMap<String, String> getQueryParameters(HttpServletRequest request){
        HashMap<String, String> queryParams = new HashMap<>();
        String queryString = request.getQueryString();
        if(queryString != null){    
            StringTokenizer st = new StringTokenizer(queryString, "&");  
            while (st.hasMoreTokens()) {  
                String token = st.nextToken();
                int splitIndex = token.indexOf("=");
                queryParams.put(token.substring(0,splitIndex), token.substring(splitIndex+1));
            }  
        }
        return queryParams;
    }
    
}

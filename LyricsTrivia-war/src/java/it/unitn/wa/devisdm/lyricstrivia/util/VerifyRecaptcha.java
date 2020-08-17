/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author devis
 */
public class VerifyRecaptcha {
    
    private class ReCaptchav2Response{
        boolean success;
        String challenge_ts;    // timestamp of the challenge load (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)
        String hostname;         // the hostname of the site where the reCAPTCHA was solved
        
        public ReCaptchav2Response(){}
        public ReCaptchav2Response(boolean success, String challenge_ts, String hostname){
            this.success = success;
            this.challenge_ts = challenge_ts;
            this.hostname = hostname;
        }
    }
    
    public static final String URL = "https://www.google.com/recaptcha/api/siteverify";
    public static final String SECRET = "6Lfal78ZAAAAAC7KRxkzUpWeeZaJ6xECo2FjwAT7";
    private final static String USER_AGENT = "Mozilla/5.0";

	public static boolean verify(String gRecaptchaResponse) throws IOException {
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		
		try{
                    URL obj = new URL(URL);
                    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                    // add reuqest header
                    con.setRequestMethod("POST");
                    con.setRequestProperty("User-Agent", USER_AGENT);
                    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                    String postParams = "secret=" + SECRET + "&response="
                                    + gRecaptchaResponse;

                    // Send post request
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(postParams);
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'POST' request to URL : " + URL);
                    System.out.println("Post parameters : " + postParams);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                                    con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                    }
                    in.close();

                    // print result
                    String jsonString = response.toString();

                    //parse JSON response and return 'success' value
                    ReCaptchav2Response jsonResp = (new Gson()).fromJson(jsonString, ReCaptchav2Response.class);
                    
		
                    return jsonResp.success;
                    
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}

package it.unitn.wa.devisdm.lyricstrivia.util;

import java.util.regex.Pattern;

/**
 * Utility checks for validity of an email, strong password...
 * @author devis
 */
public class UtilityCheck {
    
    /* Check email validity with regex*/
    public static boolean isValidEmail(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
    } 
    
    /* Check pwd robustness: 
        - at least 8 chars
        - at least a special char
        - at least a min char
        - at least a maiusc char
        - at least a digit
    */
    public static boolean isStrong(String password) {
        boolean hasLetterMin = false;
        boolean hasLetterMaiusc = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
                char x = password.charAt(i);

                if ((x + "").matches("[^a-z A-Z0-9]")) { //is special char
                    hasSpecialChar = true;
                } else if (Character.isLetter(x)) {
                    if (Character.isLowerCase(x)) {
                        hasLetterMin = true;
                    } else if (Character.isUpperCase(x)) {
                        hasLetterMaiusc = true;
                    }
                } else if (Character.isDigit(x)) {

                    hasDigit = true;
                }

                if (hasLetterMin && hasDigit && hasLetterMaiusc && hasSpecialChar) {

                    break;
                }

            }
            return (hasLetterMin && hasDigit && hasLetterMaiusc && hasSpecialChar);
        } else {
            return false;
        }
    }

}

package it.unitn.wa.devisdm.lyricstrivia.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 *
 * @author devis
 */
public class TokenGenerator {
    /**
     * Generate an array of random bytes in order to have a salt and to store
     * the pwd in the db in a more secure way
     *
     * @return array of {@code byte[]} that will represent the salt for this
     * user
     * @throws NoSuchAlgorithmException if the selected algorithm for generating
     * salt is not found
     *
     * @author devis
     */
    public static String generateLogged() throws NoSuchAlgorithmException {
        return new RandomString().nextString();

    }

    /**
     * Generate an array of random bytes in order to have a salt and to store
     * the pwd in the db in a more secure way
     *
     * @return array of {@code byte[]} that will represent the salt for this
     * user
     * @throws NoSuchAlgorithmException if the selected algorithm for generating
     * salt is not found
     *
     * @author devis
     */
    public static byte[] generateSalt() throws NoSuchAlgorithmException {
        // VERY important to use SecureRandom instead of just Random

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        // Generate a 8 byte (64 bit) salt as recommended by RSA PKCS5
        byte[] salt = new byte[8];

        random.nextBytes(salt);

        return salt;

    }

    /**
     * Generate a token starting by salt and password of the user in a simple
     * way, but it has to allow to uniquely identify the user in order to
     * validate its account
     *
     * @param salt the array of {@code byte} representing the salt related to
     * the user
     * @param password the array of {@code byte} representing the hashed
     * password related to the user
     * @return {@code String} validation token
     *
     * @author devis
     */
    public static String generateValidationToken(byte[] salt, byte[] password) {
        String valToken1 = "";
        Long valToken2 = 0L;
        for (byte b : salt) {
            if (valToken1.length() < 3 && (Character.isLetterOrDigit((char) b))) {
                valToken1 += (char) b;
            }
            valToken2 += (int) b;
        }
        for (byte b : password) {
            if (valToken1.length() < 6 && (Character.isLetterOrDigit((char) b))) {
                valToken1 += (char) b;
            }
            valToken2 += (int) b;
        }
        return valToken1 + valToken2;

    }

    /**
     * Generate a token starting by salt and password of the user in a simple
     * way, but it has to allow to uniquely identify the user in order to
     * validate its account and allow him to restore a new pwd It has to be
     * valid just for a specific time fragment ~ 3h or something like it
     *
     * @param salt the array of {@code byte} representing the salt related to
     * the user
     * @param password the array of {@code byte} representing the hashed
     * password related to the user
     * @param h hour value
     * @param d day value
     * @return {@code String} validation token
     *
     * @author devis
     */
    public static String generateRecoveryToken(byte[] salt, byte[] password, int h, int d) {
        String valToken1 = "";
        double valToken2 = 0.0;
        for (byte b : salt) {
            if (valToken1.length() < 3 && (Character.isLetterOrDigit((char) (b + 10)))) {
                valToken1 += (char) (b + 10);
            }
            valToken2 += (int) b + 0.22 * h;
        }
        for (byte b : password) {
            if (valToken1.length() < 6 && (Character.isLetterOrDigit((char) (b + 10)))) {
                valToken1 += (char) (b + 10);
            }
            valToken2 += (int) b + 0.78 * d;
        }
        return valToken2 + valToken1 + d + "-" + h * 2;

    }

    /**
     * Generate a token starting by salt and password of the user in a simple
     * way, but it has to allow to uniquely identify the user in order to
     * validate its account and allow him to restore a new pwd It has to be
     * valid just for a specific time fragment ~ 3h or something like it
     *
     * @param salt the array of {@code byte} representing the salt related to
     * the user
     * @param password the array of {@code byte} representing the hashed
     * password related to the user
     * @param h hour value
     * @param d day value
     * @return {@code String} validation token
     *
     * @author devis
     */
    public static String generateRecoveryToken2(byte[] salt, byte[] password, int h, int d) {
        String valToken1 = "";
        double valToken2 = -100.0;
        for (byte b : password) {
            if (valToken1.length() < 3 && (Character.isLetterOrDigit((char) (b + 5)))) {
                valToken1 += (char) (b + 5);
            }
            valToken2 += (int) b + 0.72 * d;
        }
        for (byte b : salt) {
            if (valToken1.length() < 6 && (Character.isLetterOrDigit((char) (b + 5)))) {
                valToken1 += (char) (b + 5);
            }
            valToken2 += (int) b + 0.28 * h;
        }

        return d + valToken1 + (h * 2) + valToken2;

    }

    /**
     * Generate an array of bytes with PBKDF2WithHmacSHA256 starting from
     * password and salt passed as parameters
     *
     * @param password password in clear text inserted by the user
     * @param salt salt generated at registration for this user
     *
     * @return array of {@code byte[]} that will represent the hashed password
     * to store for this user
     * @throws NoSuchAlgorithmException if the selected algorithm for generating
     * salt+password hash is not found (PBKDF2WithHmacSHA256)
     * @throws InvalidKeySpecException if the params passed to KeySpec object
     * aren't valid
     *
     * @author devis
     */
    public static byte[] getEncryptedPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1024;
        int derivedKeyLength = 64;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength * 8);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        return f.generateSecret(spec).getEncoded();
    }
    
}

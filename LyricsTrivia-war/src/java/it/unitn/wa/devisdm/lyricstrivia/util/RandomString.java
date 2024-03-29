package it.unitn.wa.devisdm.lyricstrivia.util;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author devis
 */
public class RandomString {
    
    /**
     * Generate a random string.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx) {
            buf[idx] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf);
    }

    public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String LOWER = UPPER.toLowerCase();

    public static final String DIGITS = "0123456789";

    public static final String ALPHANUM = UPPER + LOWER + DIGITS;

    private final Random random;

    private final char[] symbols;

    private final char[] buf;
    
    /**
     * Create an alphanumeric string generator.
     * @param length
     * @param random
     * @param symbols
     */
    public RandomString(int length, Random random, String symbols) {
        if (length < 1) {
            throw new IllegalArgumentException();
        }
        if (symbols.length() < 2) {
            throw new IllegalArgumentException();
        }
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     * @param length
     * @param random
     */
    public RandomString(int length, Random random) {
        this(length, random, ALPHANUM);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     * @param length
     */
    public RandomString(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create cookies identifiers.
     */
    public RandomString() {
        this(50);
    }

}

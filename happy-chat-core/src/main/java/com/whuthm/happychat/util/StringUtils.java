package com.whuthm.happychat.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * A collection of utility methods for String objects.
 */
public class StringUtils {

    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String UTF8 = "UTF-8";
    public static final String USASCII = "US-ASCII";

    public static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();


    public static String encodeHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] toUtf8Bytes(String string) {
        try {
            return string.getBytes(StringUtils.UTF8);
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not supported by platform", e);
        }
    }

    private static final ThreadLocal<Random> randGen = new ThreadLocal<Random>() {
        @Override
        protected Random initialValue() {
            return new Random();
        }
    };

    private static final char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

    public static String insecureRandomString(int length) {
        return randomString(length, randGen.get());
    }

    private static final ThreadLocal<SecureRandom> SECURE_RANDOM = new ThreadLocal<SecureRandom>() {
        @Override
        protected SecureRandom initialValue() {
            return new SecureRandom();
        }
    };

    public static String randomString(final int length) {
        return randomString(length, SECURE_RANDOM.get());
    }

    private static String randomString(final int length, Random random) {
        if (length < 1) {
            return null;
        }

        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        char[] randomChars = new char[length];
        for (int i = 0; i < length; i++) {
            randomChars[i] = getPrintableChar(randomBytes[i]);
        }
        return new String(randomChars);
    }

    private static char getPrintableChar(byte indexByte) {
        assert (numbersAndLetters.length < Byte.MAX_VALUE * 2);

        // Convert indexByte as it where an unsigned byte by promoting it to int
        // and masking it with 0xff. Yields results from 0 - 254.
        int index = indexByte & 0xff;
        return numbersAndLetters[index % numbersAndLetters.length];
    }

    /**
     * Returns true if CharSequence is not null and is not empty, false otherwise.
     * Examples:
     *    isNotEmpty(null) - false
     *    isNotEmpty("") - false
     *    isNotEmpty(" ") - true
     *    isNotEmpty("empty") - true
     *
     * @param cs checked CharSequence
     * @return true if string is not null and is not empty, false otherwise
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isNullOrEmpty(cs);
    }

    /**
     * Returns true if the given CharSequence is null or empty.
     *
     * @param cs
     * @return true if the given CharSequence is null or empty
     */
    public static boolean isNullOrEmpty(CharSequence cs) {
        return cs == null || isEmpty(cs);
    }

    /**
     * Returns true if all given CharSequences are not empty.
     *
     * @param css the CharSequences to test.
     * @return true if all given CharSequences are not empty.
     */
    public static boolean isNotEmpty(CharSequence... css) {
        for (CharSequence cs : css) {
            if (StringUtils.isNullOrEmpty(cs)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if all given CharSequences are either null or empty.
     *
     * @param css the CharSequences to test.
     * @return true if all given CharSequences are null or empty.
     */
    public static boolean isNullOrEmpty(CharSequence... css) {
        for (CharSequence cs : css) {
            if (StringUtils.isNotEmpty(cs)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the given CharSequence is empty.
     *
     * @param cs
     * @return true if the given CharSequence is empty
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs.length() == 0;
    }

}

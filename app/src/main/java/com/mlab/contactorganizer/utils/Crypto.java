package com.mlab.contactorganizer.utils;


import android.util.Base64;

import java.io.UnsupportedEncodingException;

//https://stackoverflow.com/questions/15156811/encoding-decoding-of-data-between-php-java-for-android
public class Crypto {

    /**
     * // This should give the same results as in PHP
     byte[] encoded = Base64.encode(encrypted.getBytes("CP1252"), Base64.DEFAULT);
     String str = new String(encoded, "CP1252");
     * @param value
     * @return
     */
    public static String encode(String value) {
        byte[] encoded = new byte[0];

        try {
            encoded = Base64.encode(value.getBytes("UTF-8"), Base64.DEFAULT);

        return new String(encoded, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String decode(String value) {
        byte[] decoded = new byte[0];

        try {
            decoded = Base64.decode(value.getBytes("UTF-8"), Base64.DEFAULT);

            return new String(decoded, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }
}

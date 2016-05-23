package cn.dubby.what.utils;

import java.security.MessageDigest;

/**
 * Created by dubby on 16/5/1.
 */
public class MD5Utils {

    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                    .toUpperCase().substring(1, 3));
        }
        return sb.toString();
    }

    public static String encrypt(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("ISO8859-1")));
        } catch (Exception e) {
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("Hello world"));
    }
}
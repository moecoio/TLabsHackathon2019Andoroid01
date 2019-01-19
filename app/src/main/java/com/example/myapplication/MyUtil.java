package com.example.myapplication;

import android.util.Log;

import java.math.BigInteger;

public class MyUtil {
    public static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    }

    public static byte[] hexToBytes(String hexString) {
        //byte[] result = new BigInteger(hexString, 16).toByteArray();
        //return result;
        return hexStringToByteArray(hexString);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String stringToHexString(String str){
        return bytesToHex(str.getBytes());
    }

    public static void log(String msg){
        Log.e("=====!!!!MyApp!!!!=====", msg );
    }
}

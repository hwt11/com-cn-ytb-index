package com.cn.red.point.common;

import org.apache.commons.codec.binary.Hex;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Code {

    private static final String salt = "12345678901234567890123456789012";
    private static final String _salt = "68e96bc9c5281c14f7b3b652c1166942bffddd36";

    public static void main(String[] args){
        System.out.println(getSHA256Str("test"));
    }

    public static String getSHA256Str(String str){
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

    public static String getToken(String[] params){
        if(params == null || params.length == 0){
            return "";
        }
        Arrays.sort(params);
        String res = "";
        for(String param : params){
            res += param + "-";
        }
        return getSHA256Str(res + salt);
    }

    public static String _getToken(String[] params){
        if(params == null || params.length == 0){
            return "";
        }
        Arrays.sort(params);
        String res = "";
        for(String param : params){
            res += param + "-";
        }
        return EncodeMd5(res + _salt);
    }

    public static String EncodeMd5(String base) {
        String val = DigestUtils.md5DigestAsHex(base.getBytes());
        return val;
    }

    public static String EncoderByMd5(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }
}

package com.tj.practice.common.util;

import com.tj.practice.common.Constant;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

public class MD5Util {

	private static String byteArrayToHexString(byte b[]) {  
        StringBuffer resultSb = new StringBuffer();  
        for (int i = 0; i < b.length; i++)  
            resultSb.append(byteToHexString(b[i]));  
  
        return resultSb.toString();  
    }  
  
    private static String byteToHexString(byte b) {  
        int n = b;  
        if (n < 0)  
            n += 256;  
        int d1 = n / 16;  
        int d2 = n % 16;  
        return hexDigits[d1] + hexDigits[d2];  
    }  

    public static String MD5Encode(String origin) {  
    	return MD5Encode(origin,"");  
    }  
  
    public static String MD5Encode(String origin, String charsetname) {  
        String resultString = null;  
        try {  
            resultString = new String(origin);  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            if (StringUtils.isBlank(charsetname))  
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));  
            else  
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));  
        } catch (Exception e) {  
        	throw new RuntimeException("MD5签名错误",e);
        }  
        return resultString;  
    }  
    
    /**
     * 生成登录密码
     * @param username 账号
     * @param password 密码
     * @return
     */
    public static String encodeLoginPassword(String username,String password) {
    	if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
    		throw new RuntimeException("参数为空");
    	}
    	StringBuilder builder = new StringBuilder(Constant.PUBLIC_SALT)  //签名算法公共盐值(com.tj.practice.common.Constant)
    			.append(username).append("@").append(password).append("@").append(username)
    			;
    	return MD5Encode(builder.toString(), "UTF-8");
    }

    /**
     * 生成登录密码
     * @param username 账号
     * @param password 密码
     * @return
     */
    public static String encodePayPassword(String username,String password) {
    	if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
    		throw new RuntimeException("签名参数为空");
    	}
    	StringBuilder builder = new StringBuilder(Constant.PUBLIC_SALT)  //签名算法公共盐值(com.tj.practice.common.Constant)
    			.append(username).append("@").append(password).append("@").append(username)
    			.append(Constant.PUBLIC_SALT)
    			;
    	return MD5Encode(builder.toString(), "UTF-8");
    }

    public static String encode4Yb(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = md5.digest(password.getBytes("utf-8"));
            String passwordMD5 = byteArrayToHexString(byteArray);
            return passwordMD5;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return password;
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",  
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" }; 
}

package com.tj.practice.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;

public class Base64Util {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    public static void main(String[] args) throws Exception {

//        String url = "https://jxsr-oss1.antelopecloud.cn/files2/538378381/5b850a172017008d0410b6de?access_token=538378381_0_1566992195_d3769b88cadb2366b4e434c535174a54&key=%2Fiermu%2Fai%2F137898525675_538378381_1535445526128_1535445527765884139.jpg";
        // byte[] b =
        // ImgCutter.image2Base64("https://jxsr-oss1.antelopecloud.cn/files?obj_id=5b71489480004002041083a1&access_token=2147500034_3356491776_1565686804_7779becd44fd1f3d9ccd939139880cee");

//        String text = Base64Util.encodeToString(ImgCutter.image2Base64(url));
//        final byte[] b = decodeStr(text);
//
//        // Base64Util.encodeToString(ImgCutter.image2Base64("https://jxsr-oss1.antelopecloud.cn/files?obj_id=5b71489480004002041083a1&access_token=2147500034_3356491776_1565686804_7779becd44fd1f3d9ccd939139880cee"));
//
//        FileImageOutputStream imageOutput = new FileImageOutputStream(new File("d://222.jpg"));
//        imageOutput.write(b, 0, b.length);
//        imageOutput.close();
//
//        String s = "a123456";
//        System.out.println("原字符串：" + s);
//        String encryptString = encryptBASE64(s);
//        System.out.println("加密后：" + encryptString);
//        System.out.println("解密后：" + decryptBASE64(encryptString));

//        String url = "http://192.168.14.45:9091/staticResource/v1/storage/cycle/fid/1555293784995001999?signature=3784231c91ce13c3ed8a01ba434dadba&expire=1555466400000";
//        String base64UrlSafe = Base64Util.encryptBASE64UrlSafe(url);
//        System.out.println("base64UrlSafe:" + base64UrlSafe);
//        String url1 = Base64Util.decryptBASE64UrlSafe(base64UrlSafe);
//        System.out.println("url1:" + url1);
//        String urlImg = "http://192.168.101.14:8099" + "/image/v1/external/1111.jpg?url=" + base64UrlSafe;
//        System.out.println("urlImg:" + urlImg);

    }

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) {
        byte[] bt;
        try {
            bt = (new BASE64Decoder()).decodeBuffer(key);
            //如果出现乱码可以改成： String(bt, "utf-8")或 gbk
            return new String(bt, DEFAULT_CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] decode(String str) {
        //return decoder.decode(str);
        byte[] bt;
        try {
            bt = (new BASE64Decoder()).decodeBuffer(str);
            //如果出现乱码可以改成： String(bt, "utf-8")或 gbk
            return bt;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encode(byte[] bytes) {
        return (new BASE64Encoder()).encodeBuffer(bytes);
    }

    /**
     * BASE64加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    public static String encodeToString(byte[] bytea) {
        return encoder.encodeToString(bytea);
    }

    public static byte[] decodeStr(String str) {
        return decoder.decode(str);
    }

    /**
     * 生成Url安全的base64
     *
     * @param str
     * @return
     */
    public static String encryptBASE64UrlSafe(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
            s = s.replace("+", "-");
            s = s.replace("/", "_");
            s = s.replace("=", "");
            s = s.replaceAll("\r|\n", "");
        }
        return s;
    }

    /**
     * 将url安全的bas64解析出url
     *
     * @param safeBase64Str
     * @return
     */
    public static String decryptBASE64UrlSafe(String safeBase64Str) {
        String base64Str = safeBase64Str.replace('-', '+');
        base64Str = base64Str.replace('_', '/');
        int mod4 = base64Str.length() % 4;
        if (mod4 > 0) {
            base64Str = base64Str + "====".substring(mod4);
        }
        return Base64Util.decryptBASE64(base64Str);
    }
}

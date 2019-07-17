/*
 * 
 * 
 * 
 */
package com.tj.practice.common.util.RSA;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * Utils - RSA加密解密
 * 
 * 
 * 
 */
@Slf4j
public final class RSAUtils {

	/** 安全服务提供者 */
//	private static final Provider PROVIDER = new BouncyCastleProvider();

	/** 密钥大小 */
	private static final int KEY_SIZE = 1024;

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    public static final String PKCS1Padding = "RSA/ECB/PKCS1Padding";

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHM_MD5 = "MD5withRSA";
    public static final String SIGN_ALGORITHM_SHA1 = "SHA1WithRSA";


	/**
//	 * 不可实例化
	 */
	private RSAUtils() {
	}

	/**
	 * 生成密钥对
	 * 
	 * @return 密钥对
	 */
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static RSAPublicKey getPublicKey(KeyPair keyPair) {
		return (RSAPublicKey) keyPair.getPublic();
	}

	public static PrivateKey getPrivateKey(KeyPair keyPair) {
		return keyPair.getPrivate();
	}

	/**
	 * 加密
	 * 
	 * @param publicKey 公钥
	 * @param data 数据
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		Assert.notNull(publicKey,"公钥为Null");
		Assert.notNull(data,"数据为Null");
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param publicKey 公钥
	 * @param text 字符串
	 * @return Base64编码字符串
	 * @throws UnsupportedEncodingException 
	 */
	public static String encrypt(PublicKey publicKey, String text) throws UnsupportedEncodingException {
		Assert.notNull(publicKey,"公钥为Null");
		Assert.notNull(text,"数据为Null");
		byte[] data = encrypt(publicKey, text.getBytes("UTF-8"));
		return data != null ? Base64.encodeBase64String(data) : null;
	}

	/**
	 * 解密
	 * 
	 * @param privateKey 私钥
	 * @param data 数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		Assert.notNull(privateKey,"私钥为Null");
		Assert.notNull(data,"数据为Null");
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			log.error("RSA解密出错：",e);
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param privateKey 私钥
	 * @param text Base64编码字符串
	 * @return 解密后的数据
	 * @throws UnsupportedEncodingException 
	 */
	public static String decrypt(PrivateKey privateKey, String text) throws UnsupportedEncodingException {
		Assert.notNull(privateKey,"私钥为Null");
		Assert.notNull(text,"数据为Null");
		byte[] data = decrypt(privateKey, Base64.decodeBase64(text));
		return data != null ? new String(data,"utf-8") : null;
	}


    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign, String signAlgorithm)
            throws Exception {
        byte[] keyBytes = java.util.Base64.getDecoder().decode((publicKey));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(java.util.Base64.getDecoder().decode(sign));
    }

}
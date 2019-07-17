	
package com.tj.practice.common.util.RSA;

import com.tj.practice.common.util.MD5Util;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Calendar;

/**
 * 验证码、邀请码生成器
 * 
 * @author Bear.Xiong
 */
public class RandomUtils {
	/**
	 * 生成4位随机数字
	 * 
	 */
	public static String next4Num() {
		return String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
	}

	/**
	 * 生成5位随机数字
	 *
	 */
	public static String next5Num() { return String.valueOf((int) ((Math.random() * 9 + 1) * 10000)); }
	/**
	 * 生成6位随机密码
	 * 
	 */
	public static String randomPassword() {
		return RandomStringUtils.random(6, true, true);
	}

	/**
	 * 生成随机token，长度32位
	 * @return
	 */
	public static String nextToken() {
		Calendar c = Calendar.getInstance();
		String time = RandomStringUtils.random(6, true, true);
		String random4num = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
		return MD5Util.MD5Encode(time + c.getTimeInMillis() + random4num, "UTF-8");
	}
}
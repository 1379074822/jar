package com.mall.tools;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 散列算法 生成数据的摘要信息，是一种不可逆的算法，一般适合存储密码之类的数据，
 */
public class MD5Utils {
	private static final String SALT = "1qazxsw2";//干扰数据 盐 防破解

	private static final String ALGORITH_NAME = "md5";//散列算法类型为MD5

	private static final int HASH_ITERATIONS = 2;//hash的次数

	public static String encrypt(String pswd) {
		String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(SALT), HASH_ITERATIONS).toHex();
		return newPassword;
	}

	public static String encrypt(String username, String pswd) {
		String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(username + SALT),
				HASH_ITERATIONS).toHex();
		return newPassword;
	}
	public static void main(String[] args) {
		String name = "8000115100";
		String pwd = "123456";
		System.out.println( "uname- " + name + " , pwd- " +  pwd + " ,after encrypt :"
				+ MD5Utils.encrypt(name, pwd));
	}
}

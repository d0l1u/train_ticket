package com.l9e.util;

import java.util.UUID;

/**
 * 主键生成器
 * @author licheng
 *
 */
public class IDGenerator {
	
	public static void main(String[] args) {
		System.out.println(generateID("TC"));
	}

	/**
	 * 根据前缀生成主键
	 * @param prex
	 * @return
	 */
	public static String generateID(String prex) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(UUID.randomUUID().toString())
			.append(Math.random())
			.append(System.currentTimeMillis());
		
		String md5_16 = MD5Util.md5_16(builder.toString(), "UTF-8");
		builder.setLength(0);
		builder.append(prex)
			.append(md5_16);
		return builder.toString();
	}
}

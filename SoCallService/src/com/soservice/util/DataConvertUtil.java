package com.soservice.util;

/**
 * 将基本数据类型转换为byte数组，以及反向转换的方法 只涉及转换操作，对于参数没有进行校验 适用范围：RMS操作、网络数据传辄1�7
 */
public class DataConvertUtil {
	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。
	 */
	public static byte[] intToBytes(int value) {
		byte[] src = new byte[4];
		src[0] = (byte) ((value >> 24) & 0xFF);
		src[1] = (byte) ((value >> 16) & 0xFF);
		src[2] = (byte) ((value >> 8) & 0xFF);
		src[3] = (byte) (value & 0xFF);
		return src;
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。
	 */
	public static int bytesToInt(byte[] src) {
		int value;
		value = (int) (((src[0] & 0xFF) << 24) | ((src[1] & 0xFF) << 16)
				| ((src[2] & 0xFF) << 8) | (src[3] & 0xFF));
		return value;
	}

	/**
	 * main函数
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		// 6552
		byte[] b = new byte[] { 0, 0, 25, -104 };
		byte[] b1 = intToBytes(6552);
		System.out.println(b1);
		System.out.println(bytesToInt(b));

	}
}

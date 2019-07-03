package com.l9e.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Demo {

	/**
	 * @param redirectUrl 19
	 * @param key
	 * @param mobileNo
	 * @return
	 */
	public String getRedirectUrl(String redirectUrl, String key, String mobileNo) {
		String askId = getAskId();
		return redirectUrl + ((redirectUrl.indexOf("?") == -1) ? "?" : "&")
				+ "askId=" + askId + "&mobileNo=" + mobileNo + "&digest="
				+ EncryptStringByMD5(askId + mobileNo + key);

	}

	private String EncryptStringByMD5(String strSource) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] strTemp = strSource.getBytes();
			java.security.MessageDigest mdTemp = java.security.MessageDigest
					.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	private String getAskId() {
		String rand = Integer.toString(new Random().nextInt(999999));
		rand = "000000".substring(rand.length()) + rand;
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + rand;
	}

	public static void main(String[] args) {
		System.out.println(new Demo().getRedirectUrl("http://192.168.12.98:8080/login/trainIndex.jhtml", "talkweb@19e20140126",
				"13716579764"));
		
		System.out.println(String.valueOf((Double.parseDouble("0.500") * 100)));
		
		System.out.println(String.valueOf((Double.parseDouble("1.500") * 100)).split("\\.")[0]);
	}

}

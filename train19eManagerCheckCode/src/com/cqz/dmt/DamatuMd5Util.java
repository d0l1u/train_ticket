package com.cqz.dmt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DamatuMd5Util {
	private static String  byteArray2HexString(byte [] data) {
		StringBuilder sb = new StringBuilder();
	for (byte b : data) {
			String s = Integer.toHexString(b & 0xff);
			if (s.length() == 1) {
				sb.append("0" + s);
			} else {
				sb.append(s);
			}
		}
		return sb.toString();
	}

	//用“\n”连接预授权信息、用户名、用户密码MD5值，使用压缩的软件KEY对其进行DES加密，扩展成16进制字串
	public static String jm(String preauth,String username,String password,String key){
		String pwd_md5_str=myMd5(password,"UTF-8");
		String enc_data_str = preauth + "\n" + username + "\n"+ pwd_md5_str; 
		System.out.println(enc_data_str);
		DES des;
		try {
			des = new DES(key);
			/*System.out.println("加密前的字符："+enc_data_str);  
			System.out.println("加密后的字符："+des.encrypt(enc_data_str));  */
			return des.encrypt(enc_data_str);
		} catch (Exception e) {
			e.printStackTrace();
		}//自定义密钥   
		return null;
	}
	

	
	public static String myMd5(String md5_str,String charset){
		try {
			MessageDigest messDigest=MessageDigest.getInstance("MD5");
			messDigest.update(md5_str.getBytes(charset));
			byte[] digBytes=messDigest.digest();
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<digBytes.length;i++){
				int num=digBytes[i];
				if(num<0) num+=256;
				/*sb.append(INDEX_CHAR[num >>> 4]);
				sb.append(INDEX_CHAR[num % 16]);*/
				sb.append(Integer.toHexString(num>>>4));
				sb.append(Integer.toHexString(num%16));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		System.out.println(DamatuMd5Util.jm("1234567890abcdef1234567890abcdef", "user", "test", "9503ce045ad14d83ea876ab578bd3184"));;
	}
}

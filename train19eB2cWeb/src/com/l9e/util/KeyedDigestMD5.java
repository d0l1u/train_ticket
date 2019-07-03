package com.l9e.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KeyedDigestMD5 {
    
	public static byte[] getKeyedDigest(byte[] buffer, byte[] key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer);
            return md5.digest(key);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
	
		
	public static String getKeyedDigest(String strSrc, String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strSrc.getBytes("GBK"));
            
            String result="";
            byte[] temp;
            temp=md5.digest(key.getBytes("GBK"));
    		for (int i=0; i<temp.length; i++){
    			result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
    		}
    		
    		return result;
    		
        } catch (NoSuchAlgorithmException e) {
        	
        	e.printStackTrace();
        	
        }catch(Exception e)
        {
          e.printStackTrace();
        }
        return null;
    }
	public static String getKeyedDigestUTF8(String strSrc, String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strSrc.getBytes("UTF8"));
            
            String result="";
            byte[] temp;
            temp=md5.digest(key.getBytes("UTF8"));
    		for (int i=0; i<temp.length; i++){
    			result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
    		}
    		
    		return result;
    		
        } catch (NoSuchAlgorithmException e) {
        	
        	e.printStackTrace();
        	
        }catch(Exception e)
        {
          e.printStackTrace();
        }
        return null;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuffer signData=new StringBuffer();		
		signData.append("extend1="+"nihao")
				.append("&extend2="+"wohao")
				.append("&mxdate="+"2014-05-07 15:01:30")
				.append("&mxorderid="+"pay201402155893458")
				.append("&orderid="+"pay2014568745842")
				.append("&status="+"11");
		System.out.println(KeyedDigestMD5.getKeyedDigest(signData.toString(), "nihao"));
		
		String s = "version=2.00&mxResq=TK1501041113341004&status=0&retCode=00000&oriPayOrderId=BC1501041100351001&finishTime=20150104113516&amount=355&refOrderId=RO15010411172700001062&fee=0.00";
		System.out.println(getKeyedDigestUTF8(s,"19pay.com.cn"));
	}
}


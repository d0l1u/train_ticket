package com.cqz.utils;

import org.apache.log4j.Logger;

/*
 String CMyFile::EntryPassEntryPass(String strTxt, String strKey)
 {
 String strChars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-=+";
 int  nh=GetRandN(strChars);
 char  ch = strChars.GetAt(nh);
 String strCh;
 strCh.Format("%c",ch);
 String strMKey=strKey;
 strMKey+=strCh;
 String strRMkey=strMKey.Mid(nh%8,nh%8+7);
 String strBase64Code=m_Base64.Encode(strTxt,strTxt.GetLength());

 String strTmp;
 String   temp;
 int i=0,j=0,k=0;
 for (i=0;i<strBase64Code.GetLength();i++)
 {
 if (k==strRMkey.GetLength())
 {
 k=0;
 }
 int  x1=nh+strChars.Find(strBase64Code.GetAt(i),0);
 int  x2=(int)(strRMkey.GetAt(k++));
 j=(x1+x2)%64;
 char  b=strChars.GetAt(j);
 String strB;
 strB.Format("%c",b);
 temp+=strB;
 }
 String strR=strCh;
 strR+=temp;
 return strR;
 }


 传进来的参数，txt,key  文本
 1. 字符串strchars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-=+"; 
 2, 随机获取�?��字符,随机的位置nh,对应字符ch
 3，strkey+ch  合成字符串mkey
 4, 获取rmkey=mkey.Mid(nh%8,nh%8+7);
 5, 对txt进行base64加密得到字符串strBase64Code

 循环i=0;i<strBase64Code.GetLength;i++
 6，x1=nh+strChars.Find(strBase64Code.GetAt(i),0);
 7�?



 */
public class CodeGenerator {
	public final static String strChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-=+";

	
	private static final Logger logger = Logger.getLogger(CodeGenerator.class);
	public static void main(String[] args) {
		// System.out.println(entryPassEntryPass("sfsdfs", "1234567890"));
		for(int i =0 ;i <200;i++)
		System.out.println(entryPassEntryPass(GetMacAddress.getMacAddress(), GetMacAddress.getMacAddress()));
	}

	public static String getKey() {
		//logger.info("获取MAC地址2:GetMacAddress.getMacAddress():"+GetMacAddress.getUnixMACAddress());
		return entryPassEntryPass(GetMacAddress.getMacAddress(), GetMacAddress.getMacAddress());
		//return entryPassEntryPass(GetMacAddress.getUnixMACAddress(), GetMacAddress.getUnixMACAddress());
	}

	public static String getStrMac() {
		//logger.info("获取MAC地址2:GetMacAddress.getMacAddress():"+GetMacAddress.getUnixMACAddress());
		return new sun.misc.BASE64Encoder().encodeBuffer(GetMacAddress.getMacAddress().getBytes());
		//return new sun.misc.BASE64Encoder().encodeBuffer(GetMacAddress.getUnixMACAddress().getBytes());
	}

	public static String entryPassEntryPass(String strTxt, String strKey) {
		int nh = (int) (Math.random() * (strChars.length()));
		char ch = strChars.charAt(nh);

		String strCh = String.format("%c", ch);
		String strMKey = strKey;
		strMKey += strCh;
		int s = nh % 8;
		int e = nh % 8 + 7;
		String strRMkey = "";
		strRMkey = strMKey.substring(s, (strMKey.length() > s + e) ? s + e : strMKey.length());

		String strBase64Code = new sun.misc.BASE64Encoder().encodeBuffer(strTxt.getBytes());

		String temp = "";
		int i = 0, j = 0, k = 0;

		for (i = 0; i < strBase64Code.length(); i++) {
			if (k == strRMkey.length()) {
				k = 0;
			}
			int x1 = nh + strChars.indexOf(strBase64Code.charAt(i), 0);
			int x2 = (int) (strRMkey.charAt(k++));
			j = (x1 + x2) % 64;
			char b = strChars.charAt(j);
			String strB = String.format("%c", b);
			temp += strB;
		}
		String strR = strCh;
		strR += temp;
		return strR;

	}
	
	
	

}

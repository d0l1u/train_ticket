package com.nineteen.util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jna.Library;
import com.sun.jna.Native;
public class CaptchaOCRUtil {
	//51OCR是表示51ocr.dll
	private static String DLLPATH = "D:\\yzm\\51OCR";

	private static Log log = LogFactory.getLog(CaptchaOCRUtil.class);

	public interface OCR extends Library {
		OCR INSTANCE = (OCR) Native.loadLibrary(DLLPATH, OCR.class);
		public int www_51ocr_com_InitOCR(String templatefile);
		public int www_51ocr_com_RECOG_2(byte[] imagebuf, int size, int type, byte[] text);
		public String www_51ocr_com_RECOG_3(String out,int size,String in);
	}
	
	public static boolean init() {
		int uid = 0;
		try {
			File directory = new File("D:\\yzm\\");
			String currentPath = directory.getCanonicalPath();
			uid = OCR.INSTANCE.www_51ocr_com_InitOCR(currentPath+ "\\51ocr.Templates");
		} catch (Exception ex) {
			log.error("failed to load the 51ocr.Templates.", ex);
		}
		if (uid == 0) {
			log.info("load the 51ocr.Templates successfully.");
			return true;
		} else {
			log.error("failed to load the 51ocr.Templates, return code" + uid);
			return false;
		}
	}

	public static String getToken(String out,int size,String in){
		
		String res = OCR.INSTANCE.www_51ocr_com_RECOG_3(out, size, in);
		System.err.println("res:"+res);
		return "";
	}
	public static String DecodeByByBytes(byte[] captchaByte) {
		byte[] byteResult = new byte[12];

		//这里3表示jpg文件, 如果是bmp, 用1, gif用2, png用4
		int cid = OCR.INSTANCE.www_51ocr_com_RECOG_2(captchaByte,captchaByte.length, 3, byteResult);
		String strResult = "";
		try {
			if (cid == 0) {
				strResult = new String(byteResult, "UTF-8").trim();
				log.info("captcha is ocrrd successfully, captcha code:" + strResult);
			} else {
				log.error("failed to ocr captcha , return code:" + cid);
			}
		} catch (Exception ex) {
			log.error("failed to ocr captcha , return code:" + cid, ex);
		}
		return strResult;
	}
	
	public static byte[] image2byte(String path){
	    byte[] data = null;
	    FileImageInputStream input = null;
	    try {
	      input = new FileImageInputStream(new File(path));
	      ByteArrayOutputStream output = new ByteArrayOutputStream();
	      byte[] buf = new byte[1024];
	      int numBytesRead = 0;
	      while ((numBytesRead = input.read(buf)) != -1) {
	      output.write(buf, 0, numBytesRead);
	      }
	      data = output.toByteArray();
	      output.close();
	      input.close();
	    }
	    catch (FileNotFoundException ex1) {
	      ex1.printStackTrace();
	    }
	    catch (IOException ex1) {
	      ex1.printStackTrace();
	    }
	    return data;
	  }
	public static String ocrUtil(String img_url){
		init();
		byte[] bytImage = image2byte(img_url);
		String checkcode = DecodeByByBytes(bytImage);
		log.error("result code:" + checkcode);
		return checkcode;
	}
	public static void main(String args[]){
		//init()只用执行一次
		init();
		byte[] bytImage = image2byte("D:\\testyzm.jpg");
		String checkcode = DecodeByByBytes(bytImage);
		log.info("result code:" + checkcode);
		
	}
}

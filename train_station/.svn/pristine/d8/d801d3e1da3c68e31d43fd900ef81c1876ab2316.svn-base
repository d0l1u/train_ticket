package com.l9e.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

public class FileUtil {

	private static Logger logger = Logger.getLogger(FileUtil.class);

	private static File openFile(String filePath) {
		File file;
		file = new File(filePath);
		return file;
	}

	public static boolean existsFile(String filePath) {
		File file;
		file = new File(filePath);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static String readFile(String filePath) {
		File file = openFile(filePath);
		FileInputStream fis = null;
		StringBuffer sb = new StringBuffer();

		try {
			fis = new FileInputStream(file);
			byte[] bytes = new byte[fis.available()];
			int len = fis.read(bytes);
			sb.append(new String(bytes, "UTF-8"));
			if (fis != null) {
				fis.close();
			}

		} catch (FileNotFoundException e) {
			logger.error("读取文件异常" + e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return sb.toString();
	}

	public static void writeFile(String filePath, String word, String url_encode) {
		try {
			FileOutputStream fos = new FileOutputStream(filePath); 
	        OutputStreamWriter osw = new OutputStreamWriter(fos, url_encode); 
	        osw.write(word);
			if (osw != null) {
				osw.close();
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error("文件名" + filePath + "错误信息：" + e.getMessage());
		}
	}

	public static OutputStream openOutputStream(String filePath) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(openFile(filePath));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}
		return os;
	}

	public static InputStream openInputStream(String filePath) {
		InputStream is = null;
		try {
			is = new FileInputStream(openFile(filePath));
		} catch (FileNotFoundException e) {
			logger.error("��ȡ�ļ�ʱ�������" + e.getMessage());
		}
		return is;
	}

	public static void writeWord(OutputStream os, String word) {
		try {
			os.write(word.getBytes());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	public static void closeOutputStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
}

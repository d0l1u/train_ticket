package com.cqz.tc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Encoder;
@Deprecated
public class TcBase64 {

	public String file2Base64(File target) {
		
		if(target == null || !target.exists())
			return null;
		BASE64Encoder encoder = new BASE64Encoder();
		
		InputStream in = null;
		String result = null;
		
		try {
			in = new FileInputStream(target);
			byte[] buff = new byte[(int)target.length()];
			in.read(buff);
			result = encoder.encode(buff);
			
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(in != null) 
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

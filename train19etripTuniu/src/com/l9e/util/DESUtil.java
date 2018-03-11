package com.l9e.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import net.sf.json.JSONObject;

public class DESUtil {
	private final static String ALGORITHM = "DES";	
	/**
	 * 加密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return	  返回加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		return cipher.doFinal(src);
	}
	
	/**
	 * 解密
	 * @param src	数据源
	 * @param key	密钥，长度必须是8的倍数
	 * @return		返回解密后的原始数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		return cipher.doFinal(src);
	}
	public static String generateSign(String jsonStr, String pwd) {

        String sign = null;
        if (jsonStr == null || "".equals(jsonStr)) {
            throw new RuntimeException("参数为空");
        } else {
            // 按照key值首字母排序，并去掉value为空的
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            List<String> keyList = new ArrayList<String>();
            @SuppressWarnings("unchecked")
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (jsonObject.getString(key) != null && !"".equals(jsonObject.getString(key))
                        && !"null".equals(jsonObject.getString(key))) {
                    keyList.add(key);
                }

            }
            // 排序
            // Collections.sort(keyList);

            String temp = null;
            for (int i = 0; i < keyList.size(); i++) {
                for (int j = i + 1; j < keyList.size(); j++) {
                    if (keyList.get(i).charAt(0) > keyList.get(j).charAt(0)) {
                        temp = keyList.get(i);
                        keyList.set(i, keyList.get(j));
                        keyList.set(j, temp);
                    }
                }
            }

            // 2. 将参数名、参数值依次拼接在一起
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < keyList.size(); i++) {
                sb.append(keyList.get(i));
                sb.append(jsonObject.get(keyList.get(i)));
            }

            // 3. 在开头和结尾分别加上密钥
            sign = pwd + sb.toString() + pwd;

            sign = MD5Util.md5(sign, "UTF-8").toUpperCase();
         

        }
        return sign;
    }
}

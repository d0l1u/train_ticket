package com.l9e.train.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DSA�ӽ��ܹ���
 * @author lijie
 *
 */
public class DSAHelper {

	private Logger logger = Logger.getLogger(DSAHelper.class);
	private BASE64Encoder encoder = new BASE64Encoder();
	private BASE64Decoder decoder = new BASE64Decoder();
	private PrivateKey prikey = null;
	private PublicKey pubkey = null;
	
	/**
	 * ����˽Կ�ļ�
	 * @param keyPath
	 */
	public void setPriKey(String keyPath){
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(keyPath));
			prikey = (PrivateKey) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			logger.error("��ȡ˽Կ�ļ�ʧ�ܣ�");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("��ȡ˽Կ�ļ�ʧ�ܣ�");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.error("��ȡ˽Կ�ļ�ʧ�ܣ�");
			e.printStackTrace();
		}
	}
	/**
	 * ���빫Կ�ļ�
	 * @param keyPath
	 */
	public void setPubKey(String keyPath){
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(keyPath));
			pubkey = (PublicKey) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			logger.error("��ȡ��Կ�ļ�ʧ�ܣ�");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("��ȡ��Կ�ļ�ʧ�ܣ�");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.error("��ȡ��Կ�ļ�ʧ�ܣ�");
			e.printStackTrace();
		}
	}
	
	/**
	 * ����DSA���ܹ��
	 * @param param
	 * @return
	 */
	public String dsaSign(String param) {
		String ret = null;
		logger.info("DSA����Դ����" + param);
			try {
				// �����ǩ��
				Signature signature = Signature.getInstance("DSA");
				signature.initSign(prikey);
				signature.update(param.getBytes());
				byte[] signed = signature.sign();
				ret = encoder.encode(signed);
				logger.info("DSA���ܽ��" + ret);
			} catch (InvalidKeyException e) {
				logger.error("DSA����ʧ�ܣ���֧�ֵ���Կ����");
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				logger.error("DSA����ʧ�ܣ�");
				e.printStackTrace();
			} catch (SignatureException e) {
				logger.error("DSA����ʧ�ܣ�");
				e.printStackTrace();
			} 
		return ret;
	}

	/**
	 * ����DSA��֤���
	 * @param signStr
	 * @param param
	 */
	public boolean dsaCheck(String signStr,String param) {
		logger.info("����ĶԷ����ܽ��" + signStr);
		try {
			// �Եõ��ļ��ܴ�����BASE64����
			byte[] signed = decoder.decodeBuffer(signStr);;
			// ��֤��Կ��
			Signature signCheck = Signature.getInstance("DSA");
			signCheck.initVerify(pubkey);
			signCheck.update(param.getBytes());
			if (signCheck.verify(signed)) {
				logger.error("DSA������֤ͨ��");
				return true;
			} else {
				logger.error("DSA������֤ʧ�ܣ�ǩ��ƥ�䣡");
				return false;
			}
		} catch (InvalidKeyException e) {
			logger.error("DSA������֤ʧ�ܣ���֧�ֵ���Կ����");
		} catch (FileNotFoundException e) {
			logger.error("DSA������֤ʧ�ܣ�û���ҵ���Կ�ļ�");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			logger.error("DSA������֤ʧ�ܣ�");
			e.printStackTrace();
		} catch (SignatureException e) {
			logger.error("�����BASE64���ܴ���");
		} catch (IOException e) {
			logger.error("DSA������֤ʧ�ܣ�IO�쳣");
			e.printStackTrace();
		}
		return false;
	}
	
}

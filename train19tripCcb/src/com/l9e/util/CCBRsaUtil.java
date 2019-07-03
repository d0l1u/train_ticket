package com.l9e.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;


import javax.crypto.Cipher;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class CCBRsaUtil{
	 
   private static final Logger log = Logger.getLogger(CCBRsaUtil.class);
	
   public static CCBRsaUtil  instance = null;
   
   
   public static synchronized CCBRsaUtil getInstance()
   {
       if(instance == null){
    	   instance = new CCBRsaUtil(); 
       }
       return instance;
   }
   
	public CCBRsaUtil() {
		initSecurity();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 从证书文件中获取私钥
	 * @param corpName
	 * @return
	 * @throws Exception
	 */
    public static KeyPair getKeyPair(String corpName) throws Exception {   
        
    	String file = corpName;
      	log.info(file);
    	   
    	KeyPair kp = null;
    	FileInputStream fis = null;
    	ObjectInputStream oos=null;
    	try
    	{
    	 fis = new FileInputStream(file);
        
         oos = new ObjectInputStream(fis);   
         kp = (KeyPair) oos.readObject();   
    	}catch(Exception e)
    	{
    		 log.info(" get pairkey error ");
    		 e.printStackTrace();
    	}finally
    	{
    		if( null != oos)
   		 {
   			 oos.close();
   		 }
   		 if(null != fis)
   		 {
   			fis.close(); 
   		 }
    		
    	}

        return kp;   
    }   
  

    /**
     * 根据密钥串，产生公钥
     * @param modulus
     * @param publicExponent
     * @return
     * @throws Exception
     */
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus,   
            byte[] publicExponent) throws Exception {   
        KeyFactory keyFac = null;   
        try {   
            keyFac = KeyFactory.getInstance("RSA",   
                    "BC");   
        } catch (NoSuchAlgorithmException ex) {   
            throw new Exception(ex.getMessage());   
        }   
  
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(   
                modulus), new BigInteger(publicExponent));   
        try {   
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);   
        } catch (InvalidKeySpecException ex) {   
            throw new Exception(ex.getMessage());   
        }   
    }   
  
 
    /**
     * 根据密钥串，产生私钥
     * @param modulus
     * @param privateExponent
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus,   
            byte[] privateExponent) throws Exception {   
        KeyFactory keyFac = null;   
        try {   
            keyFac = KeyFactory.getInstance("RSA",   
                    "BC");   
        } catch (NoSuchAlgorithmException ex) {   
            throw new Exception(ex.getMessage());   
        }   
  
        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(   
                modulus), new BigInteger(privateExponent));   
        try {   
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);   
        } catch (InvalidKeySpecException ex) {   
            throw new Exception(ex.getMessage());   
        }   
    }   
  

    public static byte[] encryptBySiteIdEx(String siteId, String data) throws Exception {   

        	RSAPublicKey rsap1 = (RSAPublicKey) getKeyPair(siteId).getPublic(); 
        	
        	return encrypt(rsap1, data.getBytes()); 
    }

    
    
    /**
     * 用公钥加密数据
     * @param pk
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {   
    	log.info("start to encrypt infomation ");
    	long startTime = System.currentTimeMillis(); 
        try {   
            Cipher cipher = Cipher.getInstance("RSA",   
                    "BC");   
            cipher.init(Cipher.ENCRYPT_MODE, pk);   
            int blockSize = cipher.getBlockSize();//key_size=1024   
            int outputSize = cipher.getOutputSize(data.length);// 
            int leavedSize = data.length % blockSize;   
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1  
                    : data.length / blockSize;   
            byte[] raw = new byte[outputSize * blocksSize];   
            int i = 0;   
            while (data.length - i * blockSize > 0) {   
                if (data.length - i * blockSize > blockSize)   
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i   
                            * outputSize);   
                else  
                    cipher.doFinal(data, i * blockSize, data.length - i   
                            * blockSize, raw, i * outputSize);   
       
                i++;   
            }   
            System.out.println(System.currentTimeMillis() - startTime); 
            return raw;   
        } catch (Exception e) {   
            throw new Exception(e.getMessage());   
        }   
        
     
    }   
  
    
    /**
     * 用私钥加密数据
     * @param pk
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptVers(PrivateKey pk, byte[] data) throws Exception {   
        try {   
            Cipher cipher = Cipher.getInstance("RSA",   
                    "BC");   
            cipher.init(Cipher.ENCRYPT_MODE, pk);   
            int blockSize = cipher.getBlockSize();//key_size=1024   
            int outputSize = cipher.getOutputSize(data.length);// 
            int leavedSize = data.length % blockSize;   
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1  
                    : data.length / blockSize;   
            byte[] raw = new byte[outputSize * blocksSize];   
            int i = 0;   
            while (data.length - i * blockSize > 0) {   
                if (data.length - i * blockSize > blockSize)   
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i   
                            * outputSize);   
                else  
                    cipher.doFinal(data, i * blockSize, data.length - i   
                            * blockSize, raw, i * outputSize);   
       
                i++;   
            }   
            return raw;   
        } catch (Exception e) {   
            throw new Exception(e.getMessage());   
        }   
    }   

    /**
     * 用私钥解密公钥加密的数据
     * @param pk
     * @param raw
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(PrivateKey pk, byte[] raw) throws Exception {   
        try {   
            Cipher cipher = Cipher.getInstance("RSA",   
                    "BC");   
            cipher.init(cipher.DECRYPT_MODE, pk);   
            int blockSize = cipher.getBlockSize();   
            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);   
            int j = 0;   
  
            while (raw.length - j * blockSize > 0) {   
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));   
                j++;   
            }   
            return bout.toByteArray();   
        } catch (Exception e) {   
            throw new Exception(e.getMessage());   
        }   
    }   
    
    /**
     * 用公钥解密私钥加密的数据
     * @param pk
     * @param raw
     * @return
     * @throws Exception
     */
    public static byte[] decryptVers(PublicKey pk, byte[] raw) throws Exception {   
        try {   
            Cipher cipher = Cipher.getInstance("RSA","BC");   
            cipher.init(cipher.DECRYPT_MODE, pk);   
            int blockSize = cipher.getBlockSize();   
            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);   
            int j = 0;   
  
            while (raw.length - j * blockSize > 0) {   
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));   
                j++;   
            }   
            return bout.toByteArray();   
        } catch (Exception e) {   
            throw new Exception(e.getMessage());   
        }   
    }   
    
    /**
    * Transform the specified byte into a Hex String form.
    */
    public static final String bytesToHexStr(byte[] bcd) {
    StringBuffer s = new StringBuffer(bcd.length * 2);

    for (int i = 0; i < bcd.length; i++) {
    s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
    s.append(bcdLookup[bcd[i] & 0x0f]);
    }

    return s.toString();
    }

    /**
    * Transform the specified Hex String into a byte array.
    */
    public static final byte[] hexStrToBytes(String s) {
    byte[] bytes;

    bytes = new byte[s.length() / 2];

    for (int i = 0; i < bytes.length; i++) {
    bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
    16);
    }

    return bytes;
    }

    private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5',
    '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
    
    public static String getParameterValue(String strName, String [] temp)
    {
 	   for(int i=0;i<temp.length ;i++)
 	   {
 		   String tempString = temp[i];
 		   if(tempString.indexOf(strName) >= 0)
 		   {
 			   return tempString.substring(tempString.indexOf("=") +1);
 		   }
 	   }
 	   return null;
    }
    
    

	private static BouncyCastleProvider bcp = null;
    
    private static BouncyCastleProvider getProvider() {
		   if (bcp == null) {
		    try {
		    	bcp = new BouncyCastleProvider();
		    } catch (Exception e) {
		     e.printStackTrace();
		    }
		   }
		   return bcp;
		}
    
	    /**
	     * 初始化 加密的提供类
	     */
	    public static void initSecurity()
	    {
	    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	    }
	    
   
	  public static void main(String[] args) throws Exception {   
			  
			// 解密
			  String corpName="D:\\workspace\\TestSSH\\src\\ricki\\cheung\\test\\B2Ctest.cert";
			  RSAPrivateKey privateKey = (RSAPrivateKey) CCBRsaUtil.getInstance().getKeyPair(corpName).getPrivate();
			  RSAPublicKey  publicKey = (RSAPublicKey) CCBRsaUtil.getInstance().getKeyPair(corpName).getPublic();
			  
			  String test =  "userId=1234&itemNo=100&cityName=bjh1212";
			  byte[] en_test = encryptVers(privateKey, test.getBytes()); //字节数组  公钥加密
    		  String strTest = Help.encodeHexStr(en_test);//字节数组转换为十六进制
		 System.out.println(strTest.length());
         System.out.println("加密串:"+strTest);	  
	     byte[] en_test1 = Help.decodeHex(strTest.toCharArray());
	
	     String  de_test = new String(CCBRsaUtil.getInstance().decryptVers(publicKey, en_test1));
	          
	     System.out.println("----解密信息=" +de_test);  
	
		}

}
  

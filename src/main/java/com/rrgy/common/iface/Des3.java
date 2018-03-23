package com.rrgy.common.iface;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Des3 {
	// 密钥  
    private final static String secretKey = "6b65805371548541wert1234";  
    // 向量  
    private final static String iv = "01234567";  
    // 加解密统一使用的编码方式  
    private final static String encoding = "utf-8";  
  
    /** 
     * 3DES加密 
     *  
     * @param plainText 普通文本 
     * @return 
     * @throws Exception  
     */  
    public static String encode(String plainText) throws Exception {  
        Key deskey = null;  
        BASE64Encoder encoder = new BASE64Encoder();  
        
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());  
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");  
        deskey = keyfactory.generateSecret(spec);  
  
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");  
//        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());  
        cipher.init(Cipher.ENCRYPT_MODE, deskey);  
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));  
        return encoder.encode(encryptData);  
    }  
  
    /** 
     * 3DES解密 
     *  
     * @param encryptText 加密文本 
     * @return 
     * @throws Exception 
     */  
    public static String decode(String encryptText) throws Exception {  
    	//--通过base64,将字符串转成byte数组  
        BASE64Decoder decoder = new BASE64Decoder();  
        byte[] bytesrc = decoder.decodeBuffer(encryptText);  
        //--解密的key  
        DESedeKeySpec dks = new DESedeKeySpec(secretKey.getBytes("UTF-8"));  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");  
        SecretKey securekey = keyFactory.generateSecret(dks);  
          
        //--Chipher对象解密  
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");  
        cipher.init(Cipher.DECRYPT_MODE, securekey);  
        byte[] retByte = cipher.doFinal(bytesrc);  
          
        return new String(retByte);  
    }
}

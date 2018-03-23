package com.lfgj.clinet.pay.payment.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryption {

	public static String Encrypt(String data, String key, String iv)
			throws Exception {
		try {

			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			int blockSize = cipher.getBlockSize();

			byte[] dataBytes = data.getBytes("UTF-8");
			int plaintextLength = dataBytes.length;
			if (plaintextLength % blockSize != 0) {
				plaintextLength = plaintextLength
						+ (blockSize - (plaintextLength % blockSize));
			}

			byte[] plaintext = new byte[plaintextLength];
			System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			byte[] encrypted = cipher.doFinal(plaintext);
			return Base64Method.EncryptBase64(encrypted);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String Desencrypt(String data, String key, String iv)
			throws Exception {
		try {
			
			byte[] encrypted1 =Base64Method.DecryptBase64ForByte(data);
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original,"UTF-8");
			return originalString;
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

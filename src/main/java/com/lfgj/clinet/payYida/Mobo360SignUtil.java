package com.lfgj.clinet.payYida;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.rrgy.core.constant.ConstConfig;

public class Mobo360SignUtil {

	private static String signType = Mobo360Config.SIGN_TYPE;
	// 证书
	private static PrivateKey CER_PRIVATE_KEY = null;
	private static X509Certificate CER_PUBLIC_KEY = null;
	// MD5
	private static String MD5_KEY = ConstConfig.pool.get("pay.yida.sceret");;
	// RSA
	private static PrivateKey RSA_PRIVATE_KEY = null;
	private static PublicKey RSA_PUBLIC_KEY = null;

	private Mobo360SignUtil() {

	}



	private static final String RSA_KEY_ALGORITHM = "RSA";
	private static final String SIGN_ALGORITHMS = "MD5withRSA";
	private static final String CHAR_SET = "UTF-8";


	/**
	 * 证书签名初始化
	 * 
	 * @param pfxFile
	 *            签名私钥文件的绝对路径
	 * @param certFile
	 *            验证签名文件的绝对路径
	 * @param pfxPwd
	 *            私钥文件的密码
	 */
	public static void initCert(String pfxFilePath, String certFilePath, String pfxPwd) throws Exception {
		if (StringUtils.isBlank(pfxFilePath)) {
			throw new Exception("私钥文件路径不能为空！");
		}
		if (StringUtils.isBlank(certFilePath)) {
			throw new Exception("公钥文件路径不能为空！");
		}
		if (StringUtils.isBlank(pfxPwd)) {
			throw new Exception("私钥密码不能为空！");
		}
		if (CER_PRIVATE_KEY == null || CER_PUBLIC_KEY == null) {
			InputStream is = null;
			try {
				KeyStore ks = KeyStore.getInstance("PKCS12");
				is = new FileInputStream(pfxFilePath);
				String pwd = pfxPwd;
				ks.load(is, pwd.toCharArray());
				String alias = "";
				Enumeration<String> e = ks.aliases();
				while (e.hasMoreElements()) {
					alias = e.nextElement();
				}
				CER_PRIVATE_KEY = (PrivateKey) ks.getKey(alias, pwd.toCharArray());
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				is = new FileInputStream(certFilePath);
				CER_PUBLIC_KEY = (X509Certificate) cf.generateCertificate(is);
				is.close();
			} catch (FileNotFoundException fnfe) {
				throw new RuntimeException("证书文件未找到，请检查配置文件！" + fnfe);
			} catch (Exception e) {
				throw new RuntimeException("签名初始化失败！" + e);
			} finally {
				if (null != is) {
					is.close();
				}
			}
		}
	}

	/**
	 * 生成签名
	 * 
	 * @param sourceData
	 * @return
	 * @throws Exception
	 */
	public static String signData(String sourceData) throws Exception {
		 //System.out.println("88888888888888888888888888888888888"+sourceData);
		if (StringUtils.isBlank(sourceData)) {
			throw new Exception("签名数据源串为空！");
		}
		String signStrintg = "";
		if ("CER".equals(signType)) {
			if (null == CER_PRIVATE_KEY) {
				throw new Exception("签名私钥证书尚未初始化！");
			}
			signStrintg = signWithRSA(CER_PRIVATE_KEY, sourceData);
		} else if ("MD5".equals(signType)) {
			if (StringUtils.isEmpty(MD5_KEY)) {
				throw new Exception("签名MD5 KEY尚未初始化！");
			}
			signStrintg = signByMD5(sourceData, MD5_KEY);
		} else if ("RSA".equals(signType)) {
			if (null == RSA_PRIVATE_KEY) {
				throw new Exception("签名RSA私钥尚未初始化！");
			}
			signStrintg = signWithRSA(RSA_PRIVATE_KEY, sourceData);
		}
		signStrintg = signStrintg.replaceAll("\r", "").replaceAll("\n", "");
	    //System.out.println("(sim)"+signStrintg);
		return signStrintg;
	}

	private static String signWithRSA(PrivateKey privateKey, String sourceData) throws Exception {
		String signString = "";
		Signature sign = Signature.getInstance(SIGN_ALGORITHMS);
		sign.initSign(privateKey);
		sign.update(sourceData.getBytes(CHAR_SET));
		byte[] signBytes = sign.sign();
		signString = Base64.encodeBase64String(signBytes);
		return signString;
	}

	/**
	 * 验证签名
	 * 
	 * @param signData
	 *            签名数据
	 * @param srcData
	 *            原数据
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyData(String signData, String srcData) throws Exception {
		if (StringUtils.isBlank(signData)) {
			throw new Exception("验证签名--原签名数据为空！");
		}
		if (StringUtils.isBlank(srcData)) {
			throw new Exception("验证签名--原数据为空！");
		}
		if ("CER".equals(signType)) {
			if (null == CER_PUBLIC_KEY) {
				throw new Exception("签名公钥证书尚未初始化！");
			}
			return checkSignWithRSA(CER_PUBLIC_KEY.getPublicKey(), signData, srcData);
		} else if ("MD5".equals(signType)) {
			if (StringUtils.isEmpty(MD5_KEY)) {
				throw new Exception("签名MD5 KEY尚未初始化！");
			}
			if (signData.equalsIgnoreCase(signByMD5(srcData, MD5_KEY))) {
				return true;
			} else {
				return false;
			}
		} else if ("RSA".equals(signType)) {
			if (null == RSA_PUBLIC_KEY) {
				throw new Exception("签名RSA公钥尚未初始化！");
			}
			return checkSignWithRSA(RSA_PUBLIC_KEY, signData, srcData);
		} else {
			return false;
		}
	}

	private static boolean checkSignWithRSA(PublicKey publicKey, String signData, String srcData) throws Exception {
		byte[] b = Base64.decodeBase64(signData);
		Signature sign = Signature.getInstance(SIGN_ALGORITHMS);
		sign.initVerify(publicKey);
		sign.update(srcData.getBytes(CHAR_SET));
		return sign.verify(b);
	}

	public static String signByMD5(String sourceData, String key) throws Exception {
		String data = sourceData + key;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] sign = md5.digest(data.getBytes(CHAR_SET));

		return Bytes2HexString(sign).toUpperCase();
	}

	/**
	 * 将byte数组转成十六进制的字符串
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String Bytes2HexString(byte[] b) {
		StringBuffer ret = new StringBuffer(b.length);
		String hex = "";
		for (int i = 0; i < b.length; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);

			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex.toUpperCase());
		}
		return ret.toString();
	}

}
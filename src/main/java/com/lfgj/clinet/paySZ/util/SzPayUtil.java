package com.lfgj.clinet.paySZ.util;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.lfgj.util.MD5;

public class SzPayUtil {

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param parameter
	 *            签名参数组 return 去掉空值与签名参数后的新签名参数组
	 * @throws PayException
	 */
	public static String createSign(Map<String, Object> data, String gymchtKey) throws RuntimeException {
		if (data.isEmpty()) {
			throw new RuntimeException("参数数组不能为空!");
		}
		TreeMap<String, Object> treemap = new TreeMap<String, Object>(data); 
		Set<String> keySet = treemap.keySet();
		StringBuffer signPars = new StringBuffer();
		for (String key : keySet) {
			if (key == "pay_sign" || treemap.get(key) == "")
				continue;
			else
				signPars.append(key + "="+treemap.get(key)+"&");
		}
		signPars.append("key="+gymchtKey);
		
		System.out.println(signPars);
		MD5 md5 = new MD5();
		String sign = md5.enCodeByMD5(signPars.toString(), null);
		return sign.toUpperCase();
	}

}

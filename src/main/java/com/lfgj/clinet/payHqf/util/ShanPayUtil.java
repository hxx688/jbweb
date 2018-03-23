package com.lfgj.clinet.payHqf.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import com.lfgj.clinet.payHqf.exception.PayException;

public class ShanPayUtil {

	/**
	 * 生成要请求给环球汇的参数数组
	 * 
	 * @param para_temp
	 *            请求前的参数数组
	 * @return 要请求的参数数组
	 */
	public static String buildRequestParaShan(Map<String, Object> para_temp,
			String key) throws PayException {
		// 除去待签名参数数组中的空值和签名参数
		Map<String, Object> para = paraFilterShan(para_temp);
		// 生成签名结果
		return buildRequestMysignShan(para, key);
	}

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param parameter
	 *            签名参数组 return 去掉空值与签名参数后的新签名参数组
	 * @throws PayException
	 */
	private static Map<String, Object> paraFilterShan(
			Map<String, Object> parameter) throws PayException {
		if (parameter.isEmpty()) {
			throw new PayException("参数数组不能为空!");
		}
		Map<String, Object> para_filte = new HashMap<String, Object>();
		Set<String> keySet = parameter.keySet();
		for (String key : keySet) {
			if (key == "sign" || parameter.get(key) == "")
				continue;
			else
				para_filte.put(key, parameter.get(key));
		}
		return para_filte;
	}

	/**
	 * 生成签名结果
	 * 
	 * @param para
	 *            已排序要签名的数组 return 签名结果字符串
	 */
	private static String buildRequestMysignShan(Map<String, Object> para,
			String key) throws PayException {
		String sign = createLinkstringShan(para);
		return md5SignShan(sign, key);
	}

	/**
	 * 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param para
	 *            需要拼接的数组 return 拼接完成以后的字符串
	 * @throws PayException
	 */
	private static String createLinkstringShan(Map<String, Object> para)
			throws PayException {
		if (para.isEmpty()) {
			throw new PayException("参数数组不能为空!");
		}
		StringBuffer arg = new StringBuffer();
		if(!"".equals(para.get("body"))&&para.get("body")!=null){
			arg.append("body="+para.get("body").toString()+"&");
		}
		arg.append("notify_url="+para.get("notify_url").toString()+"&");
		arg.append("out_order_no="+para.get("out_order_no").toString()+"&");
		arg.append("partner="+para.get("partner").toString()+"&");
		arg.append("return_url="+para.get("return_url").toString()+"&");
		arg.append("subject="+para.get("subject").toString()+"&");
		arg.append("total_fee="+para.get("total_fee").toString()+"&");
		arg.append("user_seller="+para.get("user_seller").toString());
		// 如果存在转义字符，那么去掉转义
		return StringEscapeUtils.unescapeJava(arg.toString());
	}

	private static String md5SignShan(String prestr, String key) {
		Md5Util md5Util = new Md5Util();
		return md5Util.encode(prestr+key, null);
	}
}

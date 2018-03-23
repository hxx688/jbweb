package com.lfgj.clinet.payHqf.util;
/**
 * 
 * Description：常量 <br>
 * 
 * ClassName：Constant <br>
 * 
 * Date：2016年6月25日下午11:42:09 <br>
 * 
 * Version：v1.0 <br>
 * 
 */
public class ConstantHqf {
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	/**
	 * 合作身份者PID，签约账号，由16位纯数字组成的字符串，请登录商户
	 */
	public final static String  PARTNER = "544898823718369";
	
	/**
	 *  MD5密钥，安全检验码，由数字和字母组成的32位字符串，请登录商户后
	 */
	public final static String KEY = "YWEzPK7zYqZpkG9EpVCitQu9H26BvtvY";
	
	/**
	 * 商户号（8位数字)
	 */
	public final static String  USER_SELLER = "571238";
	
	/**
	 *  服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可
	 */
	public final static String  NOTIFY_URL = "/payfront/notifyHqf";
	
	/**
	 * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	 */
	public final static String  RETURN_URL = "/payfront/result";
	
	/**
	 * 支付地址，必须外网可以正常访问
	 */
	public final static String  GATEWAY_NEW = "http://www.globalspay.com/PayOrder/payorder";
}

package com.lfgj.clinet.paySZ.util;
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
public class ConstantSZ {
	/**
	 * 合作身份者PID
	 */
	public final static String  PARTNER = "171229";
	
	/**
	 *  MD5密钥，安全检验码，由数字和字母组成的32位字符串，请登录商户后
	 */
	public final static String KEY = "L85RBTT5qM2vDxwKxhGLbVRlcHivlADR";
	
	/**
	 *  服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可
	 */
	public final static String  NOTIFY_URL = "/payfront/notifySz";
	
	/**
	 * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	 */
	public final static String  RETURN_URL = "/payfront/result";
	
	/**
	 * 支付地址，必须外网可以正常访问
	 */
	public final static String  GATEWAY_NEW = "http://106.14.206.106";
}

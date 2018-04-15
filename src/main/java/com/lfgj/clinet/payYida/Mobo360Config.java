package com.lfgj.clinet.payYida;

/**
 * 
 * 类名：Mobo360Config
 * 功能：基础配置类
 * 详细：设置商户相关信息及证书文件和通知地址等
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究接口使用，只是提供一个参考。
 *
 */

public class Mobo360Config {
	// 请选择签名类型， MD5、CER(证书文件)、RSA
    public static final String SIGN_TYPE = "MD5";
	//public static final String SIGN_TYPE = "CER";
    //public static final String SIGN_TYPE = "RSA";
	//******************配置商户基本信息***结束
	
	//******************使用MD5签名方式需配置下列参数

	//******************MD5签名方式需配置***结束
	
	
	

	
	//public static String MERCHANT_NOTIFY_URL = "http://127.0.0.1:8080/MobaoPayExample/callBack.jsp";
	// 支付系统网关

	public static final String MOBAOPAY_API_VERSION = "1.0.0.0";
	public static final String MOBAOPAY_API_VERSIONAP = "1.0.0.1";

	// 接口名称
	public static final String MOBAOPAY_APINAME_PAY = "WEB_PAY_B2C";
	public static final String MOBAOPAY_APINAME_QUERY = "MOBO_TRAN_QUERY";
	public static final String MOBAOPAY_APINAME_REFUND = "MOBO_TRAN_RETURN";
	public static final String MOBAOPAY_APINAME_REAL_PAY = "CUST_REAL_PAY";
	public static final String MOBAOPAY_APINAME_DS = "SINGLE_ENTRUST_SETT";
}

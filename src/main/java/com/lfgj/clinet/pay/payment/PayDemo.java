package com.lfgj.clinet.pay.payment;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.clinet.pay.payment.utils.AesEncryption;
import com.lfgj.clinet.pay.payment.utils.HttpRequest;
import com.lfgj.clinet.pay.payment.utils.MD5Util;
import com.lfgj.util.CommKit;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.JsonKit;

public class PayDemo {

	public static void main(String[] args){
		String appid = CommKit.getParameter("300").getPara();
		String key = CommKit.getParameter("301").getPara();
		String session = CommKit.getParameter("302").getPara();
		
//		Map<String,Object> info = getPayInfo();
//		String url = "masget.pay.compay.router.font.pay";
		
		Map<String,Object> info = getQueryInfo();
		String url = "ReqOrderInfo";
		
		try{
			String result = Invoke(url, appid, key, session, info);
			System.out.println("result:"+result);
			JSONObject json = JsonKit.parse(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @return
	 * result:{"ret":0,"message":"成功","data":"http://www.beizijinfu.com/topay.jsp?out_trade_no=921934951618965504"}
	 */
	public static Map<String,Object> getPayInfo(){
		Map<String,Object> info = new HashMap<String,Object>();
		info.put("amount","30000");
		info.put("Payordernumber",Func.orderNo("P"));
		info.put("Fronturl","");
		String domain = ConstConfig.pool.get("config.domain");
		String url = domain + "/clientService?serviceId=lf_pay/callback&iscrypt=2";
		info.put("Backurl",url);
		info.put("Body","测试");
		info.put("ExtraParams","");
		info.put("PayType","0"); // 0银联在线,1微信支付,2支付宝方式
		info.put("SubpayType","02"); // 银联在线 02快捷 
		Map<String,String> payParams = new HashMap<String,String>();
		payParams.put("BankCard", "6228480068331190071");
		payParams.put("IDCard", "420323197903013736");
		payParams.put("Tel", "13003890911");
		payParams.put("Name", "袁雨成");
		info.put("payParams",payParams);
		return info;
	}
	
	/**
	 * @return
	 * result:{"ret":0,"message":"成功","data":[{"orderNumber":"P1711071638243278311","amount":500000.0,"payorderid":"927817518666739712","businesstime":"2017-11-07 04:39:16","respcode":"2","extraparams":"","settlementAmount":495500.0,"state":"4","transfertime":"2017-11-07 04:39:16","transferseauence":"ca9c5b53a653494d8b15ca23e3a7244d","transfertype":"0"}]}
	 */
	public static Map<String,Object> getQueryInfo(){
		Map<String,Object> info = new HashMap<String,Object>();
		info.put("orderNumber","P1711071638243278311");
		return info;
	}
	
	public static String Invoke(String method, String appid, String secretkey, String session, Object info) throws IOException, Exception
    {
		String v = "2.0";
		String format = "json";
		
		String timestamp = DateKit.format(new Date(), DateKit.DATETIME_FORMATE);
        String data = AesEncryption.Encrypt(JsonKit.toJson(info), secretkey, secretkey);
        String mySgin = MD5Util.string2MD5(secretkey + appid + data + format + method + session + timestamp + v + secretkey).toLowerCase();
        System.out.println(JsonKit.toJson(info));
        System.out.println("data:"+data);
        System.out.println("mySgin:"+mySgin);
        
        Map<String, String> dic = new HashMap<String, String>();
        dic.put("appid", appid);
        dic.put("method", method);
        dic.put("format", format);
        dic.put("data", data);
        dic.put("timestamp", timestamp);
        dic.put("session", session);
        dic.put("sign", mySgin);
        dic.put("v", v);
        System.out.println("dic:"+dic);
        String resp = HttpRequest.sendPost("http://bank.fjelt.com/pay/rest", dic);
        return resp;
    }
  
}

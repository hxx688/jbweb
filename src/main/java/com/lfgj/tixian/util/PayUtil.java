package com.lfgj.tixian.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.clinet.pay.payment.utils.AesEncryption;
import com.lfgj.clinet.pay.payment.utils.MD5Util;
import com.lfgj.util.CommKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.HttpKit;
import com.rrgy.core.toolbox.kit.JsonKit;

public class PayUtil {
	
	public static Object get(Map<String, Object> dataMap){	
		String result = commonFjeltApi(dataMap,"settlement.add");
		JSONObject jsonob = JSONObject.parseObject(result);
		String ret = jsonob.getString("ret");
		if ("0".equals(ret)) {
			JSONObject data = jsonob.getJSONObject("data");
			return data.get("SettlementID");
		}
		return null;
	}
	
	public static Object search(String bankCardNo){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("BankCardNo", bankCardNo);
		String result = commonFjeltApi(dataMap,"settlement.query");	
		JSONObject jsonob = JSONObject.parseObject(result);
		String ret = jsonob.getString("ret");
		if ("0".equals(ret)) {
			JSONObject data = jsonob.getJSONObject("data");
			return data.get("SettlementID");
		}
		return null;
	}
	
	public static String check(String orderNo){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("OrderNumber", orderNo);
		String result = commonFjeltApi(dataMap,"TransferQuery");
		JSONObject jsonob = JSONObject.parseObject(result);
		String data = jsonob.getString("data");
		if(Func.isEmpty(data)){
			data = "2";
		}
		System.out.println("提现查询："+orderNo+"="+jsonob.toString());
		return data;
	}
	
	public static Object transfer(Map<String, Object> dataMap){
		String result = commonFjeltApi(dataMap,"settlement.transfer");
		JSONObject jsonob = JSONObject.parseObject(result);
		String ret = jsonob.getString("ret");
		if ("0".equals(ret)) {
			return "";
		} else {
			String message = jsonob.getString("message");
			return message;
		}
	}

	public static String commonFjeltApi(Map<String, Object> dataMap,String method) {
		Map<String, Object> payParam = new HashMap<String, Object>();
		try {
			String appid = CommKit.getParameter("300").getPara();
			String key = CommKit.getParameter("301").getPara();
			String session = CommKit.getParameter("302").getPara();
			
			String secretkey = key;
			payParam.put("appid", appid);
			payParam.put("method", method);
			payParam.put("format", "json");

			String dataJson = JsonKit.toJson(dataMap);
			String data = AesEncryption.Encrypt(dataJson, secretkey, secretkey);
			payParam.put("data", data);
			payParam.put("v", "2.0");
			payParam.put("timestamp", DateTimeKit.now());
			payParam.put("session", session);
			String source = secretkey + payParam.get("appid") + payParam.get("data") + payParam.get("format")
					+ payParam.get("method") + payParam.get("session") + payParam.get("timestamp") + payParam.get("v")
					+ secretkey;

			String sign = MD5Util.string2MD5(source).toLowerCase();
			payParam.put("sign", sign);
			
			String url = "http://bank.fjelt.com/pay/Rest";

			String result = HttpKit.post(url, payParam);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}

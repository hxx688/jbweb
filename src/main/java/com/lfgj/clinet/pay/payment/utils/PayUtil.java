package com.lfgj.clinet.pay.payment.utils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.JsonKit;

public class PayUtil {

	public static String Invoke(String method, String appid, String secretkey, String session, Object info) throws IOException, Exception
    {
		String v = "2.0";
		String format = "json";
		String timestamp = DateKit.format(new Date(), DateKit.DATETIME_FORMATE);
        String data = AesEncryption.Encrypt(JsonKit.toJson(info), secretkey, secretkey);
        String mySgin = MD5Util.string2MD5(secretkey + appid + data + format + method + session + timestamp + v + secretkey).toLowerCase();
        
        Map<String, String> dic = new HashMap<String, String>();
        dic.put("appid", appid);
        dic.put("method", method);
        dic.put("format", format);
        dic.put("data", data);
        dic.put("timestamp", timestamp);
        dic.put("session", session);
        dic.put("sign", mySgin);
        dic.put("v", v);
        String resp = HttpRequest.sendPost("http://bank.fjelt.com/pay/rest", dic);
        return resp;
    }
  
}

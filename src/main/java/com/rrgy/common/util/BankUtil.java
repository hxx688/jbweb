package com.rrgy.common.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.HttpKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.StrKit;

/**
 * 银行卡校验工具类
 * 
 * @author cxx
 *
 */
public class BankUtil {

	public static AjaxResult verifyBankCark(String bankName, String userName, String bankno) {
		AjaxResult result = new AjaxResult();
		// 本接口实时联网，包含发卡行属性信息，具体字段说明如下： code：0表示查询成功，其他表示查询失败 desc：描述信息
		// data：银行卡信息，当code=0时有效 bankno：银行卡号 bank_id：开户行编码 bank_name：开户行名称
		// card_name：卡名称 bank_phone：客服电话 card_type：卡类型
		String host = "http://cardinfo.market.alicloudapi.com/lianzhuo/querybankcard";
		// String path = "/lianzhuo/querybankcard";
		Map<String, Object> headers = new HashMap<String, Object>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
		// 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + ConstConfig.APPCODE);
		// headers.put("Authorization", "APPCODE
		// 2b4ba3c08efb4c869c4c19bc27e576fb");
		Map<String, Object> querys = new HashMap<String, Object>();
		// querys.put("bankno", "6217580100005980644");
		querys.put("bankno", bankno);

		try {
			String response = HttpKit.get(host, headers, querys);
			System.out.println(response);
			if (StrKit.isNotEmpty(response)) {
				// 返回信息转JSON对象
				JSONObject responseJSON = JsonKit.parse(response);
				JSONObject resp = responseJSON.getJSONObject("resp");
				int code = resp.getIntValue("code");
				String desc = resp.getString("desc");
				if (0 == code) {
					// 银行卡信息转JSON对象
					JSONObject data = responseJSON.getJSONObject("data");
					// 获取银行卡银行名称
					String bank_name = data.getString("bank_name");
					if (bankName.contains(bank_name)) {// 银行名称与用户所选一致
						// 银行卡二元素验证(银行卡号+姓名)
						host = "http://lundroid.market.alicloudapi.com/lianzhuo/verifi";
						// String path = "/lianzhuo/verifi";
						// Map<String, String> querys = new HashMap<String,
						// String>();
						querys.clear();
						querys.put("acct_name", userName);
						// querys.put("acct_name", "吴彦祖");
						querys.put("acct_pan", bankno);// 银行卡
						// querys.put("acct_pan", "6217580100005980644");//银行卡
						// querys.put("cert_id", "350822198908255512");身份证
						// querys.put("phone_num", "15165025603");手机号
						response = HttpKit.get(host, headers, querys);
						System.out.println(response);
						if (StrKit.isNotEmpty(response)) {
							// 返回信息转JSON对象
							responseJSON = JsonKit.parse(response);
							resp = responseJSON.getJSONObject("resp");
							code = resp.getIntValue("code");
							desc = resp.getString("desc");
							if (code == 0) {
								result.setCode(code);
								result.setMessage(desc);
							} else {
								result.setCode(code);
								result.setMessage(desc);
							}
						}
					} else {
						result.setCode(100);
						result.setMessage("银行卡号和银行名称不匹配");
					}
				} else {
					result.setCode(code);
					result.setMessage(desc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(verifyBankCark("", "", "6227001822930364905"));
		System.out.println(verifyBankCark("", "", "6212261402022846080"));
	}

}
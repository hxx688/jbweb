package com.lfgj.clinet.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.pay.payment.utils.AesEncryption;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.MathKit;

@Service
@Client(name = "lf_pay")
public class PayClient extends RequestMethod{
	@Autowired
	MemberService service;
	
	public ResultVo callback() {
		ResultVo rv = new ResultVo();
		// 获取支付平台返回的数据
		String method = getParams("Method","");
		System.out.println("回调:"+getParameters());
		if("paymentreport".equals(method)){
			String data = getParams("Data",""); // 业务数据经过AES加密后，进行urlsafe base64编码
			String sign = getParams("Sign","");
			
			// AES解密
			String key = CommKit.getParameter("301").getPara();;
			try {
				data = AesEncryption.Desencrypt(data, key, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//System.out.println("======lf_pay data:"+data);
			JSONObject json = JsonKit.parse(data);
			if(json != null){
				String ordernumber = json.getString("ordernumber"); // 商户订单号
				if(Func.isEmpty(ordernumber)){
					 ordernumber = json.getString("orderNumber"); // 商户订单号
				}
				String amount = json.getString("amount"); // 交易金额
				String payorderid = json.getString("payorderid"); // 交易流水号
				String businesstime = json.getString("businesstime"); // 交易时间yyyy-MM-dd hh:mm:ss
				String respcode = json.getString("respcode"); // 交易状态:1-待支付 2-支付完成 3-已关闭 4-交易撤销
				String extraparams = json.getString("extraparams"); // 扩展内容 原样返回
				String respmsg = json.getString("respmsg"); // 状态说明
				
				String sql = "select * from dt_payinfo where ordernumber=#{ordernumber}";
				PayInfo payInfo = Blade.create(PayInfo.class).findFirst(sql, Paras.create().set("ordernumber", ordernumber));
				
				if(payInfo != null && "0".equals(payInfo.getRespcode())){
					payInfo.setRespcode(respcode);
					payInfo.setBusinesstime(businesstime);
					payInfo.setRespmsg(respmsg);
					payInfo.setPayorderid(payorderid);
					payInfo.setRespname(LfConstant.PAY_RESPCODE.get(respcode));
					Blade.create(PayInfo.class).update(payInfo);
					
					if("2".equals(respcode)){
						boolean temp = service.addRecharge(payInfo.getUser_id(), MathKit.div(amount, "100", 2), false,payInfo.getOrdernumber());
						if(temp){
							rv.setMessage("成功");
							rv.setResponse("00");
							return rv;
						}
					}
				}
			}
		}
		
		rv.setMessage("失败");
		rv.setResponse("11");
		
		return rv;
	}
}

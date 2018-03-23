package com.lfgj.clinet.paySZ;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.paySZ.util.ConstantSZ;
import com.lfgj.clinet.paySZ.util.SzPayUtil;
import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.MathKit;

/**
 * 支付通道：深圳
 * @author Administrator
 *
 */
@Service
@Client(name = "lf_prepay_sz")
public class PrePaySZClient extends RequestMethod{
	@Autowired
	MemberService service;
	
	public ResultVo url() {
		ResultVo rv = new ResultVo();
		String person_id = getParams("id","");	
		
		String pay_service_type = "1";
		String pay_type = "1";
		String money = getParams("money","0");
		
		Member person = Blade.create(Member.class).findById(person_id);
		
		if(person == null){
			rv.setReturnCode("1");
			rv.setReturnMsg("查询不到用户信息！");
			return rv;
		}
		
		if(Float.valueOf(money) <= 0){
			rv.setReturnCode("1");
			rv.setReturnMsg("金额应大于0！");
			return rv;
		}
		
		String orderNo = getOrderNo();
		
		PayInfo payInfo = new PayInfo();
		payInfo.setAmount(money);
		payInfo.setOrdernumber(orderNo);
		payInfo.setCreate_time(new Date());
		payInfo.setUser_id(Integer.valueOf(person_id));
		payInfo.setReal_name(person.getReal_name());
		payInfo.setMobile(person.getMobile());
		payInfo.setPay_acount(person.getBank_acount());
		payInfo.setPay_type(pay_type);
		payInfo.setPay_type_name(LfConstant.PAY_TYPE.get(pay_type));
		payInfo.setRespcode("0"); // 未提交
		payInfo.setRespname(LfConstant.PAY_RESPCODE.get("0"));
		
		boolean rs = Blade.create(PayInfo.class).save(payInfo);
		if(!rs){
			rv.setReturnCode("1");
			rv.setReturnMsg("保存支付订单出错!");
			return rv;
		}
		
		return getPayUrl(money, orderNo, pay_service_type);
	}
	
	public ResultVo getPayUrl(String money, String orderNo, String pay_service_type){
		ResultVo rv = new ResultVo();
		try{
			String md5key = ConstantSZ.KEY;
			
			Map<String,Object> requestParams = getParameter(money, orderNo, pay_service_type);
			String signMd5 = SzPayUtil.createSign(requestParams, md5key);
			requestParams.put("pay_sign", signMd5);
			requestParams.put("pay_bank_code", "CCB");
//			requestParams.put("pay_remark", "");
//			requestParams.put("pay_extend1", "");
//			requestParams.put("pay_extend2", "");
			requestParams.put("pay_url", ConstantSZ.GATEWAY_NEW);
			System.out.println("深圳支付请求参数："+requestParams);
	        rv.setReturnCode("0");
			rv.setReturnParams(requestParams);
			return rv;
		}catch(Exception e){
			e.printStackTrace();
			rv.setReturnCode("1");
			rv.setReturnMsg(e.toString());
		}
		return rv;
	}
	
	private Map<String,Object> getParameter(String money, String orderNo, String pay_service_type) throws RuntimeException{
		String partner = ConstantSZ.PARTNER;
		
		Map<String,Object> parameter = new HashMap<String,Object>(); 
		parameter.put("partner", partner);
		parameter.put("pay_input_charset", "1");
		parameter.put("pay_service_type", pay_service_type);
		String pay_date = String.valueOf(new Date().getTime());
		pay_date = pay_date.substring(0, 10); // 取时间戳前10位
		parameter.put("pay_date", pay_date);
		parameter.put("pay_order_id", orderNo);
		parameter.put("pay_product_name", "购买");
		parameter.put("pay_amount", MathKit.mul(money, 100, 0));
		
		String domain = ConstConfig.pool.get("config.domain");
		String fronturl = domain + ConstantSZ.RETURN_URL;
		String notifyurl = domain + ConstantSZ.NOTIFY_URL;
		
		parameter.put("pay_notify_url", notifyurl);
		parameter.put("pay_return_url", fronturl);
		return parameter;
	}
	
	// 获取20位的交易订单号
	private String getOrderNo(){
		String orderNo = DateKit.getAllTime() + (int)((Math.random()*9+1)*100000);
		return orderNo;
	}
	
	public static void main(String[] args){
//		PrePaySZClient p = new PrePaySZClient();
//		p.getPayUrl("1", "P20171201001");
		System.out.println(DateKit.getAllTime() + ((Math.random()*9+1)*100000));  
	}
}

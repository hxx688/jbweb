package com.lfgj.clinet.payHqf;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.payFactory.IPayService;
import com.lfgj.clinet.payHqf.exception.PayException;
import com.lfgj.clinet.payHqf.util.ConstantHqf;
import com.lfgj.clinet.payHqf.util.ShanPayUtil;
import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.LfConstant;
import com.lfgj.util.PayTypeEnum;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;

/**
 * 支付通道：环球付
 * @author Administrator
 *
 */

@Service
@Client(name = "lf_prepay_hqf")
public class PrePayHqfClient extends RequestMethod implements IPayService{
	@Autowired
	MemberService service;
	
	public ResultVo url() {
		ResultVo rv = new ResultVo();
		String person_id = getParams("id","");	
		String pay_type = "6"; // 环球付
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
		
		String orderNo = Func.orderNo("P");
		
		PayInfo payInfo = new PayInfo();
		payInfo.setAmount(money);
		payInfo.setOrdernumber(orderNo);
		payInfo.setCreate_time(new Date());
		payInfo.setUser_id(Integer.valueOf(person_id));
		payInfo.setReal_name(person.getReal_name());
		payInfo.setMobile(person.getMobile());
		payInfo.setPay_acount(person.getBank_acount());
		payInfo.setPay_type(pay_type);
		payInfo.setPay_type_name(PayTypeEnum.parseByCode(pay_type).getPayName());
		payInfo.setRespcode("0"); // 未提交
		payInfo.setRespname(LfConstant.PAY_RESPCODE.get("0"));
		
		boolean rs = Blade.create(PayInfo.class).save(payInfo);
		if(!rs){
			rv.setReturnCode("1");
			rv.setReturnMsg("保存支付订单出错!");
			return rv;
		}
		
		return getPayUrl(money, orderNo);
	}
	
	@Override
    public ResultVo getPayUrl(String money, String orderNo, String... extendStrs){
		ResultVo rv = new ResultVo();
		try{
			Map<String,Object> requestParams = getParameter(money, orderNo);
			String signMd5 = ShanPayUtil.buildRequestParaShan(requestParams, ConstantHqf.KEY);
			requestParams.put("sign", signMd5);
			requestParams.put("pay_url", ConstantHqf.GATEWAY_NEW);
			String domain = ConstConfig.pool.get("config.domain");
			int i = domain.indexOf("//");
			if(i >= 0){
				domain = domain.substring(i+2, domain.length());
			}
			requestParams.put("http_referer", domain); // 这个域名，要和支付通道后台绑定的域名一样，不加http://
	        rv.setReturnCode("0");
			rv.setReturnParams(requestParams);
			System.out.println("环球付请求参数："+requestParams);
			return rv;
		}catch(Exception e){
			e.printStackTrace();
			rv.setReturnCode("1");
			rv.setReturnMsg(e.toString());
		}
		return rv;
	}
	
	private Map<String,Object> getParameter(String money, String orderNo) throws PayException{
		
		/**************************请求参数**************************/
		 //商户订单号
		String out_order_no = orderNo;
		//订单名称
		String subject = "购买";
		//付款金额
		String total_fee = money;
		//订单描述
		String body = "";
		if(out_order_no==null||subject==null||total_fee==null){
			throw new PayException("必要参数不能为空!");
		}
		//服务器异步通知页面路径
		String domain = ConstConfig.pool.get("config.domain");
		String return_url = domain + ConstantHqf.RETURN_URL;
		String notify_url = domain + ConstantHqf.NOTIFY_URL;
		
		if("".equals(notify_url)){
			throw new PayException("notify_url不能为空!");
		}
       //需http://格式的完整路径，不能加?id=123这类自定义参数
       //页面跳转同步通知页面路径
		if("".equals(return_url)){
			throw new PayException("return_url不能为空!");
		}
		if("".equals(ConstantHqf.USER_SELLER)){
			throw new PayException("商户号不能为空!");
		}
		Map<String,Object> parameter = new HashMap<String,Object>(); 
//		parameter.put("body", body);
		parameter.put("notify_url", notify_url);
		parameter.put("out_order_no", out_order_no);
		parameter.put("partner", ConstantHqf.PARTNER);
		parameter.put("return_url", return_url);
		parameter.put("subject", subject);
		parameter.put("total_fee", total_fee);
		parameter.put("user_seller", ConstantHqf.USER_SELLER);
		return parameter;
	}
	
	public static void main(String[] args){
		String domain = "http://www.baidu.com";
		int i = domain.indexOf("//");
		if(i >= 0){
			domain = domain.substring(i+2, domain.length());
		}
		System.out.println(domain);
	}
}

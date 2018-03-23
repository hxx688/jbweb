package com.lfgj.clinet.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.pay.payment.utils.PayUtil;
import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.model.Parameter;

/**
 * 支付通道：杉徳
 * @author Administrator
 *
 */
@Service
@Client(name = "lf_prepay")
public class PrePayClient extends RequestMethod{
	@Autowired
	MemberService service;
	
	public ResultVo url() {
		ResultVo rv = new ResultVo();
		
		Parameter closePayMsg = CommKit.getParameter("305");
		if(closePayMsg != null && StrKit.isNotEmpty(closePayMsg.getPara())){
			rv.setReturnCode("1");
			rv.setReturnMsg(closePayMsg.getPara());
			return rv;
		}
		
		String person_id = getParams("id","");	
		String pay_type = getParams("pay_type",""); // 支付方式：0银联 1微信  2支付宝
		String money = getParams("money","0");
		
		Member person = Blade.create(Member.class).findById(person_id);
		
		if(person == null){
			rv.setReturnCode("1");
			rv.setReturnMsg("查询不到用户信息！");
			return rv;
		}
		
		String bankCard = StrKit.trim(person.getBank_acount());
		String idCard = StrKit.trim(person.getIdcart());
		String tel = StrKit.trim(person.getBank_mobile());
		String name = StrKit.trim(person.getKaihuming());
		if(Func.isEmpty(name)){
			name = StrKit.trim(person.getReal_name());
		}
		
//		if(!"0".equals(pay_type)){
//			rv.setReturnCode("1");
//			rv.setReturnMsg("目前只支持银联快捷支付!");
//			return rv;
//		}
		
		String PayType = "";
		String SubpayType = "";
		
		if("0".equals(pay_type)){ // 银联
			PayType = "0";
			SubpayType = "02";
			
			if(StrKit.isEmpty(bankCard)
					|| StrKit.isEmpty(idCard)
					|| StrKit.isEmpty(tel)
					|| StrKit.isEmpty(name)){
				rv.setReturnCode("1");
				rv.setReturnMsg("快捷支付的资料不完整，请完善！");
				return rv;
			}
		}else if("1".equals(pay_type)){ // 微信
			PayType = "1";
			SubpayType = "10";
		}else if("2".equals(pay_type)){ // 支付宝
			PayType = "2";
			SubpayType = "10";
		}else{
			rv.setReturnCode("1");
			rv.setReturnMsg("支付类型不正确！");
			return rv;
		}
		
		String appid = CommKit.getParameter("300").getPara();
		String key = CommKit.getParameter("301").getPara();
		String session = CommKit.getParameter("302").getPara();
		
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
		payInfo.setPay_type_name(LfConstant.PAY_TYPE.get(pay_type));
		payInfo.setRespcode("0"); // 未提交
		payInfo.setRespname(LfConstant.PAY_RESPCODE.get("0"));
		
		boolean rs = Blade.create(PayInfo.class).save(payInfo);
		if(!rs){
			rv.setReturnCode("1");
			rv.setReturnMsg("保存支付信息出错!");
			return rv;
		}
		
		Map<String,Object> info = new HashMap<String,Object>();
		info.put("amount",String.valueOf(Integer.valueOf(money)*100));
		info.put("Payordernumber",orderNo);
		String domain = ConstConfig.pool.get("config.domain");
		String fonturl = domain + "/payfront/result";
		info.put("Fronturl",fonturl);
		String backurl = domain + "/clientService?serviceId=lf_pay/callback&iscrypt=2";
		info.put("Backurl",backurl);
		info.put("Body","购买");
		info.put("ExtraParams","");
		info.put("PayType",PayType); // 0银联在线,1微信支付,2支付宝方式
		info.put("SubpayType",SubpayType); // 银联在线 02快捷 
		
		if("0".equals(pay_type)){ // 银联快捷支付
			Map<String,String> payParams = new HashMap<String,String>();
			payParams.put("BankCard", bankCard);
			payParams.put("IDCard", idCard);
			payParams.put("Tel", tel);
			payParams.put("Name", name);
			info.put("payParams",payParams);
		}
		
		try{
			String result = PayUtil.Invoke("masget.pay.compay.router.font.pay", appid, key, session, info);
			
			JSONObject json = JsonKit.parse(result);
			String ret = json.getString("ret");
			if("0".equals(ret)){
				rv.setReturnCode("0");
				rv.setReturnParams(json.getString("data")); // 支付的url
			}else{
				rv.setReturnCode("1");
				rv.setReturnMsg("支付平台："+json.getString("message"));
			}
			
		}catch(Exception e){
			e.printStackTrace();
			rv.setReturnCode("1");
			rv.setReturnMsg(e.toString());
		}
		
		return rv;
	}
	
}

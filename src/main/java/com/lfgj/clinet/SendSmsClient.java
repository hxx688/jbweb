package com.lfgj.clinet;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.rrgy.common.iface.Des3;
import com.rrgy.common.iface.RegisterCodeImpl;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.Func;
import com.rrgy.sms.service.SmsService;


/**
 * 发送短信
 * @author wuhb
 *
 */

@Service
@Client(name = "lf_sendsms")
public class SendSmsClient extends RequestAbs {
	
	
	@Autowired
	SmsService smsService;
	@Autowired
	MemberService service;
	
	@Override
	public ResultVo getResult() {
		// 返回数据
		ResultVo rv = new ResultVo();
		try{
			
			String mobile = getParams("mobile","");
			String isvalid = getParams("isvalid","");
			
			if(!Func.isMobile(mobile)){
				rv.setReturnMsg("手机号码无效");
				rv.setReturnCode("3");
				return rv;
			}
			if(Func.isEmpty(mobile)){
				rv.setReturnMsg("手机号码不能为空");
				rv.setReturnCode("3");
				return rv;
			}

			if(!Func.isEmpty(isvalid)){
				Member person = new Member();
				person.setMobile(mobile);
				long l = service.count(person);	
				if(l>0){
					rv.setReturnMsg("该手机号码已存在");
					rv.setReturnCode("4");
					return rv;
				}
			}
			
			Map<String, Object> send = smsService.sendCode(mobile);
			String code = send.get("code").toString();
			
			if("0".equals(code)){
				rv.setReturnCode("0");
				rv.setReturnMsg("发送验证码成功");
			}else if("1".equals(code)){
				rv.setReturnCode("1");
				rv.setReturnMsg("发送验证码失败:");
				rv.setReturnParams(send.get("msg"));
			}else{
				rv.setReturnCode("1");
				rv.setReturnMsg("发送验证码失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			rv.setReturnCode("2");
			rv.setReturnMsg("发送验证码失败");
			rv.setReturnParams(e.toString());
		}
		return rv;
	}

	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8281/rrgy/clientService";
		String param = "{\"serviceId\":\"rrgy_2017_1.0_sendsms\","
				+ "\"params\":{\"mobile\":\"10102919224\"}}";
		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
		rs = Des3.decode(rs);
		System.out.println(rs);
	}
}

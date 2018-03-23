package com.rrgy.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;
import com.rrgy.sms.service.SmsService;

/**
 * 短信验证码验证
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_checkSms")
public class CheckSmsCodeClient extends RequestAbs {
	@Autowired
	SmsService smsService;
	@Autowired
	PersonService service;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		String mobile = getParams("mobile","");
		String code = getParams("code","");
		String isvalid = getParams("isvalid","");

		if(Func.isEmpty(mobile)){
			rv.setReturnMsg("手机号不能为空");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		if(!Func.isMobile(mobile)){
			rv.setReturnMsg("手机号码无效");
			rv.setReturnCode("3");
			return JsonKit.toJson(rv);
		}
		if(!Func.isNum(code)){
			rv.setReturnMsg("无效验证码");
			rv.setReturnCode("2");
			return JsonKit.toJson(rv);
		}
		
		if(!Func.isEmpty(isvalid)){
			Person person = new Person();
			person.setMobile(mobile);
			person.setGroup_id(1);
			long l = service.count(person);	
			if(l>0){
				rv.setReturnMsg("该手机号码已存在");
				rv.setReturnCode("4");
				return JsonKit.toJson(rv);
			}
		}
		
		boolean vlc = smsService.findValidSms(mobile, code);
		
		if(!vlc){
			rv.setReturnMsg("无效验证码");
			rv.setReturnCode("2");
			return JsonKit.toJson(rv);
		}else{
			smsService.updateSms(mobile);
		}
		
		rv.setReturnCode("0");
		rv.setReturnMsg("验证成功");
		
		return JsonKit.toJson(rv);
	}

}

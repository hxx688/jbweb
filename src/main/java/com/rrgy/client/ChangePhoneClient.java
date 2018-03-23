package com.rrgy.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;
import com.rrgy.sms.service.SmsService;

/**
 * 验证旧手机
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_changePhone")
public class ChangePhoneClient extends RequestAbs {
	
	@Autowired
	PersonService service;
	@Autowired
	SmsService smsService;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		String code = getParams("code", "");//短信验证码
		String user_id = getParams("user_id", "");
		
		Person person = Blade.create(Person.class).findById(user_id);
		String mobile = person.getMobile();
		
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

package com.rrgy.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;
import com.rrgy.sms.service.SmsService;

/**
 * 绑定新手机
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_changePhoneTwo")
public class ChangePhoneTwoClient extends RequestAbs {
	
	@Autowired
	PersonService service;
	@Autowired
	SmsService smsService;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		String code = getParams("code", "");//短信验证码
		String new_phone = getParams("new_phone","");//新手机号码
		String user_id = getParams("user_id", "");
		
		Person person = Blade.create(Person.class).findById(user_id);
		if(person==null){
			rv.setReturnMsg("会员不存在");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		
		Person p = Blade.create(Person.class).findFirstBy(" id <> #{user_id} and mobile =#{mobile} and group_id = #{group_id}",
				Paras.create().set("user_id", user_id).set("mobile", new_phone).set("group_id", person.getGroup_id()));
		
		if(null != p){
			rv.setReturnMsg("手机号码已存在");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		
		boolean vlc = smsService.findValidSms(new_phone, code);
		
		if(!vlc){
			rv.setReturnMsg("无效验证码");
			rv.setReturnCode("2");
			return JsonKit.toJson(rv);
		}else{
			smsService.updateSms(new_phone);
		}
		
		person.setMobile(new_phone);
		boolean rs = Blade.create(Person.class).update(person);
		if (rs) {
			rv.setReturnCode("0");
			rv.setReturnMsg("绑定成功");
		} else {
			rv.setReturnCode("3");
			rv.setReturnMsg("绑定失败");
		}
		
		return JsonKit.toJson(rv);
	}
}

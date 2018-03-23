package com.rrgy.person.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.common.iface.Des3;
import com.rrgy.common.iface.RegisterCodeImpl;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;
import com.rrgy.sms.service.SmsService;

@Service
@Client(name = "rrgy_2017_1.0_personForget")
public class PersonForgetClient extends RequestAbs{
	
	@Autowired
	PersonService service;
	@Autowired
	SmsService smsService;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String mobile = getParams("mobile","");
		String code = getParams("code","");
		String password = getParams("password","");
		String group_id = getParams("group_id","1");
		
		if(Func.isEmpty(mobile)){
			rv.setReturnMsg("手机号不能为空");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		
		if(!Func.isNum(code)){
			rv.setReturnMsg("无效验证码");
			rv.setReturnCode("2");
			return JsonKit.toJson(rv);
		}
		
		boolean vlc = smsService.findValidSms(mobile, code);
		
		if(!vlc){
			rv.setReturnMsg("无效验证码");
			rv.setReturnCode("2");
			return JsonKit.toJson(rv);
		}
			
		Person person = new Person();
		person.setMobile(mobile);
		person.setGroup_id(Integer.valueOf(group_id));
		List<Person> persons = service.findByTemplate(person);	
		if(persons==null||persons.size()==0){
			rv.setReturnMsg("该手机号码不存在");
			rv.setReturnCode("4");
			return JsonKit.toJson(rv);
		}
		
		person = persons.get(0);
		Person nperson = new Person();
		nperson.setId(person.getId());
		String salt = ShiroKit.getRandomSalt(6);
		nperson.setSalt(salt);
		password = ShiroKit.md5(password, salt);// 加密后的密码
		nperson.setPassword(password);
		nperson.setStatus(person.getStatus());
		vlc = service.update(nperson);
		if(!vlc){
			rv.setReturnCode("1");
			rv.setReturnMsg("密码修改失败");
		}else{
			rv.setReturnCode("0");
			rv.setReturnMsg("密码修改成功");
		}
		return JsonKit.toJson(rv);
	}

	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8281/rrgy/clientService";
		String param = "{\"serviceId\":\"rrgy_2017_1.0_personForget\","
				+ "\"params\":{\"mobile\":\"10102919224\",\"code\":\"896474\",\"password\":\"123456\"}}";
		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
		rs = Des3.decode(rs);
		System.out.println(rs);
	}
}

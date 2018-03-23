package com.lfgj.clinet.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.MD5;
import com.rrgy.common.iface.Des3;
import com.rrgy.common.iface.RegisterCodeImpl;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.Func;
import com.rrgy.sms.service.SmsService;

@Service
@Client(name = "lfgj_personFindPassword")
public class MemberForgetClient extends RequestAbs{
	
	@Autowired
	MemberService service;
	@Autowired
	SmsService smsService;
	
	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		String mobile = getParams("mobile","");
		String code = getParams("code","");
		String password = getParams("password","");
		String ch_type = getParams("ch_type","");
		
		if(Func.isEmpty(mobile)){
			rv.setReturnMsg("手机号不能为空");
			rv.setReturnCode("1");
			return rv;
		}
		
		if(!Func.isNum(code)){
			rv.setReturnMsg("无效验证码");
			rv.setReturnCode("2");
			return rv;
		}
		
		boolean vlc = smsService.findValidSms(mobile, code);
		
		if(!vlc){
			rv.setReturnMsg("无效验证码");
			rv.setReturnCode("2");
			return rv;
		}
			
		Member person = new Member();
		person.setMobile(mobile);
		List<Member> persons = service.findByTemplate(person);	
		if(persons==null||persons.size()==0){
			rv.setReturnMsg("该手机号码不存在");
			rv.setReturnCode("4");
			return rv;
		}
		
		person = persons.get(0);
		Member nperson = new Member();
		nperson.setId(person.getId());
		MD5 md5 = new MD5();
		password = md5.enCodeByMD5(password,person.getMobile());
		
		if("2".equals(ch_type)){
			nperson.setPay_password(password);
		}else{
			nperson.setPassword(password);
		}
		
		nperson.setStatus(person.getStatus());
		vlc = service.update(nperson);
		if(!vlc){
			rv.setReturnCode("1");
			rv.setReturnMsg("密码修改失败");
		}else{
			rv.setReturnCode("0");
			rv.setReturnMsg("密码修改成功");
		}
		return rv;
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

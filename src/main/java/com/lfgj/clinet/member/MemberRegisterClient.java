package com.lfgj.clinet.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.member.service.MemberService;
import com.rrgy.common.iface.Des3;
import com.rrgy.common.iface.RegisterCodeImpl;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.Func;
import com.rrgy.sms.service.SmsService;

@Service
@Client(name = "lfgj_personRegister")
public class MemberRegisterClient extends RequestAbs{
	
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
		String real_name = getParams("real_name","");
		String tuijian = getParams("tuijian","");
		
		if(Func.isEmpty(mobile)){
			rv.setReturnMsg("手机号不能为空");
			rv.setReturnCode("1");
			return rv;
		}
		if(!Func.isMobile(mobile)){
			rv.setReturnMsg("手机号码无效");
			rv.setReturnCode("3");
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
		
		if(Func.isEmpty(tuijian)){
			rv.setReturnMsg("推荐人不能为空");
			rv.setReturnCode("3");
			return rv;			
		}
		
		return service.saveNewMember(tuijian, mobile, real_name, password);
	}

	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8281/rrgy/clientService";
		String param = "{\"serviceId\":\"rrgy_2017_1.0_personRegister\","
				+ "\"params\":{\"super_id\":\"2211\",\"username\":\"demo\",\"mobile\":\"10102919224\",\"code\":\"651615\",\"password\":\"123456\"}}";
		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
		rs = Des3.decode(rs);
		System.out.println(rs);
	}
}

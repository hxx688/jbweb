package com.lfgj.clinet.member;

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

@Service
@Client(name = "lfgj_changePassword")
public class MemberChangePwdClient extends RequestAbs{
	
	@Autowired
	MemberService service;
	
	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		
		String id = getParams("id","");
		String password = getParams("password","");
		String npassword = getParams("npassword","");
		String ch_type = getParams("ch_type","1");
		
		if("1".equals(ch_type)&&Func.isEmpty(password)){
			rv.setReturnMsg("登录密码不能为空");
			rv.setReturnCode("1");
			return rv;
		}
		
		if("2".equals(ch_type)&&Func.isEmpty(password)){
			rv.setReturnMsg("支付密码不能为空");
			rv.setReturnCode("1");
			return rv;
		}
		
		if(Func.isEmpty(npassword)){
			rv.setReturnMsg("新密码不能为空");
			rv.setReturnCode("2");
			return rv;
		}
		
		Member person = service.findById(id);
		MD5 md5 = new MD5();
		password = md5.enCodeByMD5(password,person.getMobile());
		
		if("1".equals(ch_type)&&!person.getPassword().equals(password)){
			rv.setReturnMsg("登录密码错误");
			rv.setReturnCode("3");
			return rv;
		}
		
		if("2".equals(ch_type)&&!person.getPay_password().equals(password)){
			rv.setReturnMsg("支付密码错误");
			rv.setReturnCode("3");
			return rv;
		}
		
		npassword = md5.enCodeByMD5(npassword,person.getMobile());
		
		if("1".equals(ch_type)){
			person.setPassword(npassword);
		}
		
		if("2".equals(ch_type)){
			person.setPay_password(npassword);
		}
		
		boolean vlc = service.update(person);
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

package com.lfgj.clinet.member;

import java.util.Date;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Service;

import com.lfgj.member.model.Member;
import com.lfgj.util.MD5;
import com.rrgy.common.iface.RegisterCodeImpl;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.AESKit;

@Service
@Client(name = "lfgj_personLogin")
public class MemberLoginClient extends RequestAbs{
	
	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		String account = getParams("username","");
		String password = getParams("password","");
		try {
			Member person = validate(account,password);
			String crypt_id = AESKit.encrypt(person.getId().toString());
			person.setCrypt_id(crypt_id);
			rv.setReturnMsg("登录成功");
			rv.setReturnParams(person);
			rv.setReturnCode("0");
		} catch (UnknownAccountException e) {
			rv.setReturnMsg("账号不存在");
			rv.setReturnCode("1");
		} catch (DisabledAccountException e) {
			rv.setReturnMsg("账号未审核");
			rv.setReturnCode("2");
		} catch (IncorrectCredentialsException e) {
			rv.setReturnMsg("密码错误");
			rv.setReturnCode("3");
		} catch (RuntimeException e) {
			rv.setReturnMsg("未知错误,请联系管理员");
			rv.setReturnCode("4");
		}
		return rv;
	}

	private Member validate(String username,String password){
		Member person = Blade.create(Member.class).findFirstBy("mobile = #{mobile}",
				Paras.create().set("mobile", username));
		// 账号不存在
		if (null == person) {
			throw new UnknownAccountException();
		}
		
		// 账号被锁定
		if (person.getStatus() == 3) {
			throw new LockedAccountException();
		}
		
		// 账号未审核
		if (person.getStatus() != 0) {
			throw new DisabledAccountException();
		}
		MD5 md5 = new MD5();
		String mds_password = md5.enCodeByMD5(password,person.getMobile());// 加密后的密码
		if (!person.getPassword().equals(mds_password)) {// 密码不相同
			throw new IncorrectCredentialsException();
		}
		
		person.setLogin_state(1);
		person.setLogin_time(new Date());
		Blade.create(Member.class).update(person);
		return person;
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8296/blade/clientService?iscrypt=1";
		String param = "{\"serviceId\":\"lfgj_personLogin\","
				+ "\"params\":{\"username\":\"13776037133\",\"password\":\"13776037133\"}}";
//		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
//		rs = Des3.decode(rs);
		System.out.println(rs);
	}
}

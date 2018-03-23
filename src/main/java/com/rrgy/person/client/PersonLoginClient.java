package com.rrgy.person.client;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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

@Service
@Client(name = "rrgy_2017_1.0_personLogin")
public class PersonLoginClient extends RequestAbs{
	
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String account = getParams("login_acount","");
		String password = getParams("password","");
		
		Subject currentUser = ShiroKit.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(account, password.toCharArray());
		try {
			currentUser.login(token);
			Person person = (Person) currentUser.getPrincipal();
			person.setPwd(password);
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
		return JsonKit.toJson(rv);
	}

	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8080/blade/clientService";
		String param = "{\"serviceId\":\"rrgy_2017_1.0_pos_orderList\","
				+ "\"params\":{\"user_id\":\"40784\"}}";
		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
		rs = Des3.decode(rs);
		System.out.println(rs);
	}
}

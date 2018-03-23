package com.rrgy.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;

/**
 * 修改登录密码
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_changeLoginPwd")
public class ChangeLoginPwdClient extends RequestAbs {
	
	@Autowired
	PersonService service;

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		String password = getParams("password", "");//原密码
		String new_password = getParams("new_password", "");//新密码
		String user_id = getParams("user_id", "");//用户id
		
		Person person = Blade.create(Person.class).findById(user_id);
		
		
		String password_md5 = ShiroKit.md5(password, person.getSalt());
		//验证原密码是否正确
		if(!password_md5.equals(person.getPassword())){
			rv.setReturnCode("2");
			rv.setReturnMsg("密码错误");
			return JsonKit.toJson(rv);
		}
		new_password = ShiroKit.md5(new_password, person.getSalt());
		
		boolean temp = Blade.create(Person.class).updateBy("password = #{password}", " id = #{id}", Paras.create().set("password", new_password).set("id", user_id));
		if (temp) {
			rv.setReturnCode("0");
			rv.setReturnMsg("修改成功");
		} else {
			rv.setReturnCode("3");
			rv.setReturnMsg("修改失败");
		}
		
		return JsonKit.toJson(rv);
	}
}

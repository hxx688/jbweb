package com.rrgy.person.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;

@Service
@Client(name = "rrgy_2017_1.0_personInfo")
public class PersonInfoClient extends RequestAbs{

	@Autowired
	PersonService service;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String person_id = getParams("person_id","");
		String group_id = getParams("group_id","");
		String[] ids = person_id.split(",");
		Person person = null;
		if(ids.length > 1){
			List<Person> personList = Blade.create(Person.class).findBy(" id in ("+person_id+")",null);
			String nickName = "";
			String mobile = "";
			for (Person p : personList) {
				nickName += ("".equals(nickName)?"":",")+p.getNick_name();
				mobile += ("".equals(mobile)?"":",")+p.getMobile();
			}
			person = new Person();
			person.setNick_name(nickName);
			person.setMobile(mobile);
		}else{
			if(Func.isEmpty(person_id)){
				rv.setReturnMsg("无效用户");
				rv.setReturnCode("1");
				return JsonKit.toJson(rv);
			}
			Paras para = Paras.create();
			String where = " (id = #{id} or mobile = #{id}) ";
			para.set("id", person_id);
			if(!Func.isEmpty(group_id)){
				where += " and group_id = #{group_id}";
				para.set("group_id", group_id);
			}
			person = Blade.create(Person.class).findFirstBy(where,para);
			
		}
		
		if(person==null){
			rv.setReturnCode("2");
			rv.setReturnMsg("会员不存在");
			rv.setReturnParams(person);
		}else{
			rv.setReturnCode("0");
			rv.setReturnMsg("登录成功");
			rv.setReturnParams(person);
		}
		return JsonKit.toJson(rv);
	}

}

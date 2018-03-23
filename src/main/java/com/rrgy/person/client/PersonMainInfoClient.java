package com.rrgy.person.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.city.model.City;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;

@Service
@Client(name = "rrgy_2017_1.0_personMainInfo")
public class PersonMainInfoClient extends RequestAbs{

	@Autowired
	PersonService service;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String person_id = getParams("person_id","");
		if(Func.isEmpty(person_id)){
			rv.setReturnMsg("无效用户");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		
		Person person = service.findById(person_id);
		if(person==null){
			person = (Person) Blade.create(Person.class).findFirstBy(" mobile="+person_id,null);
		}
		person.setShopGold("0");//天使和商家：爱心，合伙人：商家个数
		
		City city = Blade.create(City.class).findById(person.getId());
		if(city!=null){
			person.setCity_name(city.getName());
			City province = Blade.create(City.class).findById(person.getProvince());
			person.setProvince_name(province.getName());
		}
		//天使和商家：爱心数
		if(person.getGroup_id() == 1 || person.getGroup_id() == 2){
			String shopGold = Md.queryStr("xiaofeijilu.getAixinTotal", Paras.create().set("type", person.getGroup_id()).set("userId", person.getId()));
			person.setShopGold(shopGold);
		}
		//合伙人：商家个数
		if(person.getGroup_id() == 3 || person.getGroup_id() == 4 || person.getGroup_id() == 5|| person.getGroup_id() == 6|| person.getGroup_id() == 7){
			String shopGold = Md.queryStr("person.getSaleCount", Paras.create().set("group_id", 2).set("shangjilist", "%,"+person.getId()+",%"));
			person.setShopGold(shopGold);
		}
		
		rv.setReturnMsg("登录成功");
		rv.setReturnParams(person);
		rv.setReturnCode("0");
		return JsonKit.toJson(rv);
	}

}

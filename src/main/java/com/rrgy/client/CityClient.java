package com.rrgy.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.city.model.City;
import com.rrgy.common.iface.Des3;
import com.rrgy.common.iface.RegisterCodeImpl;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.service.PersonService;

@Service
@Client(name = "rrgy_2017_1.0_city")
public class CityClient extends RequestAbs{
	@Autowired
	PersonService service;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		String level = getParams("level","");
		String upid = getParams("upid","");
		if(Func.isEmpty(upid)){
			rv.setReturnMsg("无效参数");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		
		City prodvince = new City();
		prodvince.setUpid(Integer.valueOf(upid));
		List<City> prodvinces = Blade.create(City.class).findByTemplate(prodvince);
		for(City p:prodvinces){
			City city = new City();
			city.setUpid(p.getId());
			List<City> citys = Blade.create(City.class).findByTemplate(city);
			p.getCityList().addAll(citys);
		}
				
		rv.setReturnCode("0");
		rv.setReturnParams(prodvinces);
		rv.setReturnMsg("获取数据成功");
		return JsonKit.toJson(rv);
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8080/blade/clientService";
		String param = "{\"serviceId\":\"rrgy_2017_1.0_city\","
				+ "\"params\":{\"upid\":\"0\"}}";
		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
		rs = Des3.decode(rs);
		System.out.println(rs);
	}

}

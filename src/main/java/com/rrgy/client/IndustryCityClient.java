package com.rrgy.client;

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
import com.rrgy.industry.model.Industry;
import com.rrgy.person.service.PersonService;

@Service
@Client(name = "rrgy_2017_1.0_industry")
public class IndustryCityClient extends RequestAbs{
	@Autowired
	PersonService service;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		List<Industry> industrys = Blade.create(Industry.class).findAll();
		
		rv.setReturnCode("0");
		rv.setReturnParams(industrys);
		rv.setReturnMsg("获取数据成功");
		return JsonKit.toJson(rv);
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8281/rrgy/clientService";
		String param = "{\"serviceId\":\"rrgy_2017_1.0_industry\","
				+ "\"params\":{}}";
		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
		rs = Des3.decode(rs);
		System.out.println(rs);
	}

}

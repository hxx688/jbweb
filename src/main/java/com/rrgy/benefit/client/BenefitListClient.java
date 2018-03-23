package com.rrgy.benefit.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.benefit.model.Benefit;
import com.rrgy.benefit.service.BenefitService;
import com.rrgy.common.iface.Des3;
import com.rrgy.common.iface.RegisterCodeImpl;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.kit.JsonKit;

@Service
@Client(name = "rrgy_2017_1.0_benefitList")
public class BenefitListClient extends RequestAbs{
	@Autowired
	BenefitService service;

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		Blade blade = Blade.create(Benefit.class);
		List<Benefit> benefits = blade.findAll();
		rv.setReturnCode("0");
		rv.setReturnMsg("查询成功");
		rv.setReturnParams(benefits);
		return JsonKit.toJson(rv);
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8281/rrgy/clientService";
		String param = "{\"serviceId\":\"rrgy_2017_1.0_benefitList\","
				+ "\"params\":{}}";
		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
		rs = Des3.decode(rs);
		System.out.println(rs);
	}
}

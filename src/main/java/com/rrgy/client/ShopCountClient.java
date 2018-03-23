package com.rrgy.client;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.client.bean.MainInfo;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.MathKit;
import com.rrgy.person.service.PersonService;

@Service
@Client(name = "rrgy_2017_1.0_shopCount")
public class ShopCountClient extends RequestAbs{
	@Autowired
	PersonService service;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		MainInfo info = new MainInfo();
		
		String yesterday = DateTimeKit.yesterday().toString(DateTimeKit.NORM_DATE_PATTERN);
		yesterday = this.getParams("selectDate", yesterday);
		Date date = DateTimeKit.parse(yesterday, DateTimeKit.NORM_DATE_PATTERN);
		yesterday = DateTimeKit.format(date, DateTimeKit.NORM_DATE_PATTERN);
	
		rv.setReturnCode("0");
		rv.setReturnParams(info);
		rv.setReturnMsg("获取数据成功");
		return JsonKit.toJson(rv);
	}

	private String changeAmount(BigDecimal v){
		String val = "";
		if(v.longValue()>10000){
			val = MathKit.div(v,10000, 2)+"万";
		}else{
			val = v+"";
		}
		return val;
	}
}

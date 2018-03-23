package com.rrgy.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;
import com.lfgj.tixian.model.Tixianjilu;

/**
 * 新增回购
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_personBuyNew")
public class PersonBuyNewClient extends RequestAbs {
	
	@Autowired
	PersonService service;

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		String amount = getParams("amount", "");//回购数量
		String user_id = getParams("user_id", "");
		String bank = getParams("bank", "");//银行卡
		String password = getParams("password", "");// 支付密码
		double dou = Double.valueOf(amount);
		
		Person person = Blade.create(Person.class).findById(user_id);
		
		if(dou>person.getDou().doubleValue()){
			rv.setReturnCode("1");
			rv.setReturnMsg("天使豆不足");
			return JsonKit.toJson(rv);
		}
		
		String password_md5 = ShiroKit.md5(password, person.getSalt());
		//验证二级密码是否正确
		if(!password_md5.equals(person.getPassword2())){
			rv.setReturnCode("2");
			rv.setReturnMsg("密码错误");
			return JsonKit.toJson(rv);
		}
		
		double shui = 0;
		if(person.getGroup_id()>1){
			shui = dou*0.048;
		}
		double money = dou - 5 -shui;
		
		if(money<100){
			rv.setReturnCode("4");
			rv.setReturnMsg("最小金额为100");
		}
		
		Tixianjilu tixianjilu = new Tixianjilu();
		tixianjilu.setAmount(new BigDecimal(amount));
		tixianjilu.setUserId(Integer.valueOf(user_id));
		tixianjilu.setAdd_time(DateTimeKit.date());
		tixianjilu.setStatus(1);
		tixianjilu.setShui(BigDecimal.valueOf(shui));
		tixianjilu.setShouxufei(BigDecimal.valueOf(5));
		tixianjilu.setYinhangka(Integer.valueOf(bank));
		tixianjilu.setFangshi(2);
		boolean temp = service.savebuydou(tixianjilu);
		if (temp) {
			rv.setReturnCode("0");
			rv.setReturnMsg("回购成功");
		} else {
			rv.setReturnCode("3");
			rv.setReturnMsg("回购失败");
		}
		
		return JsonKit.toJson(rv);
	}
}

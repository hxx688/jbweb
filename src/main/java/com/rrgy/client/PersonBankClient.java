package com.rrgy.client;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rrgy.bank.model.PersonBank;
import com.rrgy.bank.model.PersonBankCard;
import com.rrgy.city.model.City;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * 获取我的银行卡
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_personBank")
public class PersonBankClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String user_id = getParams("user_id","");
		if(Func.isEmpty(user_id)){
			rv.setReturnMsg("无效用户");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		
		PersonBankCard personBankCard = new PersonBankCard();
		personBankCard.setUserid(Integer.valueOf(user_id));
		personBankCard.setStatus(0);
		List<PersonBank> personBanks = Blade.create(City.class).findByTemplate(personBankCard);
		rv.setReturnCode("0");
		rv.setReturnParams(personBanks);
		rv.setReturnMsg("获取数据成功");
		return JsonKit.toJson(rv);
	}

}

package com.rrgy.client;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rrgy.bank.model.PersonBank;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * 获取银行列表
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_bankList")
public class BankListClient extends RequestAbs{
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		Blade blade = Blade.create(PersonBank.class);
		List<PersonBank> personBanks = blade.findAll();
		rv.setReturnCode("0");
		rv.setReturnParams(personBanks);
		rv.setReturnMsg("获取数据成功");
		return JsonKit.toJson(rv);
	}
}

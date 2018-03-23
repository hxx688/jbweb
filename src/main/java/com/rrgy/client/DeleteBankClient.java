package com.rrgy.client;

import org.springframework.stereotype.Service;

import com.rrgy.bank.model.PersonBankCard;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * 删除银行卡
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_deletebank")
public class DeleteBankClient extends RequestAbs{
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		String bank_id = getParams("bank_id","");
		
		if(Func.isEmpty(bank_id)){
			rv.setReturnMsg("无效银行卡");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		
		
		boolean temp = Blade.create(PersonBankCard.class).updateBy("status = 1", "id = #{id}", Paras.create().set("id", bank_id));
		if (temp) {
			rv.setReturnCode("0");
			rv.setReturnMsg("删除成功");
		} else {
			rv.setReturnCode("3");
			rv.setReturnMsg("删除失败");
		}
		
		return JsonKit.toJson(rv);
	}
}

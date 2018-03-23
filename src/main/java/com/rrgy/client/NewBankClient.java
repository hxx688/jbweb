package com.rrgy.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.bank.model.PersonBankCard;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.common.util.BankUtil;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;
import com.rrgy.sms.service.SmsService;

/**
 * 新增银行卡
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_addbank")
public class NewBankClient extends RequestAbs{
	@Autowired
	PersonService service;
	@Autowired
	SmsService smsService;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		String bank_name = getParams("bank_name","");
		String bank_num = getParams("bank_num","");
		String person_name = getParams("person_name","");
		String bank_address = getParams("bank_address","");
		String user_id = getParams("user_id","");
		String code = getParams("code", "");
		
		if(Func.isEmpty(user_id)){
			rv.setReturnMsg("无效用户");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		
		if(Func.isEmpty(bank_name)){
			rv.setReturnMsg("无效开户行");
			rv.setReturnCode("2");
			return JsonKit.toJson(rv);
		}
		
		Person userP = service.findById(user_id);
		if(userP==null){
			rv.setReturnMsg("会员不存在");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		
		boolean vlc = smsService.findValidSms(userP.getMobile(), code);
		if(!vlc){
			rv.setReturnMsg("无效验证码");
			rv.setReturnCode("2");
			return JsonKit.toJson(rv);
		}
		
		AjaxResult result = BankUtil.verifyBankCark(bank_name, person_name, bank_num);
		
		if(result.getCode() != 0){
			rv.setReturnCode("4");
			rv.setReturnMsg("银行卡验证失败");
			return JsonKit.toJson(rv);
		}
		
		PersonBankCard personBankCard = new PersonBankCard();
		personBankCard.setKaihuhang(bank_name);
		personBankCard.setUserid(Integer.valueOf(user_id));
		personBankCard.setKahao(bank_num);
		personBankCard.setXiangxindizhi(bank_address);
		personBankCard.setKaihuming(person_name);
		personBankCard.setStatus(0);
		
		boolean temp = Blade.create(PersonBankCard.class).save(personBankCard);
		if (temp) {
			rv.setReturnCode("0");
			rv.setReturnMsg("新增成功");
			//更新验证码状态
			smsService.updateSms(userP.getMobile());
		} else {
			rv.setReturnCode("3");
			rv.setReturnMsg("新增失败");
		}
		
		return JsonKit.toJson(rv);
	}
}

package com.lfgj.clinet.member;

import org.springframework.stereotype.Service;

import com.lfgj.member.model.Member;
import com.lfgj.util.LfConstant;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.system.model.Parameter;

@Service
@Client(name = "lf_person")
public class MemberInfoClient extends RequestMethod{
	
	public ResultVo info() {
		ResultVo rv = new ResultVo();
		String id = getParams("id","");
		Member person = Blade.create(Member.class).findById(id);
		
		Parameter param = new Parameter();
		param.setCode(LfConstant.PARAM_107);
		param.setStatus(1);
		param = Blade.create(Parameter.class).findTopOne(param);
		
		person.setCashFee(param.getPara());
		
		rv.setReturnCode("0");
		rv.setReturnParams(person);
		rv.setReturnMsg("");
		return rv;
	}
	
	public ResultVo shares() {
		ResultVo rv = new ResultVo();
		String id = getParams("id","");
		Member person = Blade.create(Member.class).findById(id);
		String domain = ConstConfig.pool.get("config.domain");
		String url = domain +"/register/"+person.getId();
		rv.setReturnCode("0");
		rv.setReturnParams(url);
		rv.setReturnMsg("");
		return rv;
	}
	
	public ResultVo payInfo(){
		ResultVo rv = new ResultVo();
		String id = getParams("id","");
		String phone = getParams("phone","");
		String idcard = getParams("idcard","");
		String account = getParams("account","");
		String kaihuming = getParams("kaihuming","");
		
		Member person = Blade.create(Member.class).findById(id);
		if(person==null){
			rv.setReturnMsg("会员不存在");
			rv.setReturnCode("1");
			return rv;
		}
		if(Func.isEmpty(phone)){
			rv.setReturnMsg("手机号不能为空");
			rv.setReturnCode("1");
			return rv;
		}
		if(!Func.isMobile(phone)){
			rv.setReturnMsg("手机号码无效");
			rv.setReturnCode("3");
			return rv;
		}
		
		person.setIdcart(idcard);
		person.setBank_mobile(phone);
		person.setBank_acount(account);
		person.setKaihuming(kaihuming);
		
		boolean rs = Blade.create(Member.class).update(person);
		if(rs){
			rv.setReturnCode("0");
			rv.setReturnMsg("修改成功");
		}else{
			rv.setReturnCode("4");
			rv.setReturnMsg("修改失败");
		}
		return rv;
	}
}

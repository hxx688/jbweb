package com.lfgj.clinet.withdraw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.member.model.Member;
import com.lfgj.tixian.model.Tixianjilu;
import com.lfgj.tixian.model.Yinhangka;
import com.lfgj.tixian.service.TixianjiluService;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.lfgj.util.MD5;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Db;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.system.model.Parameter;

@Service
@Client(name = "lf_withdraw")
public class WithdrawClient extends RequestMethod{
	@Autowired
	TixianjiluService service;
	
	public ResultVo index() {
		ResultVo rv = new ResultVo();
		String id = getParams("id","");
	
		Member person = Blade.create(Member.class).findById(id);
		
		Parameter param = new Parameter();
		param.setCode(LfConstant.PARAM_107);
		param.setStatus(1);
		param = Blade.create(Parameter.class).findTopOne(param);
		
		person.setCashFee(param.getPara());
		
		String sql = "select id value,name text from dt_yinhang where 1=1";
		List<Map> banks = Db.selectList(sql);
		
		Yinhangka yinghanka = new Yinhangka();
		yinghanka.setUserid(person.getId());
		yinghanka.setStatus(0);
		yinghanka = Blade.create(Yinhangka.class).findTopOne(yinghanka);
		
		Map<String,Object> rs = new HashMap<String,Object>();
		rs.put("person", person);
		rs.put("banks", banks);
		rs.put("yinghanka", yinghanka);
		
		rv.setReturnCode("0");
		rv.setReturnParams(rs);
		rv.setReturnMsg("");
		return rv;
	}
	
	public ResultVo add(){
		ResultVo rv = new ResultVo();
		
		boolean isMoney = CommKit.isMoney();		
		if(!isMoney){
			rv.setReturnCode("1");
			rv.setReturnMsg("非提现时间");
			return rv;
		}
		
		
		String id = getParams("id","");
		String amount = getParams("amount","");
		String paypwd = getParams("paypwd","");
		String bank_acount = getParams("bank_acount","");
		String bank_name = getParams("bank_name","");
		String bindmobile = getParams("bindmobile","");
		String idcard = getParams("idcard","");
		String ka_id = getParams("ka_id","");
		
		String 省市 = getParams("city","");		
		Tixianjilu 提现 = new Tixianjilu();
		if(!Func.isEmpty(省市)){
			String[] 省市组 = 省市.split(" ");
			提现.setSheng(省市组[0]);
			提现.setShi(省市组[1]);
		}
		String 支行 = getParams("zhihang","");	
		提现.setZhihang(支行);
		Member person = Blade.create(Member.class).findById(id);

		if(person==null){
			rv.setReturnCode("2");
			rv.setReturnMsg("会员不存在");
			return rv;
		}

		person.setIdcart(idcard);
		person.setBank_acount(bank_acount);
		person.setBank_name(bank_name);
		if(Func.isEmpty(bindmobile)){
			person.setBank_mobile(person.getMobile());
		}else{
			person.setBank_mobile(bindmobile);
		}
		
		String today = DateKit.getDay();
		Paras paras = Paras.create().set("userId", person.getId());		
		int ct = Blade.create(Tixianjilu.class).count(" where userId=#{userId} and add_time like '"+today+"%'", paras);
		
		if(ct>0){
			rv.setReturnCode("5");
			rv.setReturnMsg("每天只能提现一次");
			return rv;
		}
		
		MD5 md5 = new MD5();
		String pwd = md5.enCodeByMD5(paypwd,person.getMobile());// 加密后的密码
		
		if (!person.getPay_password().equals(pwd)) {// 密码不相同
			rv.setReturnCode("5");
			rv.setReturnMsg("提现密码错误");
			return rv;
		}
		
		if(Func.isEmpty(amount)){
			rv.setReturnCode("1");
			rv.setReturnMsg("金额不能为空");
			return rv;
		}
	
		if(Float.valueOf(amount) % 100 != 0){
			rv.setReturnCode("3");
			rv.setReturnMsg("只能取100的倍数");
			return rv;
		}
		
		if(person.getAmount().floatValue()<Float.valueOf(amount)){
			rv.setReturnCode("4");
			rv.setReturnMsg("余额不足");
			return rv;
		}
		
		boolean rs = service.saveTixain(person, amount,提现,ka_id);
		
		if(rs){
			rv.setReturnCode("0");
			rv.setReturnMsg("提现申请成功");
		}else{
			rv.setReturnCode("5");
			rv.setReturnMsg("提现申请失败");
		}
		
		return rv;
	}
	
	
	public ResultVo list(){
		ResultVo rv = new ResultVo();
		String id = getParams("id","");
		String page = getParams("pageNo","1");
		String rows = getParams("pageSize", LfConstant.PAGE_SIZE);
		Map<String,String> param = new HashMap<String,String>();
		param.put("userId_equal",id);
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "tixianjilu.queryList",para,"","t.add_time desc");	
		rv.setReturnCode("0");
		rv.setReturnParams(grid);
		return rv;
	}
	
}

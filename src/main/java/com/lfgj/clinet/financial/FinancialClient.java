package com.lfgj.clinet.financial;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.financial.model.Financial;
import com.lfgj.financial.service.FinancialService;
import com.lfgj.util.LfConstant;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.JsonKit;

@Service
@Client(name = "lf_financial")
public class FinancialClient extends RequestMethod{
	@Autowired
	FinancialService service;
	
	public ResultVo list(){
		ResultVo rv = new ResultVo();
		String id = getParams("id","");
		String page = getParams("pageNo","1");
		String rows = getParams("pageSize",  LfConstant.PAGE_SIZE);
		Map<String,String> param = new HashMap<String,String>();
		param.put("user_id_equal",id);
		String para = JsonKit.toJson(param);
		
		String sql = "select COALESCE(SUM(amount),0) amount from dt_financial where user_id=#{user_id} and financial_type = 1 ";
		Financial f1 = Blade.create(Financial.class).findFirst(sql, Paras.create().set("user_id", id));
		long sr = f1.getAmount().longValue();
		
		sql = "select COALESCE(SUM(amount),0) amount from dt_financial where user_id=#{user_id} and financial_type = 2 ";		
		Financial f2 = Blade.create(Financial.class).findFirst(sql, Paras.create().set("user_id", id));
		long zc = f2.getAmount().longValue();
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "financial.listUser",para,"","create_time desc");	
		
		Map<String,Object> rs = new HashMap<String,Object>();
		rs.put("grid", grid);
		rs.put("sr", sr);
		rs.put("zc", zc);
		rv.setReturnCode("0");
		rv.setReturnParams(rs);
		return rv;
	}
	
}

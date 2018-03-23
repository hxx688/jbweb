package com.rrgy.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.grid.JqGrid;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * 商家营业额(商家)
 * @author Administrator
 *
 */

@Service
@Client(name = "rrgy_2017_1.0_personShopOrderList")
public class PersonShopOrderListClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String user_id = getParams("user_id", "");
		String bili = getParams("bili", "1");
		String search_type = getParams("search_type","");//1一个月内,３三个月内
		
		if(Func.isEmpty(user_id)){
			rv.setReturnCode("1");
			rv.setReturnMsg("会员不存在");
			return JsonKit.toJson(rv);
		}
		Map<String,String> param = new HashMap<String,String>();
		Paras p = Paras.create();
		
		if("1".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -1).toString();
			param.put("reg_time_gt", time);
			p.set("reg_time", time);
		}
		if("3".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -3).toString();
			param.put("reg_time_gt", time);
			p.set("reg_time", time);
		}

		param.put("group_id_equal", "2");
		param.put("shangjilist", ","+user_id+",");
		param.put("bili_skip", bili);
		param.put("bili", ","+bili+",");
		p.set("group_id", "2");
		p.set("shangjilist", ","+user_id+",");
		p.set("bili", ","+bili+",");
		p.set("payment_status", 2);
		
		String para = JsonKit.toJson(param);

		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "person.listHhrOrder",para);	
		JqGrid<Map<String, Object>> gd = (JqGrid<Map<String, Object>>)grid;
		String bean = Md.queryStr("person.getSumAmount", p);
		gd.setBean(bean);//总计营业额
		
		rv.setReturnParams(gd);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取营业额成功");
		return JsonKit.toJson(rv);
	}

}

package com.rrgy.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.grid.JqGrid;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * 让利列表
 * @author Administrator
 *
 */

@Service
@Client(name = "rrgy_2017_1.0_personRangliList")
public class PersonRangliListClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String user_id = getParams("user_id", "");
		String search_type = getParams("search_type","");//1一个月内,2三个月内
		String type = getParams("type","1");//1待缴让利,2已缴让利
		
		Map<String,String> param = new HashMap<String,String>();
		Paras p = Paras.create();
		if("1".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -1).toString();
			param.put("add_time_gt", time);
			p.set("add_time", time);
		}
		if("3".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -3).toString();
			param.put("add_time_gt", time);
			p.set("add_time", time);
		}
		
		param.put("maijiaId_equal", user_id);
		param.put("payment_status_equal", type);
		p.set("maijiaId", user_id);
		
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "order.list",para,"add_time","desc");	
		JqGrid<Map<String, Object>> gd = (JqGrid<Map<String, Object>>)grid;
		String dRangli = Md.queryStr("order.getOrderSumRangli", p.set("payment_status", 1));
		String yRangli = Md.queryStr("order.getOrderSumRangli", p.set("payment_status", 2));
		gd.setBean(dRangli+","+yRangli);//待缴让利,已缴让利
		
		rv.setReturnParams(gd);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取让利列表成功");
		return JsonKit.toJson(rv);
	}

}

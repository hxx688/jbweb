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
 * 营业额(订单列表)
 * @author Administrator
 *
 */

@Service
@Client(name = "rrgy_2017_1.0_personOrderList")
public class PersonOrderListClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String user_id = getParams("user_id", "");
		String bili = getParams("bili", "");
		String search_type = getParams("search_type","");//1一个月内,2三个月内
		Paras p = Paras.create();
		Map<String,String> param = new HashMap<String,String>();
		if("1".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -1).toString();
			param.put("payment_time_gt", time);
			p.set("payment_time", time);
		}
		if("3".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -3).toString();
			param.put("payment_time_gt", time);
			p.set("payment_time", time);
		}
		param.put("maijiaId_equal", user_id);
		param.put("payment_status_equal", "2");
		if(!Func.isEmpty(bili)){
			param.put("bili_equal", bili);
			p.set("bili", bili);
		}
		p.set("maijiaId", user_id);
		p.set("payment_status", 2);
		
		String para = JsonKit.toJson(param);

		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "order.list",para,"add_time","desc");	
		JqGrid<Map<String, Object>> gd = (JqGrid<Map<String, Object>>)grid;
		String bean = Md.queryStr("order.getOrderSumAmount", p);
		gd.setBean(bean);//总计营业额
		
		rv.setReturnParams(gd);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取营业额成功");
		return JsonKit.toJson(rv);
	}

}

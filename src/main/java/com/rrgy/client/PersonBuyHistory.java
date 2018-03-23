package com.rrgy.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.interfaces.IQuery;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.lfgj.tixian.intercept.TixianjiluIntercept;

/**
 * 获取回购记录
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_personBuyHistory")
public class PersonBuyHistory extends RequestAbs {

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		String search_type = getParams("search_type", "");//查询类型,1:一个月内，2:三个月内,其他：全部
		String page = getParams("page", "1");
		String rows = getParams("rows", "20");
		String user_id = getParams("user_id", "");
		String status = getParams("status", "");// 类别
		
		Map<String,String> param = new HashMap<String,String>();

		param.put("userId_skip", user_id);
		param.put("status_equal", status);
		
		if("1".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -1).toString();
			param.put("add_time_gt", time);
		}
		if("3".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -3).toString();
			param.put("add_time_gt", time);
		}
		
		
		String para = JsonKit.toJson(param);
		IQuery query = new TixianjiluIntercept();
		Object grid = GridManager.paginate(null,Integer.valueOf(page), Integer.valueOf(rows), "tixianjilu.queryClientList",para,"add_time","desc",query,null);	
		
		rv.setReturnCode("0");
		rv.setReturnParams(grid);
		rv.setReturnMsg("获取数据成功");
		return JsonKit.toJson(rv);
	}
}

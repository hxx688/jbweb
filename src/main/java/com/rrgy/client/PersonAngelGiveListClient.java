package com.rrgy.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.rrgy.common.iface.Des3;
import com.rrgy.common.iface.RegisterCodeImpl;
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
 * 赠送记录（获赠、转赠）
 * @author Administrator
 *
 */

@Service
@Client(name = "rrgy_2017_1.0_personAngelGiveList")
public class PersonAngelGiveListClient extends RequestAbs{

	@SuppressWarnings("unchecked")
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String user_id = getParams("user_id", "");
		String get_user_id = getParams("get_user_id", "");
		String search_type = getParams("search_type", "");	//查询类型,1:一个月内，2:三个月内,其他：全部
		Paras paras = Paras.create();
		Map<String,String> param = new HashMap<String,String>();

		if(!StringUtils.isEmpty(user_id)){
			param.put("userId_equal", user_id);		//送出
			paras.set("userId", user_id);
		}
		if(!StringUtils.isEmpty(get_user_id)){
			param.put("userId2_equal", get_user_id);	//接收
			paras.set("userId2", get_user_id);
		}
		
		if("1".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -1).toString();
			param.put("add_time_gt", time);
			paras.set("add_time", time);
		}
		if("2".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -3).toString();
			param.put("add_time_gt", time);
			paras.set("add_time", time);
		}
		
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "zengsongjilu.list",para,"add_time","desc");	
		JqGrid<Map<String, Object>> gd = (JqGrid<Map<String, Object>>)grid;
		
//		if(!StringUtils.isEmpty(user_id)){
			String total = Md.queryStr("zengsongjilu.getDouTotal", paras);
			gd.setBean(total);//总计爱心豆
//		}
		
		rv.setReturnParams(gd);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取转赠记录成功");
		return JsonKit.toJson(rv);
	}
	
	
	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8080/blade/clientService";
		String param = "{\"serviceId\":\"rrgy_2017_1.0_yesterdayJili\","
				+ "\"params\":{\"user_id\":\"3\"}}";
		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
		rs = Des3.decode(rs);
		System.out.println(rs);
	}

}

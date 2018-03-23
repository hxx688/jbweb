package com.rrgy.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * 推荐记录
 * @author Administrator
 *
 */

@Service
@Client(name = "rrgy_2017_1.0_recommendList")
public class RecommendListClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String user_id = getParams("user_id", "");
		String search_type = getParams("search_type", "");	//查询类型,1:一个月内，2:三个月内,其他：全部
		Map<String,String> param = new HashMap<String,String>();

		param.put("shangjiId_equal", user_id);
		if("1".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -1).toString();
			param.put("reg_time_gt", time);
		}
		if("3".equals(search_type)){
			String time = DateTimeKit.offsiteMonth(new Date(), -3).toString();
			param.put("reg_time_gt", time);
		}
		
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "person.listRecommend",para,"reg_time","desc");	
		rv.setReturnParams(grid);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取推荐记录成功");
		return JsonKit.toJson(rv);
	}

}

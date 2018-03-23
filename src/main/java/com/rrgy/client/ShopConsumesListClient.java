package com.rrgy.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * 公益消费
 * @author Administrator
 *
 */

@Service
@Client(name = "rrgy_2017_1.0_shopConsumesList")
public class ShopConsumesListClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String bili = getParams("bili","");
		String user_id = getParams("user_id", "");
		
		Map<String,String> param = new HashMap<String,String>();

		param.put("user_id_equal", user_id);
		param.put("bili_equal", bili);
		
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "order.list",para,"payment_time","desc");	
		rv.setReturnParams(grid);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取消费记录成功");
		return JsonKit.toJson(rv);
	}

}

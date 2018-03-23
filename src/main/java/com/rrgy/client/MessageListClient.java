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
 * 我的消息
 * @author Administrator
 *
 */

@Service
@Client(name = "rrgy_2017_1.0_messageList")
public class MessageListClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String type = getParams("type","");//1消费记录，２回购审核，３意见反馈，４转赠消息
		String user_id = getParams("user_id", "");
		Map<String,String> param = new HashMap<String,String>();

		param.put("accept_user_name_equal", user_id);
		param.put("type_equal", type);
		
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "person_message.list",para);	
		
		rv.setReturnParams(grid);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取消息成功");
		return JsonKit.toJson(rv);
	}

}

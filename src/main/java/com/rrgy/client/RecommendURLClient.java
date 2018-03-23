package com.rrgy.client;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.kit.JsonKit;


@Service
@Client(name = "rrgy_2017_1.0_recommendURL")
public class RecommendURLClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String user_id = getParams("user_id", "");
		
		//天使推荐地址
		String angel_recommend_url = "http://www.szrrgy.com/person/register?type=1&referrer="+user_id;
		
		rv.setReturnCode("0");
		rv.setReturnParams(angel_recommend_url);
		rv.setReturnMsg("获取数据成功");
		return JsonKit.toJson(rv);
	}

}

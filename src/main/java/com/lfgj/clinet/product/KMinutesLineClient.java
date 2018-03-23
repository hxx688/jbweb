package com.lfgj.clinet.product;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.util.CommKit;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;

@Service
@Client(name = "lf_kMinutesline")
public class KMinutesLineClient extends RequestAbs{

	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		
		String type = this.getParams("type", "");
		String code = this.getParams("code", "");
			
		Collection<JSONObject> minutes_data=CommKit.getJson(code,"3","5");
		Collection<JSONObject> thirtyminutes_data=CommKit.getJson(code,"3","30");
		Collection<JSONObject> hour_data=CommKit.getJson(code,"3","60");
		
		Map<String,Collection<JSONObject>> rs = new HashMap<String,Collection<JSONObject>>();
		rs.put("minutes", minutes_data);
		rs.put("thirtyminutes", thirtyminutes_data);
		rs.put("hour", hour_data);
		
		rv.setReturnCode("0");
		rv.setReturnParams(rs);
		rv.setReturnMsg("");
		return rv;
	}
	
}

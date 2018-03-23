package com.lfgj.clinet.product;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.product.util.ProductCacheUtil;
import com.lfgj.util.CommKit;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.Func;

@Service
@Client(name = "lf_kline")
public class KLineClient extends RequestAbs{

	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		
		String type = this.getParams("type", "");
		String code = this.getParams("code", "");
		
		Collection<JSONObject> day_data=getJson(code,"0",null);
		Collection<JSONObject> week_data=getJson(code,"2",null);	
		
		Map<String,Collection<JSONObject>> rs = new HashMap<String,Collection<JSONObject>>();
		rs.put("day", day_data);
		rs.put("week", week_data);
		
		rv.setReturnCode("0");
		rv.setReturnParams(rs);
		rv.setReturnMsg("");
		return rv;
	}
	
	private Collection<JSONObject> getJson(String code,String type,String q_type){
		String key_type=type;
		if(!Func.isEmpty(q_type)){
			key_type = type +"_"+ q_type;
		}
		Map<String, JSONObject> minutes_json = ProductCacheUtil.init(code+"_"+key_type).all();
		if(minutes_json==null){
			minutes_json = CommKit.fillList(code,type,q_type,0);
		}
		
		Collection<JSONObject> minutes_data=null;
		if(minutes_json!=null){
			minutes_data = minutes_json.values();
		}
		return minutes_data;
	}
}

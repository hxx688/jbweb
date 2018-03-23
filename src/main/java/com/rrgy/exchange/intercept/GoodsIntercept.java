package com.rrgy.exchange.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.toolbox.grid.BladePage;

public class GoodsIntercept extends PageIntercept {
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			map.put("statusname", loadStatusName(map.get("status").toString()));
		}
	}
	
	//0下架，1上架
	private String loadStatusName(String status){
		if("0".equals(status)){
			return "<span style='color:red;'>已下架</span>";
		}else if("1".equals(status)){
			return "<span style='color:green;'>已上架</span>";
		}
		return "";
	}
}

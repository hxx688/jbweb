package com.rrgy.exchange.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.toolbox.grid.BladePage;

public class OrdersIntercept extends PageIntercept {
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			map.put("statusname", loadStatusName(map.get("status").toString()));
		}
	}
	
	//0:待付款,1:待发货,2:已发货,3:已确认
	private String loadStatusName(String status){
		if("0".equals(status)){
			return "<span style='color:#9932CD;'>待付款</span>";
		}else if("1".equals(status)){
			return "<span style='color:red;'>待发货</span>";
		}else if("2".equals(status)){
			return "<span style='color:blue;'>已发货</span>";
		}else if("3".equals(status)){
			return "<span style='color:green;'>已确认</span>";
		}
		return "";
	}
}

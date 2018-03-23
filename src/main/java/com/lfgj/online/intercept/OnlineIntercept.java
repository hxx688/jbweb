package com.lfgj.online.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.toolbox.grid.BladePage;

public class OnlineIntercept extends PageIntercept{
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			map.put("status_name", loadStatusName(map.get("status").toString()));
		}
		
	}
	
	private String loadStatusName(String status){
		if("1".equals(status)){
			return "<span style='color:blue;'>已回复</span>";
		}else{
			return "<span style='color:red;'>待回复</span>";
		}
	}

}

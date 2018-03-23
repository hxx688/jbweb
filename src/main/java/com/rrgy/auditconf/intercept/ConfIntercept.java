package com.rrgy.auditconf.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.toolbox.grid.BladePage;

public class ConfIntercept extends PageIntercept {
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			map.put("MODELTYPENAME", loadModelTypeName(map.get("MODEL_TYPE").toString()));
			map.put("AUDITLEVELNAME", loadAuditLevelName(map.get("AUDIT_LEVEL").toString()));
		}
	}
	
	
	private String loadModelTypeName(String type){
		if("0".equals(type)){
			return "<span style='color:red;'>订单审核</span>";
		}else if("1".equals(type)){
			return "<span style='color:green;'>提现审核</span>";
		}else if("2".equals(type)){
			return "<span style='color:blue;'>申请审核</span>";
		}
		return "";
	}
	
	private String loadAuditLevelName(String type){
		if("1".equals(type)){
			return "<span>一级审核</span>";
		}else if("2".equals(type)){
			return "<span>二级审核</span>";
		}else if("3".equals(type)){
			return "<span>三级审核</span>";
		}
		return "";
	}
}

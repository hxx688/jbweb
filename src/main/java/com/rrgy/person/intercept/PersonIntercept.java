package com.rrgy.person.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.toolbox.grid.BladePage;

public class PersonIntercept extends PageIntercept {
	
	public void queryBefore(AopContext ac) {
		
		String sql = "";
		if(null != ac.getParam().get("hangye_skip")){
			sql += " and hangye in ("+ac.getParam().get("hangye_skip")+")";
		}
		ac.setCondition(sql);
	}
	
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		
	}
	
	
	private String loadLevelName(String level){
		if("1".equals(level)){
			return "<span>一级审核</span>";
		}else if("2".equals(level)){
			return "<span>二级审核</span>";
		}else if("3".equals(level)){
			return "<span>三级审核</span>";
		}else{
			return "<span>一级审核</span>";
		}
	}
	
	//0正常,1待验证,2待审核,3锁定
	private String loadStatusName(String status){
		if("0".equals(status)){
			return "<span style='color:green;'>正常</span>";
		}else if("1".equals(status)){
			return "<span style='color:blue;'>待验证</span>";
		}else if("2".equals(status)){
			return "<span style='color:blue;'>待审核</span>";
		}else if("3".equals(status)){
			return "<span style='color:red;'>锁定</span>";
		}
		return "";
	}
}

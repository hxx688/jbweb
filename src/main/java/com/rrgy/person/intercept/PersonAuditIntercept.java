package com.rrgy.person.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.grid.BladePage;

public class PersonAuditIntercept extends PageIntercept {
	
	public void queryBefore(AopContext ac) {
		// 获取当前登录用户
		ShiroUser u = ShiroKit.getUser();
		String condition = " and FIND_IN_SET(#{user_id},(select group_concat(user_id) from dt_audit_conf a where a.audit_level=t.audit_level and a.model_type=2))";
		ac.setCondition(condition);
		ac.getParam().put("user_id",u.getId());
	}
	
	
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			map.put("STATUSNAME", loadStatusName(map.get("STATUS").toString()));
			map.put("LEVELNAME", loadLevelName(map.get("AUDIT_LEVEL").toString()));
		}
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

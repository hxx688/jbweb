package com.rrgy.system.meta.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.constant.ConstShiro;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.grid.BladePage;

public class RoleIntercept extends PageIntercept {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String roles = ShiroKit.getUser().getRoles() + "," + ShiroKit.getUser().getSubRoles();
			String condition = "and id in (#{join(ids)})";
			ac.setCondition(condition);
			ac.getParam().put("ids", roles.split(","));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			String id = map.get("id").toString();
			if("-1".equals(id)){
				map.put("NAME", loadTypeName(map.get("NAME").toString()));
			}
		}
		
	}
	
	private String loadTypeName(String status){
		return "<span style='color:red' >"+status+"(系统)</span>";
	}
}

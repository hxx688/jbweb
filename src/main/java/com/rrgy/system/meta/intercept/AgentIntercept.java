package com.rrgy.system.meta.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.grid.BladePage;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.model.User;

public class AgentIntercept extends PageIntercept {
	public String status;
	public String member_id;
	
	public void queryBefore(AopContext ac) {
		String condition = "and is_agent = 1";
		
		ShiroUser shiroUser = ShiroKit.getUser();
		User user = Blade.create(User.class).findById(shiroUser.getId());
		Object mobile = ac.getParam().get("mobile");
		Object real_name = ac.getParam().get("real_name");
		
		if(!Func.isEmpty(member_id)){
			condition += " and parent_id = " + member_id;
		}else if(user.getIs_agent() == 1){
			condition += " and parent_id = " + user.getMember_id();
		}else if("2,4".equals(status)){
			
		}else if(mobile==null&&real_name==null){
			condition += " and level = 1";
		}
		
		if(StrKit.isNotEmpty(status)){
			condition += " and status in (" + status + ")";
		}
		
		ac.setCondition(condition);
	}

	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		ShiroUser shiroUser = ShiroKit.getUser();
		User user = Blade.create(User.class).findById(shiroUser.getId());
		if(user.getRoleid().equals("-1")){
			for (Map<String, Object> map : list) {
				map.put("mobile", StrKit.hidePhone((String)map.get("mobile")));
				map.put("bank_mobile", StrKit.hidePhone((String)map.get("bank_mobile")));
			}
		}
	}
}

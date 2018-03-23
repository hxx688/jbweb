package com.rrgy.system.meta.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Db;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.grid.BladePage;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.model.User;

public class MyMemberIntercept extends PageIntercept {
	public Integer agentid;
	public Integer member_id;
	public String is_agent;

	public void queryBefore(AopContext ac) {
		String condition = "";
		if(!Func.isEmpty(agentid)){
			condition += " and agent_id = #{agent_id}";
			ac.setCondition(condition);
			ac.getParam().put("agent_id", agentid);
		}
		if(!Func.isEmpty(member_id)){
			condition += " and parent_id = #{member_id}";
			ac.setCondition(condition);
			ac.getParam().put("member_id", member_id);
		}
		if(!Func.isEmpty(is_agent)){
			condition += " and is_agent = #{is_agent}";
			ac.setCondition(condition);
			ac.getParam().put("is_agent", is_agent);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		ShiroUser shiroUser = ShiroKit.getUser();
		User user = Blade.create(User.class).findById(shiroUser.getId());
		if(user.getIs_agent() == 1){
			List<Map<String, Object>> list = page.getRows();
			for (Map<String, Object> map : list) {
				map.put("mobile", StrKit.hidePhone((String)map.get("mobile")));
			}
		}
	}
}

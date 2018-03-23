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

public class MemberIntercept extends PageIntercept {

	public Integer is_agent;
	public String status;
	public void queryBefore(AopContext ac) {
		ShiroUser shiroUser = ShiroKit.getUser();
		User user = Blade.create(User.class).findById(shiroUser.getId());
		
		String condition = " and 1=1";
		
		if(is_agent != null){
			condition +=  " and is_agent = " + is_agent;
		}else{
			condition +=  " and is_agent != 1 ";
		}
		
		if(StrKit.isNotEmpty(status)){
			condition += " and status in (" + status + ")";
		}
		
		Map<String, Object> params = ac.getParam();
		Object user_id = params.get("user_id_skip");
		if(!Func.isEmpty(user_id)){
			condition += " and (id = '"+user_id+"' or real_name like '%"+user_id+"%')";
		}
		
		if(user.getIs_agent() == 1){
			condition += " and FIND_IN_SET("+user.getMember_id()+",parent_ids)";
//			condition += " and agent_id = " + user.getId();
		}
		ac.setCondition(condition);
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
				map.put("bank_mobile", StrKit.hidePhone((String)map.get("bank_mobile")));
			}
		}
		
		
		String sql = "SELECT COALESCE (SUM(amount), 0) amount from lf_member where is_agent = 0";
		sql += ac.getCondition();
		sql += ac.getSqlEx();

		List<Map> fee = Db.selectList(sql,ac.getParam());
		String bean = fee.get(0).get("amount").toString();
		page.setBean(bean);
		
	}
	
}

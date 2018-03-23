package com.lfgj.tixian.intercept;

import java.util.List;
import java.util.Map;

import com.lfgj.tixian.model.Tixianjilu;
import com.lfgj.tixian.util.PayUtil;
import com.lfgj.util.LfConstant;
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


public class TixianjiluIntercept extends PageIntercept {
	
	private String userId;
	
	public void queryBefore(AopContext ac) {
		
		ShiroUser shiroUser = ShiroKit.getUser();
		User user = Blade.create(User.class).findById(shiroUser.getId());
		
		String sql = "";
		
		if(null != ac.getParam().get("userId_skip")){
			sql += " and t.userId = #{userId_skip}";
		}
		if(!Func.isEmpty(userId)){
			sql += " and t.userId = "+userId;
		}
		if(null != ac.getParam().get("status_skip")){
			sql += " and t.status = #{status_skip}";
		}
		if(null != ac.getParam().get("user_name_skip")){
			sql += " and u.real_name like '%"+ac.getParam().get("user_name_skip")+"%'";
		}
		
		if(LfConstant.agent_role.equals(user.getRoleid())){
//			sql += " and u.agent_id = "+user.getId();
			sql += " and FIND_IN_SET("+user.getMember_id()+",u.parent_ids)";
		}
		
		Map<String, Object> params = ac.getParam();
		Object user_id = params.get("user_id_skip");
		if(!Func.isEmpty(user_id)){
			sql += " and (t.userId = '"+user_id+"' or u.real_name like '%"+user_id+"%')";
		}
		
		Object agent_name = params.get("agent_name_skip");
		if(!Func.isEmpty(agent_name)){
			sql += " and (u.agent_id = '"+agent_name+"' or u.agent_name like '%"+agent_name+"%')";
		}
		ac.setCondition(sql);
	}
	
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		
		ShiroUser shiroUser = ShiroKit.getUser();
		User user = Blade.create(User.class).findById(shiroUser.getId());
		
		for (Map<String, Object> map : list) {			
			if("5".equals(map.get("STATUS").toString())){
				String orderNo = map.get("tixianNum").toString();
				String data = PayUtil.check(orderNo);
				
				String id = map.get("id").toString();
				Tixianjilu txjl = new Tixianjilu();
				txjl.setId(Integer.valueOf(id));
				if("1".equals(data)){			
					txjl.setStatus(2);
					Blade.create(Tixianjilu.class).update(txjl);
					map.put("STATUS","2");					
				}
				if("2".equals(data)){
					txjl.setStatus(6);
					Blade.create(Tixianjilu.class).update(txjl);
					map.put("STATUS","6");					
				}
			}
			map.put("STATUSNAME", loadStatusName(map.get("STATUS").toString()));
			
			if(user.getIs_agent() == 1){
				map.put("mobile", StrKit.hidePhone((String)map.get("mobile")));
			}
		}
		String sql = "SELECT COALESCE (SUM(t.shijiamount), 0) shijiamount,COALESCE (SUM(t.shouxufei), 0) shouxufei,COALESCE (SUM(t.amount), 0) amount from dt_tixianjilu t left join lf_member u on u.id=t.userId where u.is_agent = 0";
		sql += ac.getCondition();
		sql += ac.getSqlEx();
		
		List<Map> fee = Db.selectList(sql,ac.getParam());
		String bean = fee.get(0).get("shijiamount").toString()+","+fee.get(0).get("shouxufei").toString()+","+fee.get(0).get("amount").toString();
		
		page.setBean(bean);
		
	}
	
	//1申请中，2已打款，3退回
	private String loadStatusName(String status){
		if("1".equals(status)){
			return "<span style='color:blue;'>申请中</span>";
		}else if("2".equals(status)){
			return "<span style='color:green;'>已打款</span>";
		}else if("3".equals(status)){
			return "<span style='color:red;'>退回</span>";
		}else if("5".equals(status)){
			return "<span style='color:blue;'>打款中</span>";
		}else if("6".equals(status)){
			return "<span style='color:red;'>付款失败</span>";
		}
		return "";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}

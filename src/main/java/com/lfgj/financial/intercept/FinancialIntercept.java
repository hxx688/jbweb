package com.lfgj.financial.intercept;

import java.util.List;
import java.util.Map;

import com.lfgj.member.model.Member;
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


public class FinancialIntercept extends PageIntercept {
	private String userId;
	private String source_type;
	private String isStaticSub;
	private String startDate;
	private String endDate;
	
	public void queryBefore(AopContext ac) {
		String sql = "";
		
		if(!Func.isEmpty(userId)){
			Member member = Blade.create(Member.class).findById(userId);
			if(member.getIs_agent() == 1){ // 查子级
//				sql += " and CASE u.is_agent WHEN 1 THEN find_in_set("+userId+", u.parent_ids) ELSE u.agent_id = "+member.getAgent_id()+" END";
				sql += " and find_in_set("+userId+", u.parent_ids)";
			}else{ // 查本人
				sql += " and f.user_id = "+userId;
			}
		}else{
			ShiroUser shiroUser = ShiroKit.getUser();
			User user = Blade.create(User.class).findById(shiroUser.getId());
			if(LfConstant.agent_role.equals(user.getRoleid())&&Func.isEmpty(userId)){
//				sql += " and u.agent_id = "+user.getId();
				sql += " and FIND_IN_SET("+user.getMember_id()+",u.parent_ids)";
			}
		}
		
		if(!Func.isEmpty(source_type)){
			if("总盈亏".equals(source_type)){
				sql += " and f.source_type in('平仓','消费')";
			}else if("提现".equals(source_type)){
				sql += " and f.source_type in('提现','提现退回')";
				sql += " and f.orderNo not in(select tixianNum from dt_tixianjilu where status = 1)";
			}else{
				sql += " and f.source_type = '"+source_type+"'";
			}
		}
		
		Map<String, Object> params = ac.getParam();
		Object user_id = params.get("user_id_skip");
		if(!Func.isEmpty(user_id)){
			sql += " and (f.user_id = '"+user_id+"' or f.user_name like '%"+user_id+"%')";
		}
		
		Object agent_name = params.get("agent_name_skip");
		if(!Func.isEmpty(agent_name)){
			sql += " and (u.agent_id = '"+agent_name+"' or u.agent_name like '%"+agent_name+"%')";
		}
		
		if(!Func.isEmpty(startDate)){
			sql += " and DATE_FORMAT(f.create_time,'%Y-%m-%d') >= '" + startDate + "'";
		}
		
		if(!Func.isEmpty(endDate)){
			sql += " and DATE_FORMAT(f.create_time,'%Y-%m-%d') <= '" + endDate + "'";
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
			map.put("financial_name", loadStatusName(map.get("financial_type").toString()));
			
			if(user.getIs_agent() == 1){
				map.put("phone", StrKit.hidePhone((String)map.get("phone")));
			}
		}
		
		String sql = "select COALESCE(SUM(f.amount),0) amount from dt_financial f,lf_member u where f.user_id=u.id and u.is_agent=0 and financial_type = 1 and source_type like '%平仓%' ";
		sql += ac.getCondition();
		sql += ac.getSqlEx();	
		List<Map> shouru = Db.selectList(sql,ac.getParam());
		String bean = "";
		bean = shouru.get(0).get("amount").toString();
		
		sql = "select COALESCE(SUM(f.amount),0) amount from dt_financial f,lf_member u where f.user_id=u.id and u.is_agent=0 and financial_type = 2 and source_type like '%消费%' ";
		sql += ac.getCondition();
		sql += ac.getSqlEx();		
		List<Map> zhichu = Db.selectList(sql,ac.getParam());
		bean += ","+zhichu.get(0).get("amount").toString();	
		
		sql = "select COALESCE(SUM(f.amount),0) amount from dt_financial f,lf_member u where f.user_id=u.id and u.is_agent=0 and financial_type = 1 and source_type='充值' ";
		sql += ac.getCondition();
		sql += ac.getSqlEx();
		List<Map> chongzhi = Db.selectList(sql,ac.getParam());
		bean += ","+chongzhi.get(0).get("amount").toString();	
		
		page.setBean(bean);
	}
	

	private String loadStatusName(String status){
		if("1".equals(status)){
			return "<span style='color:blue;'>收入</span>";
		}else{
			return "<span style='color:red;'>支出</span>";
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSource_type() {
		return source_type;
	}

	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}

	public String getIsStaticSub() {
		return isStaticSub;
	}

	public void setIsStaticSub(String isStaticSub) {
		this.isStaticSub = isStaticSub;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

}

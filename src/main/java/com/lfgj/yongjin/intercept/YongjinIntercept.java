package com.lfgj.yongjin.intercept;

import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Db;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.grid.BladePage;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.model.User;

import java.util.List;
import java.util.Map;

public class YongjinIntercept extends PageIntercept
{
  public String agent_id;
  public String member_id;

  public void queryBefore(AopContext ac)
  {
    String condition = "";

    Map params = ac.getParam();
    if (!(Func.isEmpty(this.agent_id))){
    	condition += " and CASE is_agent WHEN 1 THEN parent_id = " + this.member_id + " ELSE agent_id = " + this.agent_id + " END";
    }else {
//      params.put("level", "1");
      condition += " and level = 1";
    }

    Object user_id = params.get("user_id_skip");
    if (!(Func.isEmpty(user_id))) {
      condition = condition += " and (l.id = '" + user_id + "' or l.real_name like '%" + user_id + "%')";
    }
    
    ac.setCondition(condition);
  }
  
  @SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
	  	BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		
		ShiroUser shiroUser = ShiroKit.getUser();
		User user = Blade.create(User.class).findById(shiroUser.getId());
		
		for (Map<String, Object> map : list) {
			if(user.getIs_agent() == 1){
				map.put("mobile", StrKit.hidePhone((String)map.get("mobile")));
			}
		}
	  
//		Map<String, Object> params = ac.getParam();
		
//		// 总充值
//		String sql = Md.getSql("yongjin.rechargeTotal");
//		sql += ac.getSqlEx();
//		sql += ac.getCondition();
//		sql = "select ifnull(sum(total_result),0)amount from ("+sql+")t";
//		List<Map> result = Db.selectList(sql, ac.getParam());
//		String bean = "";
//		bean = result.get(0).get("amount").toString();
//
//		// 总提现
//		sql = Md.getSql("yongjin.withdrawTotal");
//		sql += ac.getSqlEx();
//		sql += ac.getCondition();
//		sql = "select ifnull(sum(total_result),0)amount from ("+sql+")t";
//		result = Db.selectList(sql, ac.getParam());
//		bean += "," + result.get(0).get("amount").toString();
//
//		// 总盈亏
//		sql = Md.getSql("yongjin.profiLossTotal");
//		sql += ac.getSqlEx();
//		sql += ac.getCondition();
//		sql = "select ifnull(sum(total_result),0)amount from ("+sql+")t";
//		result = Db.selectList(sql, ac.getParam());
//		bean += "," + result.get(0).get("amount").toString();
//		
//		// 总手续费
//		sql = Md.getSql("yongjin.sxfTotal");
//		sql += ac.getSqlEx();
//		sql += ac.getCondition();
//		sql = "select ifnull(sum(total_result),0)amount from ("+sql+")t";
//		result = Db.selectList(sql, ac.getParam());
//		bean += "," + result.get(0).get("amount").toString();
//		
//		page.setBean(bean);
	}
}
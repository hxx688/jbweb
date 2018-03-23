package com.lfgj.order.intercept;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.lfgj.order.model.Order;
import com.lfgj.product.util.ProductCacheUtil;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Db;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.grid.BladePage;
import com.rrgy.core.toolbox.kit.BeanKit;
import com.rrgy.core.toolbox.kit.MathKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.model.User;



public class OrderIntercept extends PageIntercept{
	
	public int agentid;
	public int member_id;
	public String status;
	
	public void queryBefore(AopContext ac) {
		String sql = "";
		
		if(agentid>0){
			sql += " and u.agent_id = "+agentid;
		}
		if(member_id>0){
			sql += " and FIND_IN_SET("+member_id+",u.parent_ids)";
		}
		
		Map<String, Object> params = ac.getParam();
		Object person_name = params.get("person_name_skip");
		if(!Func.isEmpty(person_name)){
			sql += " and (t.person_id = '"+person_name+"' or t.person_name like '%"+person_name+"%')";
		}
		Object agent_name = params.get("agent_name_skip");
		if(!Func.isEmpty(agent_name)){
			sql += " and (u.agent_id = '"+agent_name+"' or u.agent_name like '%"+agent_name+"%')";
		}
		if(!Func.isEmpty(status)){
			sql += " and t.status = "+status;
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
			map.put("buy_type＿name", loadTypeName(map.get("buy_type").toString()));
			map.put("status＿name", loadStatusName(map.get("status").toString()));
			map.put("bili", loadBili(map));
			
			if(user.getIs_agent() == 1){
				map.put("mobile", StrKit.hidePhone((String)map.get("mobile")));
			}
		}
		
		String sql = "SELECT COALESCE (SUM(t.earn), 0) amount,COALESCE (SUM(t.fee), 0) fee FROM dt_orders t,lf_member u WHERE t.person_id = u.id AND t.is_agent = 0";
		sql += ac.getCondition();
		sql += ac.getSqlEx();
		
		List<Map> fee = Db.selectList(sql,ac.getParam());
		String bean = fee.get(0).get("amount").toString()+","+fee.get(0).get("fee").toString();
		
		page.setBean(bean);
		
	}
	
	private String loadTypeName(String status){
		if("1".equals(status)){
			return "<span style='color:red' >"+LfConstant.ORDER_BUY_TYPE.get(status)+"</span>";
		}
		return "<span style='color:green' >"+LfConstant.ORDER_BUY_TYPE.get(status)+"</span>";
	}
	private String loadStatusName(String status){
		return LfConstant.ORDER_STATUS_TYPE.get(status);
	}
	
	private String loadBili(Map<String, Object> map){
		Order order = BeanKit.mapToBean(map, Order.class);
		String code = order.getCode();
		JSONObject json = ProductCacheUtil.init(code).get();
		if(order.getStatus()==1){
			if(json==null){
				return "0";
			}
			String newPrice = json.getString("NewPrice");
			map.put("sale_price", newPrice);
			order.setSale_price(newPrice);
		}
		if(json!=null){
			String newPrice = json.getString("NewPrice");
			String LastClose = json.getString("LastClose");
			BigDecimal diffPrice = new BigDecimal(newPrice).subtract(new BigDecimal(LastClose));
			BigDecimal nbili = diffPrice.divide(new BigDecimal(LastClose),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			if(nbili.floatValue()>0){
				map.put("nbili", "<span style='color:red' >"+MathKit.clearZero(nbili)+"%</span>");
			}else{
				map.put("nbili", "<span style='color:green' >"+MathKit.clearZero(nbili)+"%</span>");
			}
		}
		
		if(Func.isEmpty(order.getSale_price())){
			return "0";
		}
		BigDecimal earn = CommKit.getEarn(order);
		if(order.getStatus()==1){
			map.put("earn", earn);
		}
		BigDecimal bili = earn.divide(order.getOrder_money(),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		if(bili.floatValue()>0){
			return "<span style='color:red' >"+MathKit.clearZero(bili)+"%</span>";
		}else{
			return "<span style='color:green' >"+MathKit.clearZero(bili)+"%</span>";
		}
	}
}

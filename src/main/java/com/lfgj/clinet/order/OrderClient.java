package com.lfgj.clinet.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.order.model.Order;
import com.lfgj.order.service.OrderService;
import com.lfgj.order.util.OrderCacheUtil;
import com.lfgj.product.util.ProductCacheUtil;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.JsonKit;

@Service
@Client(name = "lf_order")
public class OrderClient extends RequestMethod{
	@Autowired
	OrderService service;
	
	public ResultVo index() {
		ResultVo rv = new ResultVo();
		String person_id = getParams("id","");
		Paras paras = Paras.create().set("id", person_id).set("status",1);
		List<Order> orders = Md.selectList("order.listSale",paras,Order.class);
		BigDecimal totalEarn = BigDecimal.ZERO;
		for(Order stockorder:orders){
			JSONObject json = ProductCacheUtil.init(stockorder.getCode()).get();
			if(json!=null){
				String pprice = json.getString("NewPrice");
				stockorder.setSale_price(pprice);
				BigDecimal earn = CommKit.getEarn(stockorder);
				stockorder.setEarn(earn);
				totalEarn = totalEarn.add(earn);
			}else{
				stockorder.setSale_price("0");
				stockorder.setEarn(BigDecimal.ZERO);
				totalEarn = BigDecimal.ZERO;
			}
		}

		Map<String,Object> vl = new HashMap<String,Object>();
		vl.put("orders", orders);
		vl.put("totalEarn",totalEarn);
		rv.setReturnCode("0");
		rv.setReturnParams(vl);
		rv.setReturnMsg("");
		return rv;
	}
	
	public ResultVo view(){
		ResultVo rv = new ResultVo();
		String order_id = getParams("order_id","");	
		Order order = service.findById(order_id);
		if(order==null){
			rv.setReturnCode("1");
			rv.setReturnMsg("无效订单");
			return rv;
		}
		
		if(order.getStatus()==1){
			JSONObject obj = ProductCacheUtil.init(order.getCode()).get();
			if(obj!=null){
				String pprice = obj.getString("NewPrice");
				order.setSale_price(pprice);
				BigDecimal earn = CommKit.getEarn(order);
				order.setEarn(earn);
				BigDecimal amount = order.getOrder_money().add(earn);
				order.setOrder_earn(amount);
			}else{
				order.setSale_price("0");
				order.setEarn(BigDecimal.ZERO);
				order.setOrder_earn(BigDecimal.ZERO);
			}
		}else{
			BigDecimal earn = CommKit.getEarn(order);
			order.setEarn(earn);
			BigDecimal amount = order.getOrder_money().add(order.getEarn());
			order.setOrder_earn(amount);
		}
		
		rv.setReturnCode("0");
		rv.setReturnParams(order);
		rv.setReturnMsg("");
		return rv;
	}
	
	public ResultVo list(){
		ResultVo rv = new ResultVo();
		String id = getParams("id","");
		String page = getParams("pageNo","1");
		String rows = getParams("pageSize",  LfConstant.PAGE_SIZE);
		String month = getParams("month", DateKit.getMonth());
		
		Map<String,String> param = new HashMap<String,String>();
		param.put("person_id_equal",id);
		param.put("t.status_equal","2");
		param.put("buy_time",month);
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "order.listUser",para,"","buy_time desc");	
		
		rv.setReturnCode("0");
		rv.setReturnParams(grid);
		return rv;
	}
	
	public ResultVo set(){
		ResultVo rv = new ResultVo();
		String id = getParams("id","");
		String type = getParams("type","");
		String earn = getParams("earn","");
		
		if(Func.isEmpty(earn)){
			rv.setReturnCode("1");
			rv.setReturnMsg("无效参数");
			return rv;
		}
		
		BigDecimal up = new BigDecimal(earn).divide(new BigDecimal(100));
		Order order = Blade.create(Order.class).findById(id);	
		if(order==null){
			rv.setReturnCode("1");
			rv.setReturnMsg("无效订单");
			return rv;
		}
		if(order.getStatus()!=1){
			rv.setReturnCode("1");
			rv.setReturnMsg("无效订单");
			return rv;
		}
		
		JSONObject json = ProductCacheUtil.init(order.getCode()).get();
		if(json==null){
			rv.setReturnCode("1");
			rv.setReturnMsg("当前价格无效");
			return rv;
		}
		String pprice = json.getString("NewPrice");
		order.setSale_price(pprice);
		BigDecimal earnMoney = CommKit.getEarn(order);
		BigDecimal point = earnMoney.divide(order.getOrder_money(),4, BigDecimal.ROUND_HALF_UP).abs();
		
		if(up.floatValue()!=0&&earnMoney.floatValue()<0
				&&up.floatValue()<point.floatValue()
				&&"2".equals(type)){
			rv.setReturnCode("1");
			rv.setReturnMsg("不能低于当前盈亏点");
			return rv;
		}
		
		if(up.floatValue()!=0&&earnMoney.floatValue()>0
				&&up.floatValue()<point.floatValue()
				&&"1".equals(type)){
			rv.setReturnCode("1");
			rv.setReturnMsg("不能低于当前盈亏点");
			return rv;
		}
		
		if("1".equals(type)){			
			order.setUp_earn(up);
		}else{
			order.setLow_earn(up);
		}
		
		boolean rs = Blade.create(Order.class).update(order);
		if(rs){	
			OrderCacheUtil.init().put(order);
			
			rv.setReturnCode("0");
			rv.setReturnParams("");
		}else{
			rv.setReturnCode("2");
			rv.setReturnMsg("设置失败");
		}
		return rv;
	}
}

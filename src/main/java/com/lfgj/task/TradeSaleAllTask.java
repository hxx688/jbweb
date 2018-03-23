package com.lfgj.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.order.model.Order;
import com.lfgj.order.service.OrderService;
import com.lfgj.product.util.ProductCacheUtil;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.MathKit;

/**
 * 
 * @author Administrator
 *
 */
public class TradeSaleAllTask implements Runnable {
	
	@Autowired
	OrderService orderService;

	@Override
	public void run() {
		System.out.println("4:5 点定时平仓");
		List<Order> orders = Md.selectList("order.listAll", Paras.create().set("status","1"),Order.class);
		saleAll(orders);
	}
	
	private void saleAll(List<Order> orders){
		if(orders==null){
			return;
		}
		System.out.println(DateKit.getTime()+":定时平仓 ");
		for(Order order:orders){
			JSONObject obj = ProductCacheUtil.init(order.getCode()).get();
			if(obj==null){
				continue;
			}
			if(order.getStatus()==2){
				continue;
			}
			
			String price = obj.get("NewPrice").toString();	
			price = MathKit.clearZero(price);
			String rs = orderService.updateOrder(order.getId().toString(),price,"定时平仓");
			if(!"0".equals(rs)){
				System.out.println(order.getId()+":平仓失败");
			}
		}
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	
}

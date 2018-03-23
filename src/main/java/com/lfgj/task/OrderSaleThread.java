package com.lfgj.task;

import com.lfgj.order.model.Order;
import com.lfgj.order.service.OrderService;
import com.lfgj.order.util.OrderCacheUtil;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.MathKit;

public class OrderSaleThread implements Runnable{
	
	private OrderService orderService;
	private Order order;
	private String price;
	
	public OrderSaleThread(OrderService orderService,Order order,String price){
		this.orderService = orderService;
		this.order = order;
		this.price = price;
	}

	@Override
	public void run() {
		update();
	}

	private void update(){
		System.out.println(DateKit.getTime()+":定时平仓 ID:"+order.getId());
		price = MathKit.clearZero(price);
		order.setSale_price(price);
		String rs = orderService.updateOrder(order.getId().toString(),price,"定时平仓");
		OrderCacheUtil.init().remove(order.getId());
		if(!"0".equals(rs)){
			System.out.println(order.getId()+":平仓失败");
		}
	}
}

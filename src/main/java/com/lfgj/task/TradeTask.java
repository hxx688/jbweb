package com.lfgj.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.order.model.Order;
import com.lfgj.order.service.OrderService;
import com.lfgj.order.util.OrderCacheUtil;
import com.lfgj.product.util.ProductCacheUtil;
import com.lfgj.util.CommKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.MathKit;

/**
 * 
 * @author Administrator
 *
 */
public class TradeTask implements Runnable {
	
	@Autowired
	OrderService orderService;

	@Override
	public void run() {
		CommKit.display("================TradeTask===============");
		try{
			init();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void init(){
		List<Order> orders = OrderCacheUtil.init().all();
		
		if(orders==null){
			return;
		}
		
		CommKit.display("订单数："+orders.size());
		
		for(Order order:orders){
			boolean isTask = CommKit.isTask(order.getCode());		
			if(!isTask){
				continue;
			}

			JSONObject obj = ProductCacheUtil.init(order.getCode()).get();
			if(obj==null){
				return;
			}
			if(order.getStatus()==2){
				OrderCacheUtil.init().remove(order.getId());
				continue;
			}
			
			String price = obj.get("NewPrice").toString();			
			String upprice = order.getUp_price();	
			String lowprice = order.getLow_price();
			CommKit.display(order.getOrder_num()+"("+order.getBuy_type_name()+")"+" 止损价："+lowprice+"，止盈价："+upprice+",现价："+price);
			if(order.getBuy_type()==1
					&&!"0".equals(upprice)
					&&!Func.isEmpty(upprice)
					&&Float.valueOf(price)>=Float.valueOf(upprice)){	//盈价平仓
				price = MathKit.clearZero(upprice);
				orderService.updateOrder(order.getId().toString(),price,"盈价平仓");
				continue;
			}
			
			if(order.getBuy_type()==1
					&&!Func.isEmpty(lowprice)
					&&Float.valueOf(price)<=Float.valueOf(lowprice)){	//亏价平仓
				price = MathKit.clearZero(lowprice);
				orderService.updateOrder(order.getId().toString(),price,"亏价平仓");	
				continue;
			}
			
			if(order.getBuy_type()==2
					&&!"0".equals(upprice)
					&&!Func.isEmpty(upprice)
					&&Float.valueOf(price)<=Float.valueOf(upprice)){	//盈价平仓
				price = MathKit.clearZero(upprice);
				orderService.updateOrder(order.getId().toString(),price,"盈价平仓");				
				continue;
			}
			
			if(order.getBuy_type()==2
					&&!Func.isEmpty(lowprice)
					&&Float.valueOf(price)>=Float.valueOf(lowprice)){	//亏价平仓
				price = MathKit.clearZero(lowprice);
				orderService.updateOrder(order.getId().toString(),price,"亏价平仓");
				continue;
			}		
		}
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
}

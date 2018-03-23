package com.lfgj.order.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lfgj.order.model.Order;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.MathKit;

public class OrderCacheUtil {
	
	private static String cache_key = LfConstant.ORDER_QUERY;
	private static Map<String, OrderCacheUtil> pool = new ConcurrentHashMap<String, OrderCacheUtil>();
	
	private OrderCacheUtil(String cache){
		cache_key = cache;
	}
	
	public void put(Order order){
		initSalePrice(order);
		Map<Integer,Order> orderMap = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(orderMap==null){
			orderMap = new HashMap<Integer,Order>();
		}
		orderMap.put(order.getId(), order);		
		CacheKit.put(LfConstant.CACHE_NAME,cache_key, orderMap);
	}
	
	public Order get(Integer id){
		Map<Integer,Order> orderMap = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(orderMap==null){return null;}
		return orderMap.get(id);
	}
	
	public void remove(Integer id){
		Map<Integer,Order> orderMap = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(orderMap!=null){
			orderMap.remove(id);
			CacheKit.put(LfConstant.CACHE_NAME,cache_key, orderMap);
		}
	}
	
	public void clear(){
		CacheKit.remove(LfConstant.CACHE_NAME,cache_key);
	}
	
	public List<Order> all(){
		List<Order> orders = new ArrayList<Order>();
		Map<Integer,Order> orderMap = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(orderMap==null){return null;}
		orders.addAll(orderMap.values());
		return orders;
	}

	public static OrderCacheUtil init(String cache){
		OrderCacheUtil ocu = pool.get(cache);
		if(ocu==null){
			ocu = new OrderCacheUtil(cache);
			pool.put(cache, ocu);
		}
		return ocu;
	}
	
	public static OrderCacheUtil init(){
		return init(LfConstant.ORDER_QUERY);
	}
	
	/**
	 * 设置出手价格，默认100%强制平仓
	 * @param order
	 */
	public void initSalePrice(Order order){
		setUp(order);
		setLow(order);
	}
	
	private BigDecimal getPoint(BigDecimal bili,Order order){
		if(order.getYinkui()==null){
			System.out.println(order.getOrder_num()+":盈亏参数无效");
			return BigDecimal.ZERO;
		}
		BigDecimal	earn = bili.multiply(order.getOrder_money()); //损失
		BigDecimal a1 = earn.divide(new BigDecimal(order.getBuy_count()));//每手损失
		BigDecimal a2 = a1.divide(order.getYinkui());//上升点数
		a2 = a2.multiply(order.getMin_point());//实际点数
		CommKit.display(order.getOrder_num()+" 比例："+bili+" 点数："+a2);
		return a2;
	}
	
	private void setUp(Order order){
		BigDecimal up = order.getUp_earn();
		if(up.floatValue()==0){
			order.setUp_price("0");
		}else if(order.getBuy_type()==1){
			BigDecimal a2 = getPoint(up,order);
			String up_price = new BigDecimal(order.getBuy_price()).add(a2).toString();//盈利平仓价(购)
			order.setUp_price(MathKit.clearZero(up_price));
		}else{
			BigDecimal a2 = getPoint(up,order);
			String up_price = new BigDecimal(order.getBuy_price()).subtract(a2).toString();//盈利平仓价(售)
			order.setUp_price(MathKit.clearZero(up_price));
		}
	}
	
	private void setLow(Order order){
		BigDecimal low = order.getLow_earn();
		if(low.floatValue()==0){
			low = BigDecimal.ONE;
		}
		
		if(order.getBuy_type()==1){
			BigDecimal a2 = getPoint(low,order);
			String low_price = new BigDecimal(order.getBuy_price()).subtract(a2).toString();
			order.setLow_price(MathKit.clearZero(low_price));	//损失平仓价
		}else{
			BigDecimal a2 = getPoint(low,order);
			String low_price = new BigDecimal(order.getBuy_price()).add(a2).toString();
			order.setLow_price(MathKit.clearZero(low_price));
		}
	}
}

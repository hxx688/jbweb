package com.rrgy.exchange.service;

import java.util.Map;

import com.rrgy.core.base.service.IService;
import com.rrgy.exchange.model.ExchangeOrder;

/**
 * Generated by Blade.
 * 2017-03-10 14:48:55
 */
public interface ExchangeOrderService extends IService<ExchangeOrder>{
	
	boolean updateStatus(String ids, Object status);
	
	Map<String, Object> findOne(Object id);

	/**
	 * 新增订单
	 * @param eo
	 * @throws Exception
	 */
	public int addOrder(ExchangeOrder eo)throws Exception;

}

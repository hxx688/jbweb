package com.lfgj.product.service;

import java.util.Map;

import com.rrgy.core.base.service.IService;
import com.lfgj.product.model.ProductSale;

/**
 * Generated by Blade.
 * 2017-09-09 11:53:17
 */
public interface ProductSaleService extends IService<ProductSale>{
	
	boolean updateStatus(String ids, Object status);
	
	Map<String, Object> findOne(Object id);

}

package com.rrgy.industry.service;

import java.util.Map;

import com.rrgy.core.base.service.IService;
import com.rrgy.industry.model.Industry;

/**
 * Generated by Blade.
 * 2017-01-04 14:59:34
 */
public interface IndustryService extends IService<Industry>{
	
	boolean updateStatus(String ids, Object status);
	
	Map<String, Object> findOne(Object id);

}

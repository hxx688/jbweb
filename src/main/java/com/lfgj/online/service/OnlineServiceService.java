package com.lfgj.online.service;

import java.util.Map;

import com.rrgy.core.base.service.IService;
import com.lfgj.online.model.OnlineService;

/**
 * Generated by Blade.
 * 2017-09-15 16:10:50
 */
public interface OnlineServiceService extends IService<OnlineService>{
	
	boolean updateStatus(String ids, Object status);
	
	Map<String, Object> findOne(Object id);

}
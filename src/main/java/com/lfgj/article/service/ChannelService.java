package com.lfgj.article.service;

import java.util.Map;

import com.rrgy.core.base.service.IService;
import com.lfgj.article.model.Channel;

/**
 * Generated by Blade.
 * 2017-09-02 18:13:22
 */
public interface ChannelService extends IService<Channel>{
	
	boolean updateStatus(String ids, Object status);
	
	Map<String, Object> findOne(Object id);

}

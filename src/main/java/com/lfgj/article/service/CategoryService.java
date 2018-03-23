package com.lfgj.article.service;

import java.util.Map;

import com.rrgy.core.base.service.IService;
import com.lfgj.article.model.Category;

/**
 * Generated by Blade.
 * 2017-09-02 19:39:13
 */
public interface CategoryService extends IService<Category>{
	
	boolean updateStatus(String ids, Object status);
	
	Map<String, Object> findOne(Object id);

}

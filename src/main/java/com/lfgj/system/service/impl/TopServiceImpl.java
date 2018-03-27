package com.lfgj.system.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.rrgy.core.base.service.BaseService;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.lfgj.system.model.Top;
import com.lfgj.system.service.TopService;

/**
 * Generated by Blade.
 * 2017-09-04 21:47:01
 */
@Service
public class TopServiceImpl extends BaseService<Top> implements TopService{

	@Override
	public boolean updateStatus(String ids, Object status) {
		Paras paras = Paras.create().set("status", status).set("ids", ids.split(","));
		boolean temp = updateBy("status = #{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOne(Object id) {
		return Md.selectUnique("top.findOne", Paras.create().set("id", id), Map.class);
	}

}
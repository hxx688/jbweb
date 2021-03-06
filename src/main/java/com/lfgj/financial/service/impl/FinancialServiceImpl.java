package com.lfgj.financial.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.rrgy.core.base.service.BaseService;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.lfgj.financial.model.Financial;
import com.lfgj.financial.service.FinancialService;

/**
 * Generated by Blade.
 * 2017-09-18 21:29:22
 */
@Service
public class FinancialServiceImpl extends BaseService<Financial> implements FinancialService{

	@Override
	public boolean updateStatus(String ids, Object status) {
		Paras paras = Paras.create().set("status", status).set("ids", ids.split(","));
		boolean temp = updateBy("status = #{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOne(Object id) {
		return Md.selectUnique("financial.findOne", Paras.create().set("id", id), Map.class);
	}

}

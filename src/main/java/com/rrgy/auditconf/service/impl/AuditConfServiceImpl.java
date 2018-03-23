package com.rrgy.auditconf.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.rrgy.core.base.service.BaseService;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.auditconf.model.AuditConf;
import com.rrgy.auditconf.service.AuditConfService;

/**
 * Generated by Blade.
 * 2017-01-05 14:05:29
 */
@Service
public class AuditConfServiceImpl extends BaseService<AuditConf> implements AuditConfService{

	@Override
	public boolean updateStatus(String ids, Object status) {
		Paras paras = Paras.create().set("status", status).set("ids", ids.split(","));
		boolean temp = updateBy("status = #{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOne(Object id) {
		return Md.selectUnique("auditConf.findOne", Paras.create().set("id", id), Map.class);
	}

}

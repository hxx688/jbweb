package com.rrgy.sms.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.rrgy.core.base.service.BaseService;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.sms.model.SmsConfig;
import com.rrgy.sms.service.SmsConfigService;

/**
 * Generated by Blade.
 * 2017-01-04 16:31:00
 */
@Service
public class SmsConfigServiceImpl extends BaseService<SmsConfig> implements SmsConfigService{

	@Override
	public boolean updateStatus(String ids, Object status) {
		Paras paras = Paras.create().set("status", status).set("ids", ids.split(","));
		boolean temp = updateBy("status = #{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOne(Object id) {
		return Md.selectUnique("smsConfig.findOne", Paras.create().set("id", id), Map.class);
	}

}

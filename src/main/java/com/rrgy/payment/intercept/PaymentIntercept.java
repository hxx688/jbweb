package com.rrgy.payment.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.grid.BladePage;
import com.rrgy.system.model.Attach;

public class PaymentIntercept extends PageIntercept {
	
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			Attach attach = Blade.create(Attach.class).findById(map.get("img_url"));
			if(attach!=null){
				map.put("ATTACHURL", ConstConfig.DOMAIN + attach.getUrl());
			}
		}
	}
}

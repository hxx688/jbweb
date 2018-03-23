package com.rrgy.exchange.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.rrgy.core.base.service.BaseService;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.exchange.model.ExchangeGoods;
import com.rrgy.exchange.service.ExchangeGoodsService;

/**
 * Generated by Blade.
 * 2017-03-09 13:54:49
 */
@Service
public class ExchangeGoodsServiceImpl extends BaseService<ExchangeGoods> implements ExchangeGoodsService{

	@Override
	public boolean updateStatus(String ids, Object status) {
		Paras paras = Paras.create().set("status", status).set("ids", ids.split(","));
		boolean temp = updateBy("status = #{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOne(Object id) {
		return Md.selectUnique("exchangeGoods.findOne", Paras.create().set("id", id), Map.class);
	}

}

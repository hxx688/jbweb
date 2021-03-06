package com.lfgj.product.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.rrgy.core.base.service.BaseService;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.lfgj.product.model.ProductSale;
import com.lfgj.product.service.ProductSaleService;

/**
 * Generated by Blade.
 * 2017-09-09 11:53:17
 */
@Service
public class ProductSaleServiceImpl extends BaseService<ProductSale> implements ProductSaleService{

	@Override
	public boolean updateStatus(String ids, Object status) {
		Paras paras = Paras.create().set("status", status).set("ids", ids.split(","));
		boolean temp = updateBy("status = #{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOne(Object id) {
		return Md.selectUnique("productSale.findOne", Paras.create().set("id", id), Map.class);
	}

}

package com.lfgj.plugin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.product.model.DateAdjust;
import com.lfgj.product.model.Product;
import com.lfgj.product.service.ProductService;
import com.lfgj.product.util.AdjustCacheUtil;
import com.lfgj.util.LfConstant;
import com.rrgy.core.interfaces.IPlugin;
import com.rrgy.core.plugins.connection.ConnectionPlugin;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.JsonKit;

public class InitProductPlugin implements IPlugin {
	
	@Autowired
	ProductService productService;
	
	@Override
	public void start() {
		ConnectionPlugin.init().start();
		List<DateAdjust> das = Blade.create(DateAdjust.class).findAll();
		for(DateAdjust da:das){
			AdjustCacheUtil.init(da.getCode()+"_date").putMap(da);
		}
	}

	
	@Override
	public void stop() {
		CacheKit.removeAll(LfConstant.CACHE_NAME);
	}
}

package com.lfgj.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lfgj.order.service.OrderService;
import com.lfgj.product.model.Product;
import com.lfgj.product.service.ProductService;
import com.lfgj.product.util.ProductListCacheUtil;
import com.lfgj.util.CommKit;

/**
 * 
 * @author Administrator
 *
 */
public class KLineTask implements Runnable {
	
	@Autowired
	OrderService orderService;
	@Autowired
	ProductService productService;
	
	@Override
	public void run() {

		System.out.println("重置分时线， 日线，周线图");
		
		Map<String,Product> products = ProductListCacheUtil.init().get();
		if(products==null){
			return;
		}
		List<Product> ps = new ArrayList<Product>();
		ps.addAll(products.values());
		CommKit.initLine(ps);
		
		productService.initDateAdjust();
		
	}
	
	public static void main(String[] args) {
		
	}
	
}

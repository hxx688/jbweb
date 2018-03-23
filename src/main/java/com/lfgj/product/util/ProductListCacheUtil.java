package com.lfgj.product.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lfgj.product.model.Product;
import com.lfgj.util.LfConstant;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.CacheKit;

public class ProductListCacheUtil {
	private String cache_key = LfConstant.PRODUCT_QUERY;
	private static Map<String, ProductListCacheUtil> pool = new ConcurrentHashMap<String, ProductListCacheUtil>();
	
	private ProductListCacheUtil(String cache){
		cache_key = cache;
	}
	
	public void putList(List<Product> products){
		Map<String,Product> productMap = get();
		if(productMap==null){
			productMap = new LinkedHashMap<String,Product>(); 
		}
		
		if(products!=null){
			for(Product p:products){
				if(!Func.isEmpty(p.getCode())){
					productMap.put(p.getCode(), p);
				}				
			}
		}
		CacheKit.put(LfConstant.CACHE_NAME,cache_key, productMap);
	}
	
	public void put(Product product){
		Map<String,Product> productMap = get();
		if(productMap==null){
			productMap = new LinkedHashMap<String,Product>(); 
		}
		if(Func.isEmpty(product.getCode())){
			return;
		}
		productMap.put(product.getCode(), product);
		CacheKit.put(LfConstant.CACHE_NAME,cache_key, productMap);
	}
	
	
	public Map<String,Product> get(){
		Map<String,Product> productMap = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(productMap==null){return null;}
		return productMap;
	}

	public Product get(String code){
		Map<String,Product> productMap = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(productMap==null){return null;}
		return productMap.get(code);
	}
	
	public List<Product> getList(){
		Map<String, Product> priceMap = get();
		if(priceMap==null){return null;}
		List<Product> products = new ArrayList<Product>();
		products.addAll(priceMap.values());
		return products;
	}
	
	public void update(Product product){
		remove(product.getCode());
		put(product);
	}
	
	public void remove(String code){
		Map<String,Product> productMap = get();
		if(productMap!=null){
			productMap.remove(code);
			CacheKit.put(LfConstant.CACHE_NAME,cache_key, productMap);
		}
	}
	
	public void removeList(List<Product> products){
		Map<String,Product> productMap = get();
		if(productMap!=null){
			for(Product product:products){
				productMap.remove(product.getCode());
				CacheKit.put(LfConstant.CACHE_NAME,cache_key, productMap);
			}
		}
	}
	
	public void clear(){
		CacheKit.remove(LfConstant.CACHE_NAME,cache_key);
	}
	
	public static ProductListCacheUtil init(String cache){
		ProductListCacheUtil ocu = pool.get(cache);
		if(ocu==null){
			ocu = new ProductListCacheUtil(cache);
			pool.put(cache, ocu);
		}
		return ocu;
	}

	public static ProductListCacheUtil init(){
		ProductListCacheUtil ocu = pool.get(LfConstant.PRODUCT_QUERY);
		if(ocu==null){
			ocu = new ProductListCacheUtil(LfConstant.PRODUCT_QUERY);
			pool.put(LfConstant.PRODUCT_QUERY, ocu);
		}
		return ocu;
	}
}

package com.lfgj.product.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.util.CommKit;
import com.lfgj.util.FixedSizeLinkedHashMap;
import com.lfgj.util.LfConstant;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.CacheKit;

public class ProductCacheUtil {
	private String cache_key = LfConstant.ORDER_QUERY;
	private static Map<String, ProductCacheUtil> pool = new ConcurrentHashMap<String, ProductCacheUtil>();
	private JSONObject lastValue;
	
	private ProductCacheUtil(String cache){
		cache_key = cache;
	}
	
	public void putString(String time,JSONObject price){
//		System.out.println(time+"="+price.getString("Date")+"="+price.getString("NewPrice"));
		lastValue = price;
		Map<String,JSONObject> orderMap = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(orderMap==null){
			orderMap = new LinkedHashMap<String,JSONObject>();
		}
		orderMap.put(time, price);
		CacheKit.put(LfConstant.CACHE_NAME,cache_key, orderMap);
		
		String nprice = price.getString("NewPrice");
		if(Func.isEmpty(nprice)){
			return;
		}
		FixedSizeLinkedHashMap<String,String> priceMap = CacheKit.get(LfConstant.CACHE_NAME,cache_key+"_line");
		if(priceMap==null){
			priceMap = new FixedSizeLinkedHashMap<String,String>();
			priceMap.setMAX_ENTRIES(3600);
		}
		priceMap.put(time, price.getString("NewPrice"));
		CacheKit.put(LfConstant.CACHE_NAME,cache_key+"_line", priceMap);
	}
	
	public void putLine(JSONObject price){
		CacheKit.put(LfConstant.CACHE_NAME,cache_key+"_line_last", price);
	}

	public JSONObject getLine(){
		return CacheKit.get(LfConstant.CACHE_NAME,cache_key+"_line_last");
	}
	
	public void clear(){
		CommKit.display("　clear：");
		CacheKit.remove(LfConstant.CACHE_NAME,cache_key);
		CacheKit.remove(LfConstant.CACHE_NAME,cache_key+"_line");
	}
	
	public JSONObject get(){
		return lastValue;
	}
	
	public Map<String,JSONObject> all(){
		Map<String,JSONObject> orderMap = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(orderMap==null){return null;}
		return orderMap;
	}

	public Map<String,String> allLine(){
		Map<String,String> priceMap = CacheKit.get(LfConstant.CACHE_NAME,cache_key+"_line");
		if(priceMap==null){return null;}
		return priceMap;
	}
	
	public static ProductCacheUtil init(String cache){
		ProductCacheUtil ocu = pool.get(cache);
		if(ocu==null){
			ocu = new ProductCacheUtil(cache);
			pool.put(cache, ocu);
		}
		return ocu;
	}

}

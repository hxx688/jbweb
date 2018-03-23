package com.lfgj.product.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lfgj.product.model.Adjust;
import com.lfgj.product.model.DateAdjust;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.DateTimeKit;

public class AdjustCacheUtil {
	private String cache_key = LfConstant.ADJUST_QUERY;
	private static Map<String, AdjustCacheUtil> pool = new ConcurrentHashMap<String, AdjustCacheUtil>();
	
	private AdjustCacheUtil(String cache){
		cache_key = cache;
	}
	
	public void put(Adjust adjust){
		List<Adjust> products = get();
		if(products==null){
			products = new ArrayList<Adjust>(); 
		}
		if(Func.isEmpty(adjust.getCode())){
			return;
		}
		products.add(adjust);
		CacheKit.put(LfConstant.CACHE_NAME,cache_key, products);
	}
	
	public void putMap(DateAdjust dateAdjust){
		Map<String,DateAdjust> code_date_map = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(code_date_map==null){
			code_date_map = new HashMap<String,DateAdjust>();
		}
		String date = DateKit.getDay(dateAdjust.getCreate_date());
		code_date_map.put(date, dateAdjust);
		CacheKit.put(LfConstant.CACHE_NAME,cache_key, code_date_map);
	}
	
	public Map<String,DateAdjust> getMap(){
		Map<String,DateAdjust> code_date_map = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		return code_date_map;
	}
	
	public DateAdjust getToday(){
		Map<String,DateAdjust> code_date_map = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(code_date_map!=null){
			String today = DateKit.getDay();
			int h = DateTimeKit.thisHour();
			if(h<LfConstant.hour){
				today = DateKit.getDay(DateTimeKit.yesterday());
			}
			return code_date_map.get(today);
		}
		return null;
	}
	
	public void putList(List<Adjust> adjust){
		CacheKit.put(LfConstant.CACHE_NAME,cache_key, adjust);
	}
	
	public List<Adjust> get(){
		List<Adjust> adjusts = CacheKit.get(LfConstant.CACHE_NAME,cache_key);
		if(adjusts==null){return null;}
		return adjusts;
	}
	
	public void clear(){
		CommKit.display("　clear：");
		CacheKit.remove(LfConstant.CACHE_NAME,cache_key);
	}

	public static AdjustCacheUtil init(String code){
		code = code+"_adjust";
		AdjustCacheUtil ocu = pool.get(code);
		if(ocu==null){
			ocu = new AdjustCacheUtil(code);
			pool.put(code, ocu);
		}
		return ocu;
	}

}

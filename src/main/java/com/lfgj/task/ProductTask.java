package com.lfgj.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lfgj.product.model.DateAdjust;
import com.lfgj.product.model.Product;
import com.lfgj.product.util.AdjustCacheUtil;
import com.lfgj.product.util.ProductCacheUtil;
import com.lfgj.product.util.ProductListCacheUtil;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Administrator
 *
 */
public class ProductTask implements Runnable {

	@Override
	public void run() {
		CommKit.display("================ProductTask===============");
		try{
			init();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void init(){
		Map<String,Product> products = ProductListCacheUtil.init().get();
		if(products==null){		
			Product p = new Product();
			p.setStatus(1);
			List<Product> ps = Blade.create(Product.class).findByTemplate(p);
			if(ps!=null&&ps.size()>0){
				ProductListCacheUtil.init().putList(ps);
				products = ProductListCacheUtil.init().get();
			}else{
				return;
			}
			CommKit.display("查询数据库－商品数："+products.size());
		}
		
		CommKit.display("商品数："+products.size());
		
		if(products.size()==0){
			return;
		}

		String dateTime = DateKit.getTime();
		long difftime = diffTime(dateTime,"5");
		if(difftime>300000){
			System.out.println(DateKit.getTime()+" 刷新５分钟线");
			initTime(products,"3","5");
			CacheKit.put(LfConstant.CACHE_NAME, "5_last_time", dateTime);
		}
		
		long difftime2 = diffTime(dateTime,"30");
		if(difftime2>1800000){
			System.out.println(DateKit.getTime()+" 刷新３０分钟线");
			initTime(products,"3","30");
			CacheKit.put(LfConstant.CACHE_NAME, "30_last_time", dateTime);
		}
		
		long difftime3 = diffTime(dateTime,"60");
		if(difftime3>3600000){
			System.out.println(DateKit.getTime()+" 刷新60分钟线");
			initTime(products,"3","60");
			CacheKit.put(LfConstant.CACHE_NAME, "60_last_time", dateTime);
		}
		
		String url = getUrl(products);
		if(Func.isEmpty(url)){
			return;
		}
		CommKit.display("请求ＵＲＬ："+url);
		String json = HttpKit.post(url);
		CommKit.display("返回："+json);
		processNew(json,products,url);
	}
	
	private void initTime(Map<String,Product> products,String type,String time){
		for(Product p:products.values()){
			boolean isTask = CommKit.isTask(p.getCode());
			if(isTask){
				ProductCacheUtil.init(p.getCode()+"_"+type+"_"+time).clear();
				CommKit.fillList(p.getCode(),type,time,0);
				try {
		            Thread.sleep(LfConstant.time);  
		        } catch (InterruptedException e) {  
		            e.printStackTrace();  
		        }
			}
		}
	}
	
	public void processNew(String json,Map<String,Product> products,String url){
		JSONArray jsarr=null;
		try{
			jsarr=JsonKit.parseArray(json);
		}catch(JSONException ex){
			System.out.println(ex.getLocalizedMessage()+":"+json);
			System.out.println("异常请求："+url);
			JSONObject jo = JsonKit.parse(json);
			String err = jo.getString("errcode");
			String msg = jo.getString("errmsg");
			if("4009".equals(err)){
				String code = msg.split(" ")[0];
				ProductListCacheUtil.init().remove(code);
			}
		}
		if(jsarr==null){
			return;
		}
		CommKit.display("返回商品数："+jsarr.size());
		for(int x =0;x<jsarr.size();x++){			
			JSONObject jsonobj = (JSONObject) jsarr.get(x);
			String date = jsonobj.getString("Date");
            if(null != date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.format(new Date(Long.valueOf(date)*1000L));
                jsonobj.put("Date", date);

            }
			String code = jsonobj.getString("Symbol");
			BigDecimal NewPrice = jsonobj.getBigDecimal("NewPrice");
			if(NewPrice.floatValue()<=0){
				continue;
			}
			
			Product product = products.get(code);
			if(product.getDiancs()==null){
				product.setDiancs(BigDecimal.ZERO);
			}
			
			CommKit.initBei(product.getBei(),jsonobj);
			
			initAdjust(product,jsonobj);
			
			CommKit.initDecmail(product.getDiancs(),jsonobj);
			
			ProductCacheUtil.init(code).putString(date, jsonobj);
		}
	}
	
	private void initAdjust(Product product,JSONObject jsonobj){
		if(product.getAdjust()!=null){
			DateAdjust da = AdjustCacheUtil.init(product.getCode()+"_date").getToday();
			BigDecimal NewPrice = jsonobj.getBigDecimal("NewPrice");
			BigDecimal nNewPrice = NewPrice.add(product.getAdjust());
			String price  = MathKit.clearZero(nNewPrice);
			jsonobj.put("NewPrice",  price);
			
			if(da!=null){
				BigDecimal high = jsonobj.getBigDecimal("High");
				BigDecimal nHigh = high.add(da.getHigh());
				jsonobj.put("High", MathKit.clearZero(nHigh));
				
				BigDecimal low = jsonobj.getBigDecimal("Low");
				if(low.floatValue()==0){
					JSONObject obj = ProductCacheUtil.init(product.getCode() + "_3").getLine();
					low = obj.getBigDecimal("Low");
				}
				
				if(low.floatValue()!=0){
					BigDecimal nLow = low.add(da.getLow());
					jsonobj.put("Low", MathKit.clearZero(nLow));
				}
				
				BigDecimal open = jsonobj.getBigDecimal("Open");
				BigDecimal nOpen = open.add(da.getFirst());
				jsonobj.put("Open", MathKit.clearZero(nOpen));	
				
				BigDecimal lastClose = jsonobj.getBigDecimal("LastClose");
				if(lastClose.floatValue()!=0){
					BigDecimal nLastClose = lastClose.add(da.getFirst());
					jsonobj.put("LastClose", MathKit.clearZero(nLastClose));	
				}
				
			}else{
				if(product.getAdjust().floatValue()>0){
					BigDecimal high = jsonobj.getBigDecimal("High");
					BigDecimal nHigh = high.add(product.getAdjust());
					jsonobj.put("High", MathKit.clearZero(nHigh));
				}
				
				if(product.getAdjust().floatValue()<0){
					BigDecimal low = jsonobj.getBigDecimal("Low");
					if(low.floatValue()==0){
						JSONObject obj = ProductCacheUtil.init(product.getCode() + "_3_5").get();
						low = obj.getBigDecimal("Low");
					}
					
					if(low.floatValue()!=0){
						BigDecimal nLow = low.add(product.getAdjust());
						jsonobj.put("Low", MathKit.clearZero(nLow));
					}
				}
			}
			CommKit.display("调整后:" + product.getCode() + "：" + jsonobj);
		}
	}
	public long diffTime(String dateTime,String fix){
		String lastTime = CacheKit.get(LfConstant.CACHE_NAME, fix+"_last_time");
		if(Func.isEmpty(lastTime)){
			CacheKit.put(LfConstant.CACHE_NAME, fix+"_last_time", dateTime);
			return -1;
		}
		Long stime = DateKit.parseTime(dateTime).getTime();
		Long etime = DateKit.parseTime(lastTime).getTime();
		long v = stime - etime;
		return v;
	}
	
	private String getUrl(Map<String,Product> products){
		String symbol = "";
		for(Product product:products.values()){
			if(Func.isEmpty(product.getCode())){
				continue;
			}		
			boolean isTask = CommKit.isTask(product.getCode());
			if(!isTask){
				continue;
			}
			symbol +="," + product.getCode();
		}
		if(!Func.isEmpty(symbol)){
			symbol = symbol.substring(1);
		}
		
		if(Func.isEmpty(symbol)){
			return null;
		}
		String stock = ConstConfig.pool.get("url");
		String user = ConstConfig.pool.get("user");
		String password = ConstConfig.pool.get("password");
		String query = "Date,Symbol,Name,LastClose,Open,High,Low,NewPrice";
//		String url = stock+"/stock.php?u="+user+"&p="+password+"&r_type=2&symbol="+symbol+"&query="+query;
		String url = stock+ "/stock.php?u=" + user + "&p=" + password + "&market=BS&type=stock&symbol=" + symbol + "&column=Date,Symbol,Name,LastClose,Open,High,Low,NewPrice";
		return url;
	}
	

}

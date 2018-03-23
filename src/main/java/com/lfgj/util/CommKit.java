package com.lfgj.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lfgj.order.model.Order;
import com.lfgj.product.model.Adjust;
import com.lfgj.product.model.Product;
import com.lfgj.product.util.AdjustCacheUtil;
import com.lfgj.product.util.ProductCacheUtil;
import com.lfgj.product.util.ProductListCacheUtil;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.HttpKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.MathKit;
import com.rrgy.core.toolbox.support.DateTime;
import com.rrgy.system.model.Parameter;

public class CommKit {

	public static boolean isTask(String code) {

		Product p = ProductListCacheUtil.init().get(code);
		int week = DateKit.getWeek();
		if(p==null){
			System.out.println(code+":商品不存在");
			return false;
		}
		
		if (!Func.isEmpty(p.getWeek()) && p.getWeek().indexOf("" + week) != -1) {
			return false;
		}

		String sd = DateKit.getTime();
		String d = DateKit.getDay();
		String time = p.getSale_time();
		if (Func.isEmpty(time)) {
			return false;
		}

		String[] times = time.trim().split(",");

		boolean rs = false;
		for (String time1 : times) {
			if (Func.isEmpty(time1)) {
				continue;
			}
			String[] hours = time1.split("-");

			String startH = hours[0];
			String start = d + " " + startH + ":00";

			String endH = hours[1];
			String end = d + " " + endH + ":00";

			if (DateKit.isBetweenDate(start, end, sd, DateKit.DATETIME_FORMATE)) {
				rs = true;
			}
		}
		return rs;
	}

	public static String getStartTime(String time1) {
		if (Func.isEmpty(time1)) {
			return "";
		}
		String d = DateKit.getDay();
		String[] hours = time1.split("-");

		String startH = hours[0];
		String start = d + " " + startH;

		return start;
	}

	public static String getEndTime(String time1) {
		if (Func.isEmpty(time1)) {
			return "";
		}
		String d = DateKit.getDay();
		String[] hours = time1.split("-");

		String startH = hours[0];
		String start = d + " " + startH;

		String endH = hours[1];
		String end = d + " " + endH;

		Date d1 = DateKit.parse(start, DateKit.DATETIME_SHOR_FORMATE);
		Date d2 = DateKit.parse(end, DateKit.DATETIME_SHOR_FORMATE);
		if (d2.before(d1)) {
			d2 = DateTimeKit.offsiteDay(d2, 1);
		}
		end = DateKit.format(d2, DateKit.DATETIME_SHOR_FORMATE);
		return end;
	}

	public static Parameter getParameter(String code) {
		Parameter parameter = CacheKit.get(LfConstant.CACHE_NAME, "parameter_" + code);
		if (parameter == null) {
			parameter = new Parameter();
			parameter.setCode(code);
			parameter = Blade.create(Parameter.class).findTopOne(parameter);
			CacheKit.put(LfConstant.CACHE_NAME, "parameter_" + code, parameter);
		}
		return parameter;
	}

	/**
	 * 返回指定K线类型，0为日线，1为月线，2为周线，3为分钟线，4为季线，5为年线 (默认值0)
	 * 
	 * @param symbol
	 * @param type
	 * @return
	 */
	public static Map<String, JSONObject> fillList(String symbol, String type, String q_type, int ix) {
		String stock = ConstConfig.pool.get("url");
		String user = ConstConfig.pool.get("user");
		String password = ConstConfig.pool.get("password");
		String query = "Date,Symbol,Name,Close,Open,High,Low";
		String url = stock + "/kline.php?u=" + user + "&p=" + password + "&q_type=0&qt_type=" + q_type + "&return_t="
				+ type + "&r_type=2&symbol=" + symbol + "&query=" + query;
		if ("3".equals(type)) {
			String yester = DateTimeKit.yesterday().toString(DateKit.DATE_FORMATE);
			if ("5".equals(q_type)) {
				DateTime time = DateTimeKit.offsiteDate(new Date(), Calendar.HOUR, -6);
				yester = time.toString(DateKit.DATETIME_FORMATE);
				yester = yester.replaceAll(" ", "%20");
				url += "&stime=" + yester;
			} else {
				url += "&stime=" + yester;
			}
		}

		String json = HttpKit.post(url);
		JSONArray jsarr = null;
		try {
			jsarr = JsonKit.parseArray(json);
		} catch (JSONException ex) {
			System.out.println(json + "=" + url);
			JSONObject err = JsonKit.parse(json);
			Object code = err.get("errcode");
			if (ix < 3 && "4003".equals(code.toString())) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ix = ix + 1;
				fillList(symbol, type, q_type, ix);
			}
		}
		if (jsarr == null) {
			return null;
		}
		Map<String, Product> products = ProductListCacheUtil.init().get();
		List<Adjust> adjusts = AdjustCacheUtil.init(symbol).get();

		BigDecimal lastadjust = null;
		for (int x = 0; x < jsarr.size(); x++) {
			JSONObject jsonobj = (JSONObject) jsarr.get(x);
			String date = jsonobj.getString("Date");
			String code = jsonobj.getString("Symbol");
			Product product = products.get(code);
						
			Adjust adjust = find(adjusts, date);
			BigDecimal bei = product.getBei();
			BigDecimal ad = product.getAdjust();
			if (adjust != null) {
				bei = adjust.getBei();
				ad = adjust.getAdjust();
			}
			
			initBei(bei, jsonobj);

			initDecmail(product.getDiancs(), jsonobj);
			
			if(lastadjust==null){
				lastadjust = ad;
			}
			
			if(lastadjust.floatValue()!=ad.floatValue()){
				if(lastadjust.floatValue()>ad.floatValue()){
					initAdjust(lastadjust,ad, jsonobj,"1");
				}
				if(lastadjust.floatValue()<ad.floatValue()){
					initAdjust(lastadjust,ad, jsonobj,"2");
				}
				lastadjust = ad;
			}else{
				initAdjust(ad,ad, jsonobj,"0");
			}
			
			CommKit.display(code + " " + jsonobj.toString());
			if ("3".equals(type)) {
				type = type + "_" + q_type;
				ProductCacheUtil.init(code + "_" + type).putLine(jsonobj);
			}
			
			ProductCacheUtil.init(code + "_" + type).putString(date, jsonobj);
		}
		return ProductCacheUtil.init(symbol + "_" + type).all();
	}

	public static Adjust find(List<Adjust> adjusts, String date) {
		if (adjusts == null) {
			return null;
		}

		if (date.indexOf(":") == -1) {
			date = date + " 23:59:59";
		}

		for (Adjust adjust : adjusts) {
			Date time1 = DateKit.parse(date, DateKit.DATETIME_FORMATE);
			Date time2 = adjust.getEnd_time();
			if (time1.before(time2)) {
				return adjust;
			}
		}
		return null;
	}

	public static void initBei(BigDecimal bei, JSONObject jsonobj) {
		String code = jsonobj.getString("Symbol");

		if (bei != null && bei.floatValue() > 0) {

			BigDecimal LastClose = jsonobj.getBigDecimal("LastClose");
			if (LastClose != null) {
				if(LastClose.floatValue()==0){
					LastClose = jsonobj.getBigDecimal("Open");
				}
				BigDecimal nLastClose = LastClose.multiply(bei);
				jsonobj.put("LastClose", MathKit.clearZero(nLastClose));
			} else {
				BigDecimal Close = jsonobj.getBigDecimal("Close");
				BigDecimal nClose = Close.multiply(bei);
				jsonobj.put("Close", MathKit.clearZero(nClose));
			}

			BigDecimal Open = jsonobj.getBigDecimal("Open");
			BigDecimal nOpen = Open.multiply(bei);
			jsonobj.put("Open", MathKit.clearZero(nOpen));

			BigDecimal High = jsonobj.getBigDecimal("High");
			BigDecimal nHigh = High.multiply(bei);
			jsonobj.put("High", MathKit.clearZero(nHigh));

			BigDecimal Low = jsonobj.getBigDecimal("Low");
			BigDecimal nLow = Low.multiply(bei);
			jsonobj.put("Low", MathKit.clearZero(nLow));

			BigDecimal NewPrice = jsonobj.getBigDecimal("NewPrice");
			if (NewPrice != null) {
				BigDecimal nNewPrice = NewPrice.multiply(bei);
				jsonobj.put("NewPrice", MathKit.clearZero(nNewPrice));
			}
		}
		CommKit.display("放大返回" + code + "：" + jsonobj);
	}

	/**
	 * 1:上调，2：下调
	 * @param adjust
	 * @param jsonobj
	 * @param type
	 */
	public static void initAdjust(BigDecimal adjust,BigDecimal current, JSONObject jsonobj,String type) {
		String code = jsonobj.getString("Symbol");
		if (adjust != null) {

			BigDecimal Close = jsonobj.getBigDecimal("Close");
			BigDecimal nClose = Close.add(adjust);
			jsonobj.put("Close", MathKit.clearZero(nClose));			
			
			BigDecimal Open = jsonobj.getBigDecimal("Open");
			BigDecimal nOpen = Open.add(current);
			jsonobj.put("Open", MathKit.clearZero(nOpen));
			
			if("1".equals(type)){
				BigDecimal high = jsonobj.getBigDecimal("High");
				BigDecimal nHigh = high.add(adjust);
				jsonobj.put("High", MathKit.clearZero(nHigh));

				BigDecimal low = jsonobj.getBigDecimal("Low");
				if(low.floatValue()!=0){
					BigDecimal nLow = low.add(current);
					jsonobj.put("Low", MathKit.clearZero(nLow));	
				}
			}else if("2".equals(type)){
				BigDecimal high = jsonobj.getBigDecimal("High");
				BigDecimal nHigh = high.add(current);
				jsonobj.put("High", MathKit.clearZero(nHigh));

				BigDecimal low = jsonobj.getBigDecimal("Low");
				if(low.floatValue()!=0){
					BigDecimal nLow = low.add(adjust);
					jsonobj.put("Low", MathKit.clearZero(nLow));
				}
			}else{
				
				BigDecimal high = jsonobj.getBigDecimal("High");
				BigDecimal nHigh = high.add(adjust);
				jsonobj.put("High", MathKit.clearZero(nHigh));

				BigDecimal low = jsonobj.getBigDecimal("Low");
				if(low.floatValue()!=0){
					BigDecimal nLow = low.add(adjust);
					jsonobj.put("Low", MathKit.clearZero(nLow));	
				}
			}	
		}
		
		CommKit.display("调整返回" + code + "：" + jsonobj);
	}

	public static void initTop(BigDecimal adjust, JSONObject jsonobj){
		String code = jsonobj.getString("Symbol");
		if(adjust.floatValue()==0){	
			JSONObject lastValue = ProductCacheUtil.init(code).get();
			if(lastValue!=null){
				BigDecimal ohigh = lastValue.getBigDecimal("High");
				BigDecimal oLow = lastValue.getBigDecimal("Low");
				BigDecimal high = jsonobj.getBigDecimal("High");
				BigDecimal low = jsonobj.getBigDecimal("Low");
				if(ohigh.floatValue()>high.floatValue()){
					jsonobj.put("High", MathKit.clearZero(ohigh));
				}else{
					jsonobj.put("High", MathKit.clearZero(high));
				}
				if(oLow.floatValue()<low.floatValue()){
					jsonobj.put("Low", MathKit.clearZero(oLow));	
				}else{
					jsonobj.put("Low", MathKit.clearZero(low));	
				}
			}
		}
	}
	
	public static void initDecmail(BigDecimal diancs, JSONObject jsonobj) {
		String code = jsonobj.getString("Symbol");
		if (diancs.floatValue() != 0) {
			int point = MathKit.point(MathKit.clearZero(diancs.toString()));
			BigDecimal LastClose = jsonobj.getBigDecimal("LastClose");
			if (LastClose != null) {
				BigDecimal nLastClose = LastClose.setScale(point, BigDecimal.ROUND_HALF_UP);
				jsonobj.put("LastClose", MathKit.clearZero(nLastClose));
			} else {
				BigDecimal Close = jsonobj.getBigDecimal("Close");
				BigDecimal nClose = Close.setScale(point, BigDecimal.ROUND_HALF_UP);
				jsonobj.put("Close", MathKit.clearZero(nClose));
			}

			BigDecimal Open = jsonobj.getBigDecimal("Open");
			BigDecimal nOpen = Open.setScale(point, BigDecimal.ROUND_HALF_UP);
			jsonobj.put("Open", MathKit.clearZero(nOpen));

			BigDecimal High = jsonobj.getBigDecimal("High");
			BigDecimal nHigh = High.setScale(point, BigDecimal.ROUND_HALF_UP);
			jsonobj.put("High", MathKit.clearZero(nHigh));

			BigDecimal Low = jsonobj.getBigDecimal("Low");
			BigDecimal nLow = Low.setScale(point, BigDecimal.ROUND_HALF_UP);
			jsonobj.put("Low", MathKit.clearZero(nLow));

			BigDecimal NewPrice = jsonobj.getBigDecimal("NewPrice");
			if (NewPrice != null) {
				BigDecimal nNewPrice = NewPrice.setScale(point, BigDecimal.ROUND_HALF_UP);
				jsonobj.put("NewPrice", MathKit.clearZero(nNewPrice));
			}
		}

		CommKit.display("清小数返回" + code + "：" + jsonobj);
	}

	public static BigDecimal getEarn(Order order) {
		if (order.getMin_point().floatValue() == 0) {
			return BigDecimal.ZERO;
		}
		if (order.getYinkui() == null) {
			order.setYinkui(BigDecimal.ZERO);
		}
		int scale = MathKit.point(MathKit.clearZero(order.getMin_point()));
		BigDecimal diffPrice = new BigDecimal(order.getSale_price()).subtract(new BigDecimal(order.getBuy_price()));
		diffPrice = diffPrice.setScale(scale, BigDecimal.ROUND_HALF_UP);
		BigDecimal point = diffPrice.divide(order.getMin_point()).setScale(0, BigDecimal.ROUND_DOWN);
		BigDecimal earn = point.multiply(order.getYinkui());
		earn = earn.multiply(new BigDecimal(order.getBuy_count()));

		if (order.getBuy_type() == 1 && diffPrice.floatValue() > 0) { // 正
			order.setEarn(earn);
		} else if (order.getBuy_type() == 1 && diffPrice.floatValue() < 0) {// 负
			if (earn.abs().floatValue() > order.getOrder_money().floatValue()) {
				earn = order.getOrder_money().multiply(new BigDecimal(-1));
			}
			order.setEarn(earn);
		} else if (order.getBuy_type() == 2 && diffPrice.floatValue() < 0) {// 正
			earn = earn.multiply(new BigDecimal(-1));
			order.setEarn(earn);
		} else if (order.getBuy_type() == 2 && diffPrice.floatValue() > 0) {// 负
			if (earn.floatValue() > order.getOrder_money().floatValue()) {
				earn = order.getOrder_money();
			}
			earn = earn.multiply(new BigDecimal(-1));
			order.setEarn(earn);
		} else {
			order.setEarn(BigDecimal.ZERO);
		}
		return earn;
	}

	public static boolean isMoney() {

		int week = DateKit.getWeek();
		if (week == 6 || week == 7) {
			return false;
		}

		Parameter parameter = CommKit.getParameter("104");
		if (parameter == null) {
			return false;
		}

		String sd = DateKit.getMinute();
		String d = DateKit.getDay();
		String time = parameter.getPara();
		String[] times = time.split(",");

		boolean rs = false;
		for (String time1 : times) {
			String[] hours = time1.split("-");

			String startH = hours[0];
			String start = d + " " + startH;

			String endH = hours[1];
			String end = d + " " + endH;

			if (DateKit.isBetweenDate(start, end, sd, DateKit.DATETIME_SHOR_FORMATE)) {
				rs = true;
			}
		}
		return rs;
	}

	public static void display(String msg) {
		Parameter params = getParameter("205");
		if ("1".equals(params.getPara())) {
			String date = DateKit.getTime();
			System.out.println(date + " " + msg);
		}
	}

	public static void display2(String msg) {
		Parameter params = getParameter("205");
		if ("3".equals(params.getPara())) {
			String date = DateKit.getTime();
			System.out.println(date + " " + msg);
		}
	}
	
	public static void initLine() {
		Product p = new Product();
		p.setStatus(1);
		List<Product> products = Blade.create(Product.class).findByTemplate(p);
		ProductListCacheUtil.init().putList(products);

		for (Product pt : products) {
			Paras para = Paras.create().set("code", pt.getCode());
			List<Adjust> adjusts = Md.selectList("adjust.list", para, Adjust.class);
			if (adjusts != null && adjusts.size() > 0) {
				AdjustCacheUtil.init(pt.getCode()).putList(adjusts);
			}
		}

		initLine(products);
	}

	public static void initLine(List<Product> products) {
		for (final Product ps : products) {
			ProductCacheUtil.init(ps.getCode()).clear();
			ProductCacheUtil.init(ps.getCode()+"_0").clear();
			ProductCacheUtil.init(ps.getCode()+"_2").clear();

			System.out.println(DateKit.getTime() + ":" + ps.getCode() + "日线图");
			CommKit.fillList(ps.getCode(), "0", "", 0);
			try {
				Thread.sleep(LfConstant.time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println(DateKit.getTime() + ":" + ps.getCode() + "5分钟线图");
			CommKit.fillList(ps.getCode(), "3", "5", 0);
			try {
				Thread.sleep(LfConstant.time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println(DateKit.getTime() + ":" + ps.getCode() + "30分钟线图");
			CommKit.fillList(ps.getCode(), "3", "30", 0);
			try {
				Thread.sleep(LfConstant.time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println(DateKit.getTime() + ":" + ps.getCode() + "60分钟线图");
			CommKit.fillList(ps.getCode(), "3", "60", 0);
			try {
				Thread.sleep(LfConstant.time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println(DateKit.getTime() + ":" + ps.getCode() + "周线图");
			CommKit.fillList(ps.getCode(), "2", "", 0);
			try {
				Thread.sleep(LfConstant.time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static Collection<JSONObject> getJson(String code, String type, String q_type) {
		String key_type = type;
		if (!Func.isEmpty(q_type)) {
			key_type = type + "_" + q_type;
		}
		Map<String, JSONObject> minutes_json = ProductCacheUtil.init(code + "_" + key_type).all();
		if (minutes_json == null) {
			minutes_json = CommKit.fillList(code, type, q_type, 0);
		}

		Collection<JSONObject> minutes_data = null;
		if (minutes_json != null) {
			minutes_data = minutes_json.values();
		}
		return minutes_data;
	}

	public static void main(String[] args) throws Exception {
		// Order order = new Order();
		// order.setMin_point(new BigDecimal(1));
		// order.setYinkui(new BigDecimal(10));
		// order.setSale_price("34216.551");
		// order.setBuy_price("34217.55");
		// order.setBuy_count(1);
		// order.setBuy_type(2);
		// order.setOrder_money(new BigDecimal(1000));
		// System.out.println(CommKit.getEarn(order));
		// System.out.println(DateKit.getWeek());
		boolean rs = DateKit.isBetweenDate("2017-11-06 10:11:00", "2017-11-06 13:11:00", "2017-11-06 13:11:00",
				DateKit.DATETIME_FORMATE);
		System.out.println(rs);
	}
}

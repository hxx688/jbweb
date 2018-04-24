package com.lfgj.util;

import java.util.HashMap;
import java.util.Map;

public class LfConstant {
	public enum Source {
		A("充值"),B("平仓"),C("消费"),D("消费手续费"),E("提现"),F("提现退回"),G("提现手续费");
		public String value;
		Source(String value){this.value = value;}
	};
	
	// 通道
	public final static Map<String,String> PAY_TYPE = new HashMap<String,String>(){{
        put("0", "银联快捷支付");
        put("1", "银联在线支付");
        put("5", "银联网关支付");
        put("6", "环球付");
        put("7", "环球付网关支付");
        put("8", "立达支付");
        put("9", "易达支付");
        put("10", "首捷支付");
    }};
    
    // 允许刷新的通道ID
    public final static String REFRESH_PAY_TYPE = "0,5"; 
    
    // 交易状态:1-待支付 2-支付完成 3-已关闭 4-交易撤销
    public final static Map<String,String> PAY_RESPCODE = new HashMap<String,String>(){{
    	put("0", "未提交");
    	put("1", "待支付");
        put("2", "支付完成");
        put("3", "已关闭");
        put("4", "交易撤销");
        put("5", "无效");
        put("6", "刷新成功");

    }};
	
	public final static Map<String,String> ORDER_BUY_TYPE = new HashMap<String,String>(){{
        put("1", "买涨");
        put("2", "买跌");
    }};
    
    public final static Map<String,String> ORDER_STATUS_TYPE = new HashMap<String,String>(){{
        put("1", "买入");
        put("2", "平仓");
    }};
    
    public final static int FINANCIAL_TYPE_SR = 1;
    public final static int FINANCIAL_TYPE_ZC = 2;
    public final static Map<Integer,String> FINANCIAL_TYPE = new HashMap<Integer,String>(){{
        put(FINANCIAL_TYPE_SR, "收入");
        put(FINANCIAL_TYPE_ZC, "支出");
    }};
    
	public static final String agent_role = "-1";
	public static final int agent_dept = 0;
	
	public static String CACHE_NAME = "sysCache";
	public static String PRODUCT_QUERY = "products";	//产品行情级存
	public static String ORDER_QUERY = "orders";		//订单止损止盈缓存
	public static String ORDER_SALE = "saleOrders";		//可出售订单
	public static String ADJUST_QUERY = "adjust";		//
	
	public static final String PARAM_107 = "107";
	
	public static final String PAGE_SIZE = "20";
	
	public static final String MANAGER = "manager";
	
	public final static int time = 300;
	
	public final static int hour = 5;
}

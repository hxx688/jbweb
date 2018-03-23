package com.rrgy.common.iface;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.rrgy.core.toolbox.kit.JsonKit;

public abstract class RequestAbs implements RequestIntf{
	protected String parameters;
	protected int curPage = 1;
	protected int pageSize = 10;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public void run() {
		getParameters();		
		
	}

	protected String getParams(String key,String defaultValue){
		String param = getParameters(); 
		JSONObject json = JsonKit.parse(param);
		Object value = json.get(key);
		if(value!=null){
			defaultValue = value.toString();
		}
		try {
			defaultValue = URLDecoder.decode(defaultValue,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	public void setParameters(String parameters){
		this.parameters = parameters;
	}

	public String getParameters() {
		return parameters;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public void setRequest(HttpServletRequest request){
		this.request = request;
	}
	
	public boolean validateStaff(){
		return true;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}

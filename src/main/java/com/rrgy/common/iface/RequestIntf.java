package com.rrgy.common.iface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestIntf {
	public void run();
	public void setParameters(String parameters);
	public Object getResult();
	public void setCurPage(int curPage);
	public void setPageSize(int pageSize);
	public boolean validateStaff();
	public void setRequest(HttpServletRequest request);
	public void setResponse(HttpServletResponse response);
}

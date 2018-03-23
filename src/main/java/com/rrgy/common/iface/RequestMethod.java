package com.rrgy.common.iface;

import java.lang.reflect.Method;

public abstract class RequestMethod extends RequestAbs{
	
	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		String serviceId = getParams("serviceId","");
		String methodName = "";
		if(serviceId.indexOf("/")!=-1){
			methodName = serviceId.split("/")[1];
		}else{
			rv.setReturnCode("8");
			rv.setReturnMsg("找不到接口:"+serviceId);
			return rv;
		}
		try {
			Method method = this.getClass().getDeclaredMethod(methodName);
			return (ResultVo) method.invoke(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

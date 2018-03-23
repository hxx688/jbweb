package com.rrgy.common.iface;

public class ResultVo {
	private String returnCode;  //错误码
	private String returnMsg;   //错误码信息提示
	private Object returnParams; //返回参数
	
	private String response; // 支付回调应答码
	private String message; // 支付回调应答消息
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public Object getReturnParams() {
		return returnParams;
	}
	public void setReturnParams(Object returnParams) {
		this.returnParams = returnParams;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

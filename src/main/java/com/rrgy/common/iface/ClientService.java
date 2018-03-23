package com.rrgy.common.iface;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.rrgy.common.base.BaseController;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.support.WafRequestWrapper;

@Controller
@Scope("prototype")
@RequestMapping("/")
public class ClientService extends BaseController implements ApplicationContextAware{
	String message = "";
	String parameters = "";
	String ip = "";
	String actionId = "";
	String staffId = "";
	static int i = 0;
	private ApplicationContext applicationContext;
	
	@ResponseBody
	@RequestMapping("clientService")
	public Object clientService() {
		HttpServletRequest req = this.getRequest();
		ip = req.getRemoteAddr();
		ResultVo rv = new ResultVo();
		String type = "";
		try {
			message = "";
			Class clz = null;
			RequestIntf ri = null;
			type = req.getParameter("iscrypt");
			if("2".equals(type)){
				WafRequestWrapper params = new WafRequestWrapper(getRequest());
				parameters = JsonKit.toJson(params.getParameter());
			}
			else if (!"3".equals(type)) {
				parameters = ClientUtils.streamToString(req.getInputStream());
				if (ClientUtils.isEmptyStr(parameters)) {	
					rv.setReturnCode("1");
					rv.setReturnMsg("参数无效");
					if (ClientUtils.isEmptyStr(type)) {
						return Des3.encode(JsonKit.toJson(rv));
					}else{
						return JsonKit.toJson(rv);
					}
				}
			}
			try {
				if (ClientUtils.isEmptyStr(type)) {
					parameters = Des3.decode(parameters);
					if("error".equals(parameters)){
						rv.setReturnCode("2");
						rv.setReturnMsg("参数异常");
						return Des3.encode(JsonKit.toJson(rv));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				message = "参数解密失败！";
			}
			
			clz = findServiceId();
			if (clz != null)
				ri = (RequestIntf) applicationContext.getBean(clz);
			if (!ClientUtils.isEmptyStr(message)) {
				rv.setReturnCode("2");
				rv.setReturnMsg(message);
				if (ClientUtils.isEmptyStr(type)) {
					return Des3.encode(JsonKit.toJson(rv));
				}else if("2".equals(type)){
					return rv;
				}else{
					return JsonKit.toJson(rv);
				}
			}
			i++;
			if (ri != null) {
				ri.setParameters(parameters);
				ri.setRequest(req);
				ri.run();
				if (!"4".equals(type)) {
					Object rs = ri.getResult();
					if (ClientUtils.isEmptyStr(type)&&rs!=null) {
						rs = Des3.encode(rs.toString());
					}
					if(rs!=null){
						return rs;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		rv.setReturnCode("3");
		rv.setReturnMsg("请求失败");
		if (ClientUtils.isEmptyStr(type)) {
			try {
				return Des3.encode(JsonKit.toJson(rv));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("2".equals(type)){
			return rv;
		}else{
			return JsonKit.toJson(rv);
		}
		return "";
	}
	
	public Class findServiceId() {
		Class clz = null;
		try {
			JSONObject jo = JsonKit.parse(parameters);
			actionId = (String) jo.get("serviceId");
			String param = jo.getString("params");
			if(!Func.isEmpty(param)){
				parameters = param;
			}
			if (ClientUtils.isEmptyStr(actionId)) {
				message = "服务ID无效!";
				return null;
			}
			if(actionId.indexOf("/")!=-1){
				actionId = actionId.split("/")[0];
			}
			clz = DataCore.findMdoules(actionId);
			if (clz == null) {
				message = "未找到处理事件!";
			}
		} catch (Exception ex) {
			message = ex.getMessage();
		}
		return clz;
	}
	
	 public static void main(String[] args) throws Exception {
			String url = "http://localhost:8281/blade/clientService";
			String param = "{\"serviceId\":\"zwm_2016_1.0_personInfo\","
					+ "\"params\":{\"person_id\":\"7\"}}";
			param = Des3.encode(param);
			String rs = RegisterCodeImpl.requestPage(url,param);
			rs = Des3.decode(rs);
			System.out.println(rs);
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		applicationContext = arg0;
	}
}

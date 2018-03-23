package com.rrgy.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rrgy.common.base.BaseController;
import com.rrgy.core.aop.AopContext;
import com.rrgy.core.constant.Cst;
import com.rrgy.core.interfaces.ILoader;
import com.rrgy.core.interfaces.IQuery;
import com.rrgy.core.plugins.dao.Db;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.ClassKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.StrKit;

@Controller
@RequestMapping("/ztree")
public class ZTreeController extends BaseController {
	
	@RequestMapping("/open")
	public ModelAndView open(){
		ModelAndView view = new ModelAndView("/common/_function/_ztree.html");	
		view.addObject("type", getTypeName(getParameter("type"), getParameter("source")));
		view.addObject("index", getParameter("index"));
		view.addObject("name", getParameterToDecode("name"));
		view.addObject("source", getParameter("source"));
		view.addObject("check", getParameter("check"));
		view.addObject("where", getParameter("where"));
		view.addObject("intercept", getParameter("intercept"));
		view.addObject("ext", getParameter("ext"));
		view.addObject("treeId", getParameter("treeId"));
		view.addObject("val", getParameter("val"));
		return view;
	}
	
	@ResponseBody
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/getTreeList")
	public AjaxResult getTreeList(@RequestParam String type, @RequestParam String source, @RequestParam String where, @RequestParam String intercept, @RequestParam String ext, @RequestParam String val, @RequestParam String treeId) {	
		final String sqlSource = getSql(type, source);
		
		Map<String, Object> params = Paras.createHashMap();
		if(!where.equals("0") && StrKit.notBlank(where)){
			params = JsonKit.parse(where, Map.class);
		}
		
		final Map<String, Object> modelOrMap = params;
		
		IQuery _intercept = getIntercept(type);
		if(StrKit.notBlank(intercept) && !Func.equals(intercept, "0")){
			_intercept = ClassKit.newInstance(intercept);
		}
		
		AopContext ac = new AopContext();
		ac.setObject(ext);
		ac.setTips("ztree");
		
		List<Map> list = Db.selectList(sqlSource, modelOrMap, ac, _intercept);

		String key = (StrKit.notBlank(treeId) && !Func.equals(treeId, "0")) ? treeId : ((type.indexOf("dict") >= 0) ? "num" : "id");

		String [] arr = val.split(",");
		for(Map<String, Object> map : list){
			for(String v : arr){
				//if(Func.toStr(map.get(key)).equals(v) && !v.equals("0")){
				if(Func.toStr(map.get(key)).equals(v)){
					map.put("checked", "true");
				}
			}
		}
		
		return json(list);
	}
	
	@ResponseBody
	@RequestMapping("/getTreeListName")
	@SuppressWarnings("unchecked")
	public AjaxResult getTreeListName(@RequestParam String type, @RequestParam String source, @RequestParam String where, @RequestParam String val, @RequestParam String treeId){
		type = getTypeName(type, source);
		
		final String sqlSource = getSql(type, source);
		
		Map<String, Object> params = Paras.createHashMap();
		if(StrKit.notBlank(where)){
			params = JsonKit.parse(where, Map.class);
		}
		
		final Map<String, Object> modelOrMap = params;
		
		List<Map<String, Object>> list = CacheKit.get(getCacheName(type), DICT_ZTREE_LIST + type,
				new ILoader() {
					public Object load() {
						return Db.selectList(sqlSource, modelOrMap);
					}
				});
		
		String name = "";
		
		String key = (StrKit.notBlank(treeId) && !Func.equals(treeId, "0")) ? treeId : ((type.indexOf("dict") >= 0) ? "num" : "id");

		String [] arr = val.split(",");
		for(Map<String, Object> map : list){
			for(String v : arr){
				if(Func.toStr(map.get(key)).equals(v)){
					name += Func.toStr(map.get("name")) + ",";
				}
			}
		}

		name = StrKit.removeSuffix(name, ",");
		
		return json(name);
	}
	
	private String getTypeName(String type, String source){
		if(type.indexOf("opentreeUser") >= 0){
			type = "user";
		} else if(type.indexOf("opentreeDept") >= 0){
			type = "dept";
		} else if(type.indexOf("opentreeRole") >= 0){
			type = "role";
		} else if(type.indexOf("opentree_") >= 0 || type.indexOf("opentreeDict") >= 0){
			type = "dict_" + type.replace("opentree_", "").replace("opentreeDict", "");
		} else {
			type = "diy_" + source;
		}
		return type;
	}
	
	private String getSql(String type, String source){
		String sql = "";
		if (type.indexOf("dict_") >= 0) {
			String code = type.replace("dict_", "");
			sql = "select NUM as \"num\",ID as \"id\",PID as \"pId\",NAME as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_DICT where code=" + code;
		} else if (type.equals("user")) {
			sql = "select ID as \"id\",0 as \"pId\",NAME as \"name\",'true' as \"open\" from  TFW_USER where status=1";
		} else if (type.equals("dept")) {
			sql = "select ID as \"id\",PID as \"pId\",SIMPLENAME as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_DEPT";
		} else if (type.equals("role")) {
			sql = "select ID as \"id\",PID as \"pId\",NAME as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_ROLE";
		} else {
			sql = Md.getSql(source);
		}
		return sql;
	}
	
	private String getCacheName(String type){
		String cacheName = DICT_CACHE;
		if (type.equals("user")) {
			cacheName = USER_CACHE;
		} else if (type.equals("dept")) {
			cacheName = DEPT_CACHE;
		} else if (type.equals("role")) {
			cacheName = ROLE_CACHE;
		} else {
			cacheName = DIY_CACHE;
		}
		return cacheName;
	}
	
	private IQuery getIntercept(String type) {
		IQuery intercept = Cst.me().getDefaultQueryFactory();
		if (type.indexOf("dict") >= 0) {
			intercept = Cst.me().getDefaultSelectFactory().dictIntercept();
		} else if (type.equals("user")) {
			intercept = Cst.me().getDefaultSelectFactory().userIntercept();
		} else if (type.equals("dept")) {
			intercept = Cst.me().getDefaultSelectFactory().deptIntercept();
		} else if (type.equals("role")) {
			intercept = Cst.me().getDefaultSelectFactory().roleIntercept();
		}  
		return intercept;
	}
}

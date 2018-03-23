package com.rrgy.system.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.annotatoin.Table;
import org.beetl.sql.core.db.ColDesc;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.core.JavaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rrgy.core.base.controller.CurdController;
import com.rrgy.core.beetl.BeetlMaker;
import com.rrgy.core.constant.Cst;
import com.rrgy.core.interfaces.IMeta;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.ClassKit;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.LogKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.meta.factory.GenerateFactory;
import com.rrgy.system.model.Generate;

@Controller
@RequestMapping("/generate")
public class GenerateController extends CurdController<Generate> {

	@Override
	protected Class<? extends IMeta> metaFactoryClass() {
		return GenerateFactory.class;
	}
	
	@ResponseBody
	@RequestMapping("/pojo/{table}")
	public String createPojo(@PathVariable String table) {
		try {
			Blade.dao().genPojoCodeToConsole(table);
			return "[ " + table + " ] pojo生成成功,请查看控制台";
		} catch (Exception e) {
			return "[ " + table + " ] pojo生成失败:" + e.getMessage();
		}
	}
	
	@ResponseBody
	@RequestMapping("/pojo/{slave}/{table}")
	public String createPojoSlave(@PathVariable String slave, @PathVariable String table) {
		try {
			Blade.dao(slave).genPojoCodeToConsole(table);
			return "[ " + table + " ] pojo生成成功,请查看控制台";
		} catch (Exception e) {
			return "[ " + table + " ] pojo生成失败:" + e.getMessage();
		}
	}
	
	@ResponseBody
	@RequestMapping("/sql/{table:.+}")
	public String createBuiltInSql(@PathVariable String table) {
		try {
			LogKit.println("\n\n-------------------------------- gen by beetlsql {} --------------------------------\n", DateKit.getTime());
			LogKit.println("-----↓------- curd -------↓-----\n");
			Blade.create(ClassKit.newInstance(table).getClass()).genBuiltInSqlToConsole();
			LogKit.println("\n-----↓-- updateNotNull --↓-----\n");
			LogKit.println(Blade.dao().getDbStyle().genUpdateTemplate(ClassKit.newInstance(table).getClass()).getTemplate());
			LogKit.println("\n-----↓------- field -------↓-----\n");
			Blade.dao().genSQLTemplateToConsole(ClassKit.newInstance(table).getClass().getAnnotation(Table.class).name());
			return "[ " + table + " ] sql生成成功,请查看控制台";
		} catch (Exception e) {
			return "[ " + table + " ] sql生成失败:" + e.getMessage();
		}
	}
	
	@ResponseBody
	@RequestMapping("/sql/{slave}/{table:.+}")
	public String createBuiltInSqlSlave(@PathVariable String slave, @PathVariable String table) {
		try {
			LogKit.println("\n\n-------------------------------- gen by beetlsql {} --------------------------------\n", DateKit.getTime());
			LogKit.println("-----↓------- curd --------↓-----\n");
			Blade.create(ClassKit.newInstance(table).getClass()).genBuiltInSqlToConsole();
			LogKit.println("\n-----↓-- updateNotNull --↓-----\n");
			LogKit.println(Blade.dao(slave).getDbStyle().genUpdateTemplate(ClassKit.newInstance(table).getClass()).getTemplate());
			LogKit.println("\n-----↓------ field -------↓-----\n");
			Blade.dao(slave).genSQLTemplateToConsole(ClassKit.newInstance(table).getClass().getAnnotation(Table.class).name());
			return "[ " + table + " ] sql生成成功,请查看控制台";
		} catch (Exception e) {
			return "[ " + table + " ] sql生成失败:" + e.getMessage();
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/code")
	public AjaxResult gencode(){
		String ids = getParameter("ids");
		List<Generate> list = Blade.create(Generate.class).findBy("id in (#{join(ids)})", Paras.create().set("ids", ids.split(",")));

		for (Generate gen : list) {
			
			String realPath = gen.getRealpath() + File.separator + "src" + File.separator + "main";
			String packageName = gen.getPackagename();
			String modelName = gen.getModelname();
			String upperModelName = StrKit.upperFirst(modelName);
			String lowerModelName = StrKit.lowerFirst(modelName);
			
			String tableName = gen.getTablename();
			String pkName = gen.getPkname();
			String path = File.separator + realPath + File.separator + "java" + File.separator + packageName.replace(StrKit.DOT, File.separator);
			String resourcesPath = File.separator + realPath + File.separator + "resources";
			String webappPath = File.separator + realPath + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "view";
			
			//java
			String controllerPath = path + File.separator + "controller" + File.separator + upperModelName + "Controller.java";
			String modelPath = path + File.separator + "model" + File.separator + upperModelName + ".java";
			String servicePath = path + File.separator + "service" + File.separator + upperModelName + "Service.java";
			String serviceimplPath = path + File.separator + "service" + File.separator + "impl" + File.separator + upperModelName + "ServiceImpl.java";
			
			//resources
			String sqlPath = resourcesPath + File.separator + "beetlsql" + File.separator + "gen" + File.separator + lowerModelName + ".md";
			
			//webapp
			String indexPath = webappPath + File.separator + "gen" + File.separator + lowerModelName + File.separator + lowerModelName + ".html";
			String addPath = webappPath + File.separator + "gen" + File.separator + lowerModelName + File.separator + lowerModelName + "_add.html";
			String editPath = webappPath + File.separator + "gen" + File.separator + lowerModelName + File.separator + lowerModelName + "_edit.html";
			String viewPath = webappPath + File.separator + "gen" + File.separator + lowerModelName + File.separator + lowerModelName + "_view.html";
			
			Map<String, String> pathMap = new HashMap<>();
			pathMap.put("controllerPath", controllerPath);
			pathMap.put("modelPath", modelPath);
			pathMap.put("servicePath", servicePath);
			pathMap.put("serviceimplPath", serviceimplPath);
			pathMap.put("sqlPath", sqlPath);
			pathMap.put("indexPath", indexPath);
			pathMap.put("addPath", addPath);
			pathMap.put("editPath", editPath);
			pathMap.put("viewPath", viewPath);
			
			//mkdirs
			for (Map.Entry<String, String> entry : pathMap.entrySet()) {  
				File file = new File(entry.getValue());
				if (file.exists()) {
					continue;
				} else {
					file.getParentFile().mkdirs();
				}
			}
			
			//java
			String baseTemplatePath = File.separator + Cst.me().getRealPath() + File.separator + "WEB-INF" + File.separator + "view" + File.separator + "common" + File.separator + "_template" + File.separator;
			String controllerTemplatePath = baseTemplatePath + "_controller" + File.separator + "_controller.bld";
			String modelTemplatePath = baseTemplatePath + "_model" + File.separator +  "_model.bld";
			String serviceTemplatePath = baseTemplatePath + "_service" + File.separator + "_service.bld";
			String serviceimplTemplatePath = baseTemplatePath + "_service" + File.separator + "_impl" + File.separator + "_serviceimpl.bld";
			
			//resources
			String sqlTemplatePath = baseTemplatePath + "_sql" + File.separator + "_sql.bld";
			
			//webapp
			String indexTemplatePath = baseTemplatePath + "_view" + File.separator + "_index.bld";
			String addTemplatePath = baseTemplatePath + "_view" + File.separator + "_add.bld";
			String editTemplatePath = baseTemplatePath + "_view" + File.separator + "_edit.bld";
			String viewTemplatePath = baseTemplatePath + "_view" + File.separator + "_view.bld";
			
			Paras ps = Paras.create();
			ps.set("realPath", realPath);
			ps.set("packageName", packageName);
			ps.set("modelName", upperModelName);
			ps.set("lowerModelName", lowerModelName);
			ps.set("tableName", tableName);
			ps.set("pkName", pkName);
			
			//java
			BeetlMaker.makeFile(controllerTemplatePath, ps, controllerPath);
			BeetlMaker.makeFile(serviceTemplatePath, ps, servicePath);
			BeetlMaker.makeFile(serviceimplTemplatePath, ps, serviceimplPath);
			setParasAttr(tableName, ps);
			BeetlMaker.makeFile(modelTemplatePath, ps, modelPath);
			
			//resources
			BeetlMaker.makeFile(sqlTemplatePath, ps, sqlPath);
			
			//webapp
			BeetlMaker.makeFile(indexTemplatePath, ps, indexPath);
			BeetlMaker.makeFile(addTemplatePath, ps, addPath);
			BeetlMaker.makeFile(editTemplatePath, ps, editPath);
			BeetlMaker.makeFile(viewTemplatePath, ps, viewPath);
			
			
		}
		
		return success("生成成功,已经存在的文件将会覆盖!");
	}
	

	private void setParasAttr(String table, Paras ps) {
		SQLManager sm = Blade.dao();
		final TableDesc  tableDesc = sm.getMetaDataManager().getTable(table);
		Set<String> cols = tableDesc.getCols();
		List<Map<String, Object>> attrs = new ArrayList<>();
		boolean tempDouble = false;
		boolean tempDate = false;
		for(String col : cols){
			
			ColDesc desc = tableDesc.getColDesc(col);
			Map<String, Object> attr = Paras.createHashMap();
			attr.put("comment", desc.remark);
			String attrName = sm.getNc().getPropertyName(null, desc.colName);
			attr.put("name", attrName);
			attr.put("methodName", getMethodName(attrName));
			
			attr.put("type", desc.remark);
			
			String type = JavaType.getType(desc.sqlType, desc.size, desc.digit);
			if(type.equals("Double")){
				type = "BigDecimal";
				tempDouble = true;
			}		
			if(type.equals("Timestamp")){
				type ="Date";
				tempDate = true;
			}
			
			attr.put("type", type);
			attr.put("desc", desc);
			attrs.add(attr);
		}
		
		// 主键总是拍在前面，int类型也排在前面，剩下的按照字母顺序排
		Collections.sort(attrs,new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				ColDesc desc1  = (ColDesc) o1.get("desc");
				ColDesc desc2  = (ColDesc) o2.get("desc");
				int score1 = score(desc1);
				int score2 = score(desc2);
				if(score1 == score2){
					return desc1.colName.compareTo(desc2.colName);
				}else{
					return score2 - score1;
				}
			}
			
			private int score(ColDesc desc){
				if(tableDesc.getIdNames().contains(desc.colName)) {
					return 99;
				}else if(JavaType.isInteger(desc.sqlType)) {
					return 9;
				}else if(JavaType.isDateType(desc.sqlType)) {
					return -9;
				}else{
					return 0;
				}
			}
			
		});

		String srcHead = "";
		String CR = System.getProperty("line.separator");
		if(tempDate) {
			srcHead += "import java.util.Date;" + CR;
		}
		if(tempDouble) {
			srcHead += "import java.math.BigDecimal;" + CR;
		}
		
		ps.set("attrs", attrs);
		ps.set("imports", srcHead);
	}
	
	private String getMethodName(String name) {
		char ch1 = name.charAt(0);
		char ch2 = name.charAt(1);
		if(Character.isLowerCase(ch1) && Character.isUpperCase(ch2)) {
			//aUname---> getaUname();
			return name;
		} else if(Character.isUpperCase(ch1) && Character.isUpperCase(ch2)) {
			//ULR --> getURL();
			return name ;
		} else {
			//general  name --> getName()
			char upper = Character.toUpperCase(ch1);
			return upper + name.substring(1);
		}
	}
}

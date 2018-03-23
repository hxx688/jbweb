package com.rrgy.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.jeecgframework.poi.excel.entity.vo.MapExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rrgy.common.base.BaseController;
import com.rrgy.core.constant.ConstCache;
import com.rrgy.core.interfaces.ILoader;
import com.rrgy.core.plugins.dao.Db;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.core.toolbox.support.SqlKeyword;

@Controller
@RequestMapping("/excel")
public class ExcelController extends BaseController {

	private static String cacheName = ConstCache.FILE_CACHE;

	@ResponseBody
	@RequestMapping("/preExport")
	@SuppressWarnings("unchecked")
	public AjaxResult preExport(@RequestParam String postdata, @RequestParam String colnames, @RequestParam String colmodel, @RequestParam String source, @RequestParam String code) {
		Map<String, Object> _postdata = JsonKit.parse(postdata, HashMap.class);
		String[] _colname = colnames.replace("[", "").replace("]", "").split(",");
		List<Map<String, String>> _colmodel = JsonKit.parse(colmodel, ArrayList.class);

		String md_source = Md.getSql(source);
		String menu_source = getInfoByCode(code, "SOURCE");

		String _source = (StrKit.notBlank(menu_source)) ? menu_source : md_source;

		if (StrKit.isBlank(_source)) {
			return error("未找到与该模块匹配的数据源！");
		}

		String where = Func.toStr(_postdata.get("where"));
		Object sidx = _postdata.get("sidx");
		Object sord = _postdata.get("sord");
		Object sort = _postdata.get("sort");
		Object order = _postdata.get("order");
		if (!Func.isEmpty(sidx)) {
			sort = sidx + " " + sord + (Func.isEmpty(sort) ? ("," + sort) : "");
		}
		String orderby = (Func.isOneEmpty(sort, order)) ? (" order by " + sort + " " + order) : "";
		Map<String, Object> para = JsonKit.parse(Func.isEmpty(Func.decodeUrl(where)) ? null : Func.decodeUrl(where), HashMap.class);
		if (Func.isEmpty(para)) {
			para = new HashMap<>();
		}
		String sql = "select {} from (" + _source + ") a " + SqlKeyword.getWhere(where,"where") + orderby;

		CacheKit.remove(cacheName, EXCEL_SQL + code);
		CacheKit.remove(cacheName, EXCEL_SQL_PARA + code);
		CacheKit.remove(cacheName, EXCEL_COL_NAME + code);
		CacheKit.remove(cacheName, EXCEL_COL_MODEL + code);
		CacheKit.put(cacheName, EXCEL_SQL + code, sql);
		CacheKit.put(cacheName, EXCEL_SQL_PARA + code, para);
		CacheKit.put(cacheName, EXCEL_COL_NAME + code, _colname);
		CacheKit.put(cacheName, EXCEL_COL_MODEL + code, _colmodel);

		return json(code);
	}

	/**
	 * excel视图方式
	 */
	@RequestMapping("/export")
	public String export(ModelMap modelMap, HttpServletResponse response, @RequestParam String code) {
		String sql = CacheKit.get(cacheName, EXCEL_SQL + code);
		Map<String, Object> para = CacheKit.get(cacheName, EXCEL_SQL_PARA + code);
		String[] _colname = CacheKit.get(cacheName, EXCEL_COL_NAME + code);
		List<Map<String, String>> _colmodel = CacheKit.get(cacheName, EXCEL_COL_MODEL + code);

		List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
		StringBuilder sb = new StringBuilder();
		int cnt = 0;
		for (Map<String, String> m : _colmodel) {
			if (cnt > 1) {
				if (Func.toStr(m.get("hidden")).equals("true")) {
					cnt++;
					continue;
				}
				String name = m.get("name");
				entityList.add(new ExcelExportEntity(_colname[cnt], name, Func.toInt(m.get("widthOrg"), 70) / 4));
				sb.append(name).append(",");
			}
			cnt++;
		}

		String menu_name = getInfoByCode(code, "NAME");
		@SuppressWarnings("rawtypes")
		List<Map> dataResult = Db.selectList(Func.format(sql, StrKit.removeSuffix(sb.toString(), ",")), para);
		ExportParams exportParams = new ExportParams(menu_name + " 数据导出表", "导出人账号：" + ShiroKit.getUser().getLoginName() + "      导出时间：" + DateKit.getTime(), code);
		exportParams.setColor(HSSFColor.GREY_50_PERCENT.index);
		exportParams.setAddIndex(true);
		exportParams.setIndexName("序号");

		modelMap.put(MapExcelConstants.ENTITY_LIST, entityList);
		modelMap.put(MapExcelConstants.MAP_LIST, dataResult);
		modelMap.put(MapExcelConstants.FILE_NAME, menu_name + DateKit.getAllTime());
		modelMap.put(NormalExcelConstants.PARAMS, exportParams);
		return MapExcelConstants.JEECG_MAP_EXCEL_VIEW;
	}

	private String getInfoByCode(String code, String col) {
		List<Map<String, Object>> menu = CacheKit.get(MENU_CACHE, MENU_TABLE_ALL, new ILoader() {
					public Object load() {
						return Db.selectList("select CODE,PCODE,NAME,URL,SOURCE,PATH,TIPS,ISOPEN from TFW_MENU order by levels asc,num asc");
					}
				});
		for (Map<String, Object> _menu : menu) {
			if (code.equals(Func.toStr(_menu.get("CODE")))) {
				String _info = Func.toStr(_menu.get(col));
				return _info;
			}
		}
		return "";
	}

}

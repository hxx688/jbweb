package com.rrgy.industry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rrgy.common.base.BaseController;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.industry.model.Industry;
import com.rrgy.industry.service.IndustryService;

/**
 * Generated by Blade.
 * 2017-01-04 14:59:34
 */
@Controller
@RequestMapping("/industry")
public class IndustryController extends BaseController {
	private static String CODE = "industry";
	private static String PREFIX = "dt_hangye";
	private static String LIST_SOURCE = "industry.list";
	private static String BASE_PATH = "/industry/";
	
	@Autowired
	IndustryService service;
	
	@RequestMapping(KEY_MAIN)
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "industry.html";
	}

	@RequestMapping(KEY_ADD)
	public String add(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "industry_add.html";
	}

	@RequestMapping(KEY_EDIT + "/{id}")
	public String edit(@PathVariable String id, ModelMap mm) {
		Industry industry = service.findById(id);
		mm.put("model", JsonKit.toJson(industry));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "industry_edit.html";
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	public String view(@PathVariable String id, ModelMap mm) {
		Industry industry = service.findById(id);
		mm.put("model", JsonKit.toJson(industry));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "industry_view.html";
	}

	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		Object grid = paginate(LIST_SOURCE);
		return grid;
	}

	@ResponseBody
	@RequestMapping(KEY_SAVE)
	public AjaxResult save() {
		Industry industry = mapping(PREFIX, Industry.class);
		boolean temp = service.save(industry);
		if (temp) {
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_UPDATE)
	public AjaxResult update() {
		Industry industry = mapping(PREFIX, Industry.class);
		boolean temp = service.update(industry);
		if (temp) {
			return success(UPDATE_SUCCESS_MSG);
		} else {
			return error(UPDATE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_REMOVE)
	public AjaxResult remove(@RequestParam String ids) {
		int cnt = service.deleteByIds(ids);
		if (cnt > 0) {
			return success(DEL_SUCCESS_MSG);
		} else {
			return error(DEL_FAIL_MSG);
		}
	}
}

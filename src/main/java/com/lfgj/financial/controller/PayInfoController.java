package com.lfgj.financial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfgj.financial.service.PayInfoService;
import com.rrgy.common.base.BaseController;
import com.rrgy.core.toolbox.ajax.AjaxResult;

/**
 * Generated by Blade.
 * 2017-09-18 21:29:22
 */
@Controller
@RequestMapping("/payinfo")
public class PayInfoController extends BaseController {
	private static String CODE = "payinfo";
	private static String PREFIX = "dt_payinfo";
	private static String LIST_SOURCE = "payinfo.list";
	private static String BASE_PATH = "/payinfo/";
	
	@Autowired
	PayInfoService service;
	
	@RequestMapping(KEY_MAIN)
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "payinfo.html";
	}
	
	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		Object grid = paginate(LIST_SOURCE);
		return grid;
	}
	
	@ResponseBody
	@RequestMapping("/syncStatus")
	public AjaxResult syncStatus() throws Exception{
		String ids = getParameter("ids");
		boolean temp = service.forceStatus(ids);
		if (temp) {
			return success("操作成功!");
		} else {
			return error("操作失败!");
		}
	}
	
}

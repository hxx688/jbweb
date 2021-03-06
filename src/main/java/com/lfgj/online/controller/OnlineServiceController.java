package com.lfgj.online.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rrgy.common.base.BaseController;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.lfgj.online.intercept.OnlineIntercept;
import com.lfgj.online.model.OnlineService;
import com.lfgj.online.service.OnlineServiceService;

/**
 * Generated by Blade.
 * 2017-09-15 16:10:50
 */
@Controller
@RequestMapping("/onlineService")
public class OnlineServiceController extends BaseController {
	private static String CODE = "onlineService";
	private static String PREFIX = "dt_online_service";
	private static String LIST_SOURCE = "onlineService.list";
	private static String BASE_PATH = "/onlineService/";
	
	@Autowired
	OnlineServiceService service;
	
	@RequestMapping(KEY_MAIN)
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "onlineService.html";
	}

	@RequestMapping(KEY_ADD)
	public String add(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "onlineService_add.html";
	}

	@RequestMapping(KEY_EDIT + "/{id}")
	public String edit(@PathVariable String id, ModelMap mm) {
		OnlineService onlineService = service.findById(id);
		mm.put("model", JsonKit.toJson(onlineService));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "onlineService_edit.html";
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	public String view(@PathVariable String id, ModelMap mm) {
		OnlineService onlineService = service.findById(id);
		mm.put("model", JsonKit.toJson(onlineService));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "onlineService_view.html";
	}

	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		Object grid = paginate(LIST_SOURCE,new OnlineIntercept());
		return grid;
	}

	@ResponseBody
	@RequestMapping(KEY_SAVE)
	public AjaxResult save() {
		OnlineService onlineService = mapping(PREFIX, OnlineService.class);
		boolean temp = service.save(onlineService);
		if (temp) {
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_UPDATE)
	public AjaxResult update() {
		ShiroUser user = ShiroKit.getUser();
		OnlineService onlineService = mapping(PREFIX, OnlineService.class);
		onlineService.setReplay_time(new Date());
		onlineService.setReplay_id(Integer.valueOf(user.getId().toString()));
		onlineService.setStatus(1);
		boolean temp = service.update(onlineService);
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

package com.rrgy.auditconf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rrgy.common.base.BaseController;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.system.model.User;
import com.rrgy.auditconf.intercept.ConfIntercept;
import com.rrgy.auditconf.model.AuditConf;
import com.rrgy.auditconf.service.AuditConfService;

/**
 * Generated by Blade.
 * 2017-01-05 14:05:29
 */
@Controller
@RequestMapping("/auditConf")
public class AuditConfController extends BaseController {
	private static String CODE = "auditConf";
	private static String PREFIX = "dt_audit_conf";
	private static String LIST_SOURCE = "auditConf.list";
	private static String BASE_PATH = "/auditConf/";
	
	@Autowired
	AuditConfService service;
	
	@RequestMapping(KEY_MAIN)
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "auditConf.html";
	}

	@RequestMapping(KEY_ADD)
	public String add(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "auditConf_add.html";
	}

	@RequestMapping(KEY_EDIT + "/{id}")
	public String edit(@PathVariable String id, ModelMap mm) {
		AuditConf auditConf = service.findById(id);
		Paras rd = Paras.parse(auditConf);
		mm.put("model", JsonKit.toJson(auditConf));
		mm.put("id", id);
		mm.put("auditConf", rd);
		mm.put("code", CODE);
		return BASE_PATH + "auditConf_edit.html";
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	public String view(@PathVariable String id, ModelMap mm) {
		Blade blade = Blade.create(User.class);
		AuditConf auditConf = service.findById(id);
		Paras rd = Paras.parse(auditConf);
		User u1 = blade.findById(auditConf.getUser_id());
		auditConf.setUser_name(u1.getName());
		User u2 = blade.findById(auditConf.getAdd_user());
		auditConf.setAdd_user_name(u2.getName());
		mm.put("model", JsonKit.toJson(auditConf));
		mm.put("id", id);
		mm.put("auditConf", rd);
		mm.put("code", CODE);
		return BASE_PATH + "auditConf_view.html";
	}

	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		Object grid = paginate(LIST_SOURCE,new ConfIntercept());
		return grid;
	}

	@ResponseBody
	@RequestMapping(KEY_SAVE)
	public AjaxResult save() {
		AuditConf auditConf = mapping(PREFIX, AuditConf.class);
		//获取当前登录用户
		ShiroUser u = ShiroKit.getUser();
		int userId = (Integer)u.getId();
		auditConf.setAdd_user(userId);
		auditConf.setAdd_time(DateKit.parseTime(DateKit.getTime()));
		boolean temp = service.save(auditConf);
		if (temp) {
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_UPDATE)
	public AjaxResult update() {
		AuditConf auditConf = mapping(PREFIX, AuditConf.class);
		boolean temp = service.update(auditConf);
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

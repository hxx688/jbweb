package com.rrgy.person.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rrgy.common.base.BaseController;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.annotation.DoLog;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.person.common.PersonView;
import com.rrgy.person.intercept.PersonAuditIntercept;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;

/**
 * 银牌合伙人申请
 * Generated by Blade.
 * 2016-12-29 12:54:00
 */
@Controller
@RequestMapping("/personSilverApply")
public class personSilverApplyController extends BaseController {
	private static String CODE = "personSilverApply";
	private static String PREFIX = "dt_users";
	private static String LIST_SOURCE = "person.listSilverApply";
	private static String BASE_PATH = "/person/";
	
	@Autowired
	PersonService service;
	
	@RequestMapping(KEY_MAIN)
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "personSilverApply.html";
	}

	@RequestMapping(KEY_ADD)
	public String add(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "person_add.html";
	}

	@RequestMapping(KEY_EDIT + "/{id}")
	public String edit(@PathVariable String id, ModelMap mm) {
		PersonView.getPersonView(mm,service,id);
		mm.put("code", CODE);
		return BASE_PATH + "person_audit.html";
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	public String view(@PathVariable String id, ModelMap mm) {
		PersonView.getPersonView(mm,service,id);
		mm.put("code", CODE);
		return BASE_PATH + "personPartner_view.html";
	}

	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		Object grid = paginate(LIST_SOURCE,new PersonAuditIntercept());
		return grid;
	}

	@ResponseBody
	@RequestMapping(KEY_SAVE)
	public AjaxResult save() {
		Person person = mapping(PREFIX, Person.class);
		boolean temp = service.save(person);
		if (temp) {
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_UPDATE)
	public AjaxResult update() {
		Person person = mapping(PREFIX, Person.class);
		boolean flag = service.checkMobile(person);
		if(flag){
			boolean temp = service.update(person);
			if (temp) {
				return success(UPDATE_SUCCESS_MSG);
			} else {
				return error(UPDATE_FAIL_MSG);
			}
		}else{
			return error("修改失败，手机号码已存在！");
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
	
	@ResponseBody
	@RequestMapping("/audit")
	@DoLog(name = "[银牌合伙人]审核")
	public AjaxResult audit() {
		// 获取当前登录用户
		ShiroUser u = ShiroKit.getUser();
		//String id = getParameter("ids");
		Person person = mapping(PREFIX, Person.class);
		int flag = 0;
		try {
			flag = service.auditLevelPerson(u,person);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(flag == 1){
			return success("操作成功!");
		}else if(flag == 2){
			return error("审核失败，您没有审核的权限!");
		}else if(flag == 3){
			return error("审核失败，您已经审核过!");
		}else{
			return error("操作失败!");
		}
	}
}

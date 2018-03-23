/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (cbjr@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rrgy.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rrgy.common.base.BaseController;
import com.rrgy.common.tool.SysCache;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.annotation.Before;
import com.rrgy.core.annotation.Permission;
import com.rrgy.core.constant.ConstShiro;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.meta.intercept.RoleIntercept;
import com.rrgy.system.meta.intercept.RoleValidator;
import com.rrgy.system.model.Role;
import com.rrgy.system.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{
	private static String LIST_SOURCE = "role.list";
	private static String BASE_PATH = "/system/role/";
	private static String CODE = "role";
	private static String PREFIX = "tfw_role";
	
	@Autowired
	RoleService service;

	@RequestMapping("/")
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "role.html";
	}
	
	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		Object gird = paginate(LIST_SOURCE, new RoleIntercept());
		return gird;
	}
	
	@RequestMapping(KEY_ADD)
	public String add(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "role_add.html";
	}
	
	@RequestMapping(KEY_ADD + "/{id}")
	public String add(@PathVariable String id, ModelMap mm) {
		if (StrKit.notBlank(id)) {
			mm.put("pId", id);
			mm.put("num", service.findLastNum(id));
		}
		mm.put("code", CODE);
		return BASE_PATH + "role_add.html";
	}
	
	@RequestMapping(KEY_EDIT + "/{id}")
	public String edit(@PathVariable String id, ModelMap mm) {
		Role role = service.findById(id);
		mm.put("model", JsonKit.toJson(role));
		mm.put("code", CODE);
		ShiroUser user = ShiroKit.getUser();
		if(!user.getId().toString().equals("1")){
			return BASE_PATH + "role_edit_sys.html";
		}else{
			return BASE_PATH + "role_edit.html";
		}
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	public String view(@PathVariable String id, ModelMap mm) {
		Role role = service.findById(id);
		Role parent = service.findById(role.getPid());
		String pName = (null == parent) ? "" : parent.getName();
		Paras rd = Paras.parse(role);
		rd.set("deptName", SysCache.getDeptName(role.getDeptid()))
			.set("pName", pName);
		mm.put("model", JsonKit.toJson(rd));
		mm.put("code", CODE);
		return BASE_PATH + "role_view.html";
	}
	
//	@Permission({ConstShiro.ADMINISTRATOR, ConstShiro.ADMIN})
	@RequestMapping("/authority")
	public String authority(ModelMap mm) {
		mm.put("roleId", getParameter("roleId"));
		mm.put("roleName", getParameterToDecode("roleName"));
		return BASE_PATH + "role_authority.html";
	}
	
	@ResponseBody
	@Before(RoleValidator.class)
	@RequestMapping("/saveAuthority")
	public AjaxResult saveAuthority() {
		String ids = getParameter("ids");
		String roleId = getParameter("roleId");
		String[] id = ids.split(",");
		if (id.length <= 1) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			return success("设置成功");
		}
		boolean temp = service.authority(ids, roleId);
		if (temp) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			return success("设置成功");
		} else {
			return error("设置失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(KEY_SAVE)
	public AjaxResult save() {
		Role role = mapping(PREFIX, Role.class);
		Object pid = role.getPid();
		if (null == pid) {
			role.setPid(0);
		}
		boolean temp = service.save(role);
		if (temp) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}
	
	@ResponseBody
	@RequestMapping(KEY_UPDATE)
	public AjaxResult update() {
		Role role = mapping(PREFIX, Role.class);
		boolean temp = service.update(role);
		if (temp) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			return success(UPDATE_SUCCESS_MSG);
		} else {
			return error(UPDATE_FAIL_MSG);
		}
	}
	
	@ResponseBody
	@RequestMapping(KEY_REMOVE)
	public AjaxResult remove() {
		String ids = getParameter("ids");
		if(ids.indexOf("-1")!=-1){
			return error("不能删除系统角色");
		}
		int cnt = service.deleteByIds(getParameter("ids"));
		if (cnt > 0) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			return success(DEL_SUCCESS_MSG);
		} else {
			return error(DEL_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping("/getPowerById")
	public AjaxResult getPowerById() {
		int cnt = service.getParentCnt(getParameter("id"));
		if (cnt > 0) {
			return success("success");
		} else {
			return error("error");
		}
	}
	
}

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

import com.rrgy.common.base.BaseController;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.aop.AopContext;
import com.rrgy.core.constant.Cst;
import com.rrgy.core.interfaces.ILoader;
import com.rrgy.core.plugins.dao.Db;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.StrKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cache")
public class CacheController extends BaseController {

	public void index() {

	}

	/**
	 * 获取按钮组
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/getBtn")
	public AjaxResult getBtn() {
		final String code = getParameter("code");
		ShiroUser user = ShiroKit.getUser();
		final String userId = Func.toStr(user.getId());
		final String roleId = Func.toStr(user.getRoles());

		Map<String, Object> userRole = Db.selectOneByCache(ROLE_CACHE, ROLE_EXT + userId, "select * from TFW_ROLE_EXT where userId= #{id}", Paras.create().set("id", userId));

		String roleIn = "0";
		String roleOut = "0";
		if (!Func.isEmpty(userRole)) {
			roleIn = Func.toStr(userRole.get("ROLEIN"));
			roleOut = Func.toStr(userRole.get("ROLEOUT"));
		}
		final StringBuilder sql = new StringBuilder();
		sql.append("select TFW_MENU.* ,(select name from TFW_MENU where code=#{code}) as PNAME  from TFW_MENU");
		sql.append(" where ( ");
		sql.append("	 (status=1)");
		sql.append("	 and (icon is not null and (icon like '%btn%' or icon like '%icon%' ) ) ");
		sql.append("	 and (pCode=#{code})");
		sql.append("	 and (id in (select menuId from TFW_RELATION where roleId in (#{join(roleId)})) or id in (#{join(roleIn)}))");
		sql.append("	 and id not in(#{join(roleOut)})");
		sql.append("	)");
		sql.append(" order by num");


		List<Map> btnList = Db.selectListByCache(MENU_CACHE, BTN_LIST + code + "_" + userId, sql.toString(), 
				Paras.create()
				.set("code", code)
				.set("roleId", roleId.split(","))
				.set("roleIn", roleIn.split(","))
				.set("roleOut", roleOut.split(",")));
		
		
		
		return json(btnList);
	}

	/**
	 * 获取按钮组
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/getChildBtn")
	public AjaxResult getChildBtn() {
		final String code = getParameter("code");
		ShiroUser user = ShiroKit.getUser();
		final String userId = Func.toStr(user.getId());
		final String roleId = Func.toStr(user.getRoles());

		Map<String, Object> userRole = Db.selectOneByCache(ROLE_CACHE, ROLE_EXT + userId, "select * from TFW_ROLE_EXT where userId= #{id}", Paras.create().set("id", userId));

		String roleIn = "0";
		String roleOut = "0";
		if (!Func.isEmpty(userRole)) {
			roleIn = Func.toStr(userRole.get("ROLEIN"));
			roleOut = Func.toStr(userRole.get("ROLEOUT"));
		}
		final StringBuilder sql = new StringBuilder();
		sql.append("select TFW_MENU.* ,(select name from TFW_MENU where code=#{code}) as PNAME  from TFW_MENU");
		sql.append(" where ( ");
		sql.append("	 (status=1)");
		sql.append("	 and (icon is not null and (icon like '%btn%' or icon like '%icon%' ) ) ");
		sql.append("	 and (pCode=#{code})");
		sql.append("	 and (id in (select menuId from TFW_RELATION where roleId in (#{join(roleId)})) or id in (#{join(roleIn)}))");
		sql.append("	 and id not in(#{join(roleOut)})");
		sql.append("	)");
		sql.append(" order by num");

		List<Map> btnList = Db.selectListByCache(MENU_CACHE, BTN_CHILD_LIST + code + "_" + userId, sql.toString(), 
				Paras.create()
				.set("code", code)
				.set("roleId", roleId.split(","))
				.set("roleIn", roleIn.split(","))
				.set("roleOut", roleOut.split(",")));
		
		return json(btnList);
	}

	/**
	 * 根据字典编号获取下拉框
	 *
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/getSelect")
	public AjaxResult getSelect() {
		final String code = getParameter("code");
		final String num = getParameter("num");

		List<Map> dict = Db.selectListByCache(DICT_CACHE, DICT_SELECT + code, "select num as ID,pId as PID,name as TEXT from  TFW_DICT where code=#{code} and num>0", Paras.create().set("code", code));
		
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"form-control\" style=\"margin-left:-3px;cursor:pointer;\" id=\"inputs"
				+ num + "\">");
		sb.append("<option value></option>");
		for (Map dic : dict) {
			sb.append("<option value=\"" + dic.get("ID") + "\">" + dic.get("TEXT") + "</option>");
		}
		sb.append("</select>");
		return json(sb.toString());
	}

	@ResponseBody
	@RequestMapping("/getCombo")
	public AjaxResult getCombo() {
		String type = getParameter("type");
		if (StrKit.equalsIgnoreCase(type, "diy")) {
			final String source = getParameter("source");
			String where = getParameter("where");
			Map<String, Object> param = Paras.createHashMap();
			if (StrKit.notBlank(where)) {
				param = JsonKit.parse(where);
			}
			final Map<String, Object> map = param;
			List<Map<String, Object>> diy = CacheKit.get(DIY_CACHE, DICT_COMBO + type + source,
					new ILoader() {
						public Object load() {
							return Db.selectList(Md.getSql(source), map);
						}
					});
			return json(diy);
		} else {
			final String code = getParameter("code");
			List<Map<String, Object>> dict = CacheKit.get(DICT_CACHE, DICT_COMBO + type + code,
					new ILoader() {
						public Object load() {
							return Db.selectList("select num as \"id\",name as \"text\" from  TFW_DICT where code=#{code} and num>0", Paras.create().set("code", code));
						}
					});
			return json(dict);
		}
	}

	@ResponseBody
	@RequestMapping("/getDeptSelect")
	public AjaxResult getDeptSelect() {
		final String num = getParameter("num");
		List<Map<String, Object>> dept = CacheKit.get(DEPT_CACHE, DEPT_ALL_LIST,
				new ILoader() {
					public Object load() {
						return Db.selectList("select ID,PID,simpleName as TEXT from  TFW_DEPT order by pId,num asc", Paras.create(), new AopContext(), Cst.me().getDefaultSelectFactory().deptIntercept());
					}
				});
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"form-control\" style=\"margin-left:-3px;cursor:pointer;\" id=\"inputs"
				+ num + "\">");
		sb.append("<option value></option>");
		for (Map<String, Object> _dept : dept) {
			sb.append("<option value=\"" + _dept.get("ID") + "\">" + _dept.get("TEXT") + "</option>");
		}
		sb.append("</select>");
		return json(sb.toString());
	}

	@ResponseBody
	@RequestMapping("/getUserSelect")
	public AjaxResult getUserSelect() {
		final String num = getParameter("num");
		List<Map<String, Object>> user = CacheKit.get(USER_CACHE, USER_SELECT_ALL,
				new ILoader() {
					public Object load() {
						return Db.selectList("select ID,name TEXT from TFW_USER where status=1 and name is not null order by name ", Paras.create(), new AopContext(), Cst.me().getDefaultSelectFactory().userIntercept());
					}
				});
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"form-control\" style=\"margin-left:-3px;cursor:pointer;\" id=\"inputs"
				+ num + "\">");
		sb.append("<option value></option>");
		for (Map<String, Object> _user : user) {
			sb.append("<option value=\"" + _user.get("ID") + "\">" + _user.get("TEXT") + "</option>");
		}
		sb.append("</select>");
		return json(sb.toString());
	}

	@ResponseBody
	@RequestMapping("/getRoleSelect")
	public AjaxResult getRoleSelect() {
		final String num = getParameter("num");
		List<Map<String, Object>> role = CacheKit.get(ROLE_CACHE, ROLE_ALL_LIST,
				new ILoader() {
					public Object load() {
						return Db.selectList("select ID,name TEXT from TFW_Role where  name is not null order by name ", Paras.create(), new AopContext(), Cst.me().getDefaultSelectFactory().roleIntercept());
					}
				});
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"form-control\" style=\"margin-left:-3px;cursor:pointer;\" id=\"inputs"
				+ num + "\">");
		sb.append("<option value></option>");
		for (Map<String, Object> _role : role) {
			sb.append("<option value=\"" + _role.get("ID") + "\">" + _role.get("TEXT") + "</option>");
		}
		sb.append("</select>");
		return json(sb.toString());
	}
	
	@ResponseBody
	@RequestMapping("/getDiySelect")
	public AjaxResult getDiySelect() {
		final String num = getParameter("num");
		final String source = getParameter("source");
		String where = getParameter("where");
		Map<String, Object> param = Paras.createHashMap();
		if (StrKit.notBlank(where)) {
			param = JsonKit.parse(where);
		}
		final Map<String, Object> map = param;
		List<Map<String, Object>> diy = CacheKit.get(DIY_CACHE, DIY_SELECT + source,
				new ILoader() {
					public Object load() {
						return Db.selectList(Md.getSql(source), map);
					}
				});
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"form-control\" style=\"margin-left:-3px;cursor:pointer;\" id=\"inputs" + num + "\">");
		sb.append("<option value></option>");
		for (Map<String, Object> _diy : diy) {
			sb.append("<option value=\"" + _diy.get("ID") + "\">" + _diy.get("TEXT") + "</option>");
		}
		sb.append("</select>");
		return json(sb.toString());
	}

	@ResponseBody
	@RequestMapping("/dicTreeList")
	public AjaxResult dicTreeList() {
		List<Map<String, Object>> dic = CacheKit.get(DICT_CACHE, DICT_TREE_ALL,
				new ILoader() {
					public Object load() {
						return Db.selectList("select code \"code\",id \"id\",pId \"pId\",name \"name\",num \"num\",'false' \"open\" from TFW_DICT order by code asc,num asc", Paras.create());
					}
				});

		return json(dic);
	}

	@ResponseBody
	@RequestMapping("/deptTreeList")
	public AjaxResult deptTreeList() {
		List<Map<String, Object>> dept = CacheKit.get(DEPT_CACHE, DEPT_TREE_ALL + "_" + ShiroKit.getUser().getId(),
				new ILoader() {
					public Object load() {
						return Db.selectList("select id \"id\",pId \"pId\",simpleName as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_DEPT ", Paras.create(), new AopContext("ztree"), Cst.me().getDefaultSelectFactory().deptIntercept());
					}
				});

		return json(dept);
	}

	@ResponseBody
	@RequestMapping("/roleTreeList")
	public AjaxResult roleTreeList() {
		List<Map<String, Object>> dept = CacheKit.get(ROLE_CACHE, ROLE_TREE_ALL + "_" + ShiroKit.getUser().getId(),
				new ILoader() {
					public Object load() {
						return Db.selectList("select id \"id\",pId \"pId\",name as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_ROLE ", Paras.create(), new AopContext("ztree"), Cst.me().getDefaultSelectFactory().roleIntercept());
					}
				});

		return json(dept);
	}

	@ResponseBody
	@RequestMapping("/getDicById")
	public AjaxResult getDicById() {
		final int id = getParameterToInt("id");
		List<Map<String, Object>> dict = CacheKit.get(DICT_CACHE, DICT_CODE + id,
				new ILoader() {
					public Object load() {
						return Db.selectList("select CODE from TFW_DICT where id=#{id}", Paras.create().set("id", id));
					}
				});
		return json(dict);
	}

	@ResponseBody
	@RequestMapping("/menuTreeList")
	public AjaxResult menuTreeList() {
		List<Map<String, Object>> menu = CacheKit.get(MENU_CACHE, MENU_TREE_ALL,
				new ILoader() {
					public Object load() {
						return Db.selectList("select code \"id\",pCode \"pId\",name \"name\",(case when levels=1 then 'true' else 'false' end) \"open\" from TFW_MENU where status=1 order by levels asc,num asc");
					}
				});

		return json(menu);
	}

	@ResponseBody
	@RequestMapping("/menuTreeListByRoleId")
	public AjaxResult menuTreeListByRoleId() {
		final String roleId = getParameter("roleId", "0");
		List<Map<String, Object>> menu = CacheKit.get(MENU_CACHE, MENU_TREE + roleId,
				new ILoader() {
					@SuppressWarnings("rawtypes")
					public Object load() {
						String table = "TFW_MENU";
						String pid = "";
						List<Map> pids = Db.selectList("select PID from TFW_ROLE where id in (#{join(roleId)})", Paras.create().set("roleId", roleId.split(",")));
						for (Map p : pids) {
							if (!Func.isEmpty(p.get("PID")) && Func.toInt(p.get("PID")) > 0) {
								pid += p.get("PID").toString() + ",";
							}
						}
						if (!Func.isEmpty(pid)) {
							pid = StrKit.removeSuffix(pid, ",");
							table = "(select * from TFW_MENU where id in( select MENUID from TFW_RELATION where roleId in (#{join(pid)}) ))";
						}
						StringBuilder sb = new StringBuilder();
						sb.append("select m.id \"id\",(select id from TFW_MENU  where code=m.pCode) \"pId\",name \"name\",(case when m.levels=1 then 'true' else 'false' end) \"open\",(case when r.menuId is not null then 'true' else 'false' end) \"checked\"");
						sb.append(" from ");
						sb.append(table);
						sb.append(" m left join (select MENUID from TFW_RELATION where roleId in (#{join(roleId)}) GROUP BY MENUID) r on m.id=r.menuId where m.status=1 order by m.levels,m.num asc");
						return Db.selectList(sb.toString(), Paras.create().set("roleId", roleId.split(",")).set("pid", pid.split(",")));
					}
				});

		return json(menu);
	}

	@ResponseBody
	@RequestMapping("/roleTreeListById")
	public AjaxResult roleTreeListById() {
		final String Id = getParameter("id");
		final String roleId = getParameter("roleId", "0");
		List<Map<String, Object>> menu = CacheKit.get(ROLE_CACHE, ROLE_TREE + Id,
				new ILoader() {
					public Object load() {
						String sql = "select id \"id\",pId \"pId\",name as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_ROLE where r.id>0  order by pId,num asc";
						if (Id.indexOf(",") == -1) {
							sql = "select r.id \"id\",pId \"pId\",name as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\",(case when (r1.ID=0 or r1.ID is null) then 'false' else 'true' end) \"checked\" from  TFW_ROLE r left join (select ID  from TFW_ROLE where ID in ("
									+ "#{join(roleId)}"
									+ ")) r1 on r.ID=r1.ID where r.id>0 order by pId,num asc";
						}
						return Db.selectList(sql, Paras.create().set("roleId", roleId.split(",")));
					}
				});

		return json(menu);
	}
	
	@ResponseBody
	@RequestMapping("/theme")
	public AjaxResult theme() {
		if (null == ShiroKit.getUser()) {
			return error("error");
		}
		Map<String, String> theme = CacheKit.get(SYS_CACHE, ACE_THEME + ShiroKit.getUser().getId() , new ILoader() {
			public Object load() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("ace", "ace-dark.css");
				return map;
			}
		});
		String currentTheme = theme.get("ace");
		theme.put("ace", (StrKit.equals(currentTheme, "ace-dark.css") ? "ace-white.css" : "ace-dark.css"));
		return success("success");
	}
	
}

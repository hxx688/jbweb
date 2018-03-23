package com.rrgy.common.tool;

import java.util.List;
import java.util.Map;

import com.rrgy.core.constant.ConstCache;
import com.rrgy.core.constant.ConstCacheKey;
import com.rrgy.core.interfaces.ILoader;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Db;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.model.Dept;
import com.rrgy.system.model.Dict;
import com.rrgy.system.model.Parameter;
import com.rrgy.system.model.Role;
import com.rrgy.system.model.User;

public class SysCache implements ConstCache, ConstCacheKey{
	
	/**
	 * 获取字典表对应中文
	 * @param code 字典编号
	 * @param num  字典序号
	 * @return
	 */
	public static String getDictName(final Object code, final Object num) {
		Dict dict = CacheKit.get(DICT_CACHE, GET_DICT_NAME + code + "_" + num, new ILoader() {
			@Override
			public Object load() {
				return Blade.create(Dict.class).findFirstBy("code = #{code} and num = #{num}", Paras.create().set("code", code).set("num", num));
			}
		});
		if(null == dict){
			return "";
		}
		return dict.getName();
	}
	
	/**
	 * 获取字典表
	 * @param code 字典编号
	 * @return
	 */
	public static List<Dict> getDict(final Object code) {
		List<Dict> list = CacheKit.get(DICT_CACHE, GET_DICT + code, new ILoader() {
			@Override
			public Object load() {
				return Blade.create(Dict.class).findBy("code = #{code} and num > 0", Paras.create().set("code", code));
			}
		});
		return list;
	}
	
	/**
	 * 获取字典表
	 * @param code 字典编号
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Map> getSimpleDict(final Object code) {
		List<Map> list = CacheKit.get(DICT_CACHE, GET_DICT + "simple_" + code, new ILoader() {
			@Override
			public Object load() {
				return Db.selectList("select num, name, tips from tfw_dict where code = #{code} and num > 0", Paras.create().set("code", code)); 
			}
		});
		return list;
	}

	/**
	 * 获取对应角色名
	 * @param roleId 角色id
	 * @return
	 */
	public static String getRoleName(final Object roleIds) {
		if(Func.isEmpty(roleIds)){
			return "";
		}
		final String [] roleIdArr = roleIds.toString().split(",");
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < roleIdArr.length; i++){
			final String roleId = roleIdArr[i];
			Role role = CacheKit.get(ROLE_CACHE, GET_ROLE_NAME + roleId, new ILoader() {
				@Override
				public Object load() {
					return Blade.create(Role.class).findById(roleId);
				}
			});
			if (null != role)
				sb.append(role.getName()).append(",");
		}
		return StrKit.removeSuffix(sb.toString(), ",");
	}

	/**
	 * 获取对应角色别名
	 * @param roleId 角色id
	 * @return
	 */
	public static String getRoleAlias(final Object roleIds) {
		if(Func.isEmpty(roleIds)){
			return "";
		}
		final String [] roleIdArr = roleIds.toString().split(",");
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < roleIdArr.length; i++){
			final String roleId = roleIdArr[i];
			Role role = CacheKit.get(ROLE_CACHE, GET_ROLE_ALIAS + roleId, new ILoader() {
				@Override
				public Object load() {
					return Blade.create(Role.class).findById(roleId);
				}
			});
			if (null != role)
				sb.append(role.getTips()).append(",");
		}
		return StrKit.removeSuffix(sb.toString(), ",");
	}

	/**
	 * 获取对应用户名
	 * @param userId 用户id
	 * @return
	 */
	public static String getUserName(final Object userId) {
		User user = CacheKit.get(USER_CACHE, GET_USER_NAME + userId, new ILoader() {
			@Override
			public Object load() {
				return Blade.create(User.class).findById(userId);
			}
		});
		if(null == user){
			return "";
		}
		return user.getName();
	}

	/**
	 * 获取对应部门名
	 * @param deptIds 部门id集合
	 * @return
	 */
	public static String getDeptName(final Object deptIds) {
		if(Func.isEmpty(deptIds)){
			return "";
		}
		final String [] deptIdArr = deptIds.toString().split(",");
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < deptIdArr.length; i++){
			final String deptId = deptIdArr[i];
			Dept dept = CacheKit.get(DEPT_CACHE, GET_DEPT_NAME + deptId, new ILoader() {
				@Override
				public Object load() {
					return Blade.create(Dept.class).findById(deptId);
				}
			});
			if (null != dept)
				sb.append(dept.getSimplename()).append(",");
		}
		return StrKit.removeSuffix(sb.toString(), ",");
	}
	
	/**   
	 * 获取参数表参数值
	 * @param code 参数编号
	 * @return String
	*/
	public static String getParamByCode(String code){
		Parameter param = Blade.create(Parameter.class).findFirstBy("code = #{code} and status = 1", Paras.create().set("code", code));
		return param.getPara();
	}
}

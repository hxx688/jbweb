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
package com.rrgy.system.meta.intercept;

import java.util.List;
import java.util.Map;

import com.lfgj.task.ProductCronTask;
import com.lfgj.task.TradeCronTask;
import com.lfgj.util.LfConstant;
import com.rrgy.common.tool.SysCache;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.aop.AopContext;
import com.rrgy.core.base.controller.BladeController;
import com.rrgy.core.constant.ConstCache;
import com.rrgy.core.meta.MetaIntercept;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.grid.BladePage;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.system.model.Parameter;

public class ParameterIntercept extends MetaIntercept {
	
	public void queryBefore(AopContext ac) {
		ShiroUser shiroUser = ShiroKit.getUser();
		String sql = "";
		if(!"1".equals(shiroUser.getId().toString())){
			sql += " and type != -1";
		}
		ac.setCondition(sql);
	}
	
	/**
	 * 查询后操作
	 * 
	 * @param ac
	 */
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			map.put("STATUSNAME", SysCache.getDictName(901, map.get("STATUS")));
		}
	}
	
	/**
	 * 主表新增前操作
	 * 
	 * @param ac
	 */
	public void saveBefore(AopContext ac) {
		BladeController ctrl = ac.getCtrl();
		String code = ctrl.getParameter("tfw_parameter.code");
		int cnt = Blade.create(Parameter.class).count("code = #{code}", Paras.create().set("code", code));
		if(cnt > 0){
			throw new RuntimeException("参数编号已存在!");
		}
	}
	
	/**
	 * 主表新增后操作(事务内)
	 * 
	 * @param ac
	 */
	public boolean saveAfter(AopContext ac) {
		CacheKit.remove(ConstCache.SYS_CACHE, "parameter_log");
		return super.saveAfter(ac);
	}
	
	/**
	 * 主表修改后操作(事务内)
	 * 
	 * @param ac
	 */
	public boolean updateAfter(AopContext ac) {
		CacheKit.remove(ConstCache.SYS_CACHE, "parameter_log");
		Parameter obj = (Parameter)ac.getObject();
		Parameter params = Blade.create(Parameter.class).findById(obj.getId());

		if(params!=null&&"104".equals(params.getCode())){
			CacheKit.put(LfConstant.CACHE_NAME, "parameter_104",params);
		}
		if(params!=null&&"205".equals(params.getCode())){
			CacheKit.put(LfConstant.CACHE_NAME, "parameter_205",params);
		}
		if(params!=null&&"203".equals(params.getCode())){
			CacheKit.put(LfConstant.CACHE_NAME, "parameter_203",params);
		}
		
		if(params!=null&&"202".equals(params.getCode())){
			ProductCronTask.cron = "0/"+params.getPara()+" * * * * ?";
		}
		if(params!=null&&"204".equals(params.getCode())){
			TradeCronTask.cron = "0/"+params.getPara()+" * * * * ?";
		}
		return super.updateAfter(ac);
	}
	
	/**
	 * 删除全部完毕后操作(事务外)
	 * 
	 * @param ac
	 */
	public AjaxResult removeSucceed(AopContext ac) {
		CacheKit.remove(ConstCache.SYS_CACHE, "parameter_log");
		return super.removeSucceed(ac);
	}
	
	/**
	 * 逻辑删除后操作(事务外)
	 * 
	 * @param ac
	 */
	public AjaxResult delSucceed(AopContext ac) {
		CacheKit.remove(ConstCache.SYS_CACHE, "parameter_log");
		return super.delSucceed(ac);
	}
	
	/**
	 * 还原全部完毕后操作(事务外)
	 * 
	 * @param ac
	 */
	public AjaxResult restoreSucceed(AopContext ac) {
		CacheKit.remove(ConstCache.SYS_CACHE, "parameter_log");
		return super.restoreSucceed(ac);
	}
	
}

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

import com.rrgy.common.tool.SysCache;
import com.rrgy.core.aop.Invocation;
import com.rrgy.core.constant.ConstShiro;
import com.rrgy.core.intercept.BladeValidator;
import com.rrgy.core.plugins.dao.Db;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.CollectionKit;
import com.rrgy.core.toolbox.kit.StrKit;

public class RoleValidator extends BladeValidator {

	@Override
	protected void doValidate(Invocation inv) {
		validateRole("roleId", "ids", "超级管理员不能去掉角色管理的权限!");
	}

	protected void validateRole(String field1, String field2, String errorMessage) {
		String ids = request.getParameter(field2);
		if (StrKit.isBlank(ids)) {
			addError("请选择权限!");
		} 
		String roleId = request.getParameter(field1);
		String roleAlias = SysCache.getRoleAlias(roleId);
		if(roleAlias.equals(ConstShiro.ADMINISTRATOR)){
			String[] id = ids.split(",");
			String authority = Db.queryStr("select id from tfw_menu where code = #{code}", Paras.create().set("code", "role_authority"));
			if(!CollectionKit.contains(id, authority)){
				//超管不包含权限配置则报错
				addError(errorMessage);
			}
		}
	}

}

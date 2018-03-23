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

import com.rrgy.core.aop.Invocation;
import com.rrgy.core.intercept.BladeValidator;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.model.Menu;

public class MenuValidator extends BladeValidator {

	@Override
	protected void doValidate(Invocation inv) {
		
		if (inv.getMethod().toString().indexOf("update") == -1) {
			validateRequired("tfw_menu.pcode", "请输入菜单父编号");
			validateCode("tfw_menu.code", "菜单编号已存在!");
		}
		validateSql("tfw_menu.source", "含有非法字符,请仔细检查!");
		
	}

	protected void validateCode(String field, String errorMessage) {
		String code = request.getParameter(field);
		if (StrKit.isBlank(code)) {
			addError("请输入菜单编号!");
		}
		Blade blade = Blade.create(Menu.class);
		String sql = "select * from tfw_menu where code = #{code}";
		boolean temp = blade.isExist(sql, Paras.create().set("code", code));
		if (temp) {
			addError(errorMessage);
		}
	}

}

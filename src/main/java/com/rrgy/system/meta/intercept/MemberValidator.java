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
import com.rrgy.system.model.User;

public class MemberValidator extends BladeValidator {

	@Override
	protected void doValidate(Invocation inv) {
		if(inv.getMethod().toString().indexOf("update") != -1) { // 编辑
			validateTwoNotEqual("lf_member.id","lf_member.parent_id","推荐人不能是本人");
		}else if(inv.getMethod().toString().indexOf("recharge") != -1) { // 虚拟充值
			validateInteger("lf_member.withdraw",1,1000000,"充值金额应在1~1000000之间");
		}
		
		validateAccount("手机号已存在");
	}

	protected void validateAccount(String errorMessage) {
		String mobile = request.getParameter("lf_member.mobile");
		String id = request.getParameter("lf_member.id");
		if (StrKit.isBlank(mobile)) {
			addError("请输入账号!");
		}
		String sql = "SELECT * FROM lf_member WHERE mobile = #{mobile}";
		if(StrKit.isNotEmpty(id)){
			sql += " and id <> " + id;
		}
		if (Blade.create(User.class).isExist(sql, Paras.create().set("mobile", mobile))) {
			addError(errorMessage);
		}
	}

}

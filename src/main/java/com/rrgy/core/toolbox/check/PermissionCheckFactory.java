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
package com.rrgy.core.toolbox.check;

import javax.servlet.http.HttpServletRequest;

import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.constant.Cst;
import com.rrgy.core.interfaces.ICheck;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.kit.CollectionKit;
import com.rrgy.core.toolbox.kit.HttpKit;

/**
 * 权限自定义检查
 */
public class PermissionCheckFactory implements ICheck {

	@Override
	public boolean check(Object[] permissions) {
		ShiroUser user = ShiroKit.getUser();
		if (null == user) {
			return false;
		}
		String join = CollectionKit.join(permissions, ",");
		if(ShiroKit.hasAnyRoles(join)){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkAll() {
		HttpServletRequest request = HttpKit.getRequest();
		ShiroUser user = ShiroKit.getUser();
		if (null == user) {
			return false;
		}
		String requestURI = request.getRequestURI().replace(Cst.me().getContextPath(), "");
		String[] str = requestURI.split("/");
		if (str.length > 3) {
			requestURI = str[1] + "/" + str[2];
		}
		if(ShiroKit.hasPermission(requestURI)){
			return true;
		}
		return false;
	}

}

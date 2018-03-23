package com.rrgy.common.intercept;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.constant.ConstShiro;
import com.rrgy.core.intercept.QueryInterceptor;
import com.rrgy.core.shiro.ShiroKit;

public class SelectRoleIntercept extends QueryInterceptor {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String roles = ShiroKit.getUser().getRoles() + "," + ShiroKit.getUser().getSubRoles();
			String condition = "where id in (#{join(ids)})";
			ac.setCondition(condition);
			ac.getParam().put("ids", roles.split(","));
		}
	}

}

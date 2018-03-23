package com.rrgy.common.intercept;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.constant.ConstShiro;
import com.rrgy.core.intercept.QueryInterceptor;
import com.rrgy.core.shiro.ShiroKit;

public class SelectDeptIntercept extends QueryInterceptor {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String depts = ShiroKit.getUser().getSuperDepts() + "," + ShiroKit.getUser().getDeptId() + "," + ShiroKit.getUser().getSubDepts();
			String condition = "where id in (#{join(ids)})";
			ac.setCondition(condition);
			ac.getParam().put("ids", depts.split(","));
		}
	}

}

package com.rrgy.system.meta.intercept;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.constant.ConstShiro;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.shiro.ShiroKit;

public class DeptIntercept extends PageIntercept {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String depts = ShiroKit.getUser().getDeptId() + "," + ShiroKit.getUser().getSubDepts();
			String condition = "and id in (#{join(ids)})";
			ac.setCondition(condition);
			ac.getParam().put("ids", depts.split(","));
		}
	}

}

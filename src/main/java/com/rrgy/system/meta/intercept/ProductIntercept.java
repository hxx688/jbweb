package com.rrgy.system.meta.intercept;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;

public class ProductIntercept extends PageIntercept {

	public void queryBefore(AopContext ac) {
		String condition = "and is_agent = 1";
		ac.setCondition(condition);
	}

}

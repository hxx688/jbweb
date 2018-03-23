package com.rrgy.common.intercept;

import com.rrgy.core.intercept.SelectInterceptor;
import com.rrgy.core.interfaces.IQuery;

public class DefaultSelectFactory extends SelectInterceptor {
	
	public IQuery deptIntercept() {
		return new SelectDeptIntercept();
	}
	
	public IQuery roleIntercept() {
		return new SelectRoleIntercept();
	}
	
}

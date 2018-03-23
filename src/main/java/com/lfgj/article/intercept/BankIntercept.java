package com.lfgj.article.intercept;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;

public class BankIntercept extends PageIntercept{

	public void queryBefore(AopContext ac) {
		String condition = " select name ID,name TEXT from dt_yinhang order by id";
		ac.setCondition(condition);
	}
}

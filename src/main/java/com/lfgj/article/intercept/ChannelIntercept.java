package com.lfgj.article.intercept;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;

public class ChannelIntercept extends PageIntercept{

	public void queryBefore(AopContext ac) {
		String condition = " select id ID,title TEXT from dt_channel";
		ac.setCondition(condition);
	}
}

package com.lfgj.article.intercept;

import java.util.Map;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.toolbox.Func;

public class CategoryIntercept extends PageIntercept{
	public void queryBefore(AopContext ac) {
		Map<String, Object> params = ac.getParam();
		Object title = params.get("channel_name_skip");
		if(!Func.isEmpty(title)){
			String condition = " and (select title from dt_channel where c.channel_id=id) like '%"+title+"%'";
			ac.setCondition(condition);
		}
	}
}

package com.lfgj.article.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.grid.BladePage;

public class ArticleIntercept extends PageIntercept{
	
	private String from;
	private String authscope;
	private String categroy;
	public void setFrom(String from) {
		this.from = from;
	}

	public void setAuthscope(String authscope) {
		this.authscope = authscope;
	}

	public void setCategroy(String categroy) {
		this.categroy = categroy;
	}

	public void queryBefore(AopContext ac) {		
		if(!"1".equals(from)){
			ac.setCondition(" select id ID,title TEXT from dt_article_category");
		}else{
			String condition = "";
			if(!Func.isEmpty(authscope)){
				condition += " and groupids_view = "+authscope;
			}
			if(!Func.isEmpty(categroy)){
				condition += " and category_id = "+categroy;
			}
			ac.setCondition(condition);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		if("1".equals(from)){
			BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
			List<Map<String, Object>> list = page.getRows();
			for (Map<String, Object> map : list) {
				map.put("groupids_view", loadViewName(map.get("groupids_view").toString()));
			}
		}
	}
	
	private String loadViewName(String level){
		if("1".equals(level)){
			return "<span>会员</span>";
		}else if("2".equals(level)){
			return "<span>代理商</span>";
		}else{
			return "<span>不限</span>";
		}
	}
}

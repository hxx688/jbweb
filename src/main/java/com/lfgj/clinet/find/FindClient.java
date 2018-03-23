package com.lfgj.clinet.find;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.article.model.Article;
import com.lfgj.system.model.Top;
import com.lfgj.system.service.TopService;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Paras;

@Service
@Client(name = "lf_find")
public class FindClient extends RequestMethod{
	
	@Autowired
	TopService service;
	
	public ResultVo index() {
		ResultVo rv = new ResultVo();
		String is_agent = getParams("is_agent","");
		Top top = service.findById(1);
		Article article = new Article();
		article.setCategory_id(1);
		String groupview="";
		if("0".equals(is_agent)){
			groupview = "1,0";
		}else{
			groupview = "2,0";
		}
		Paras p = Paras.create().set("category_id", "1");
		String sql = " select * from dt_article where category_id=#{category_id} and groupids_view in ("+groupview+") order by add_time desc ";
		List<Article> articles = Blade.create(Article.class).findTop(5,sql,p);
		Map<String,Object> vl = new HashMap<String,Object>();
		vl.put("top", top);
		vl.put("articles", articles);
		rv.setReturnCode("0");
		rv.setReturnParams(vl);
		rv.setReturnMsg("");	
		return rv;
	}
}

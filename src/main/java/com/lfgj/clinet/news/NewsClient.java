package com.lfgj.clinet.news;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.article.model.Article;
import com.lfgj.article.service.ArticleService;
import com.lfgj.util.LfConstant;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.JsonKit;

@Service
@Client(name = "lf_news")
public class NewsClient extends RequestMethod{

	@Autowired
	ArticleService service;
	
	public ResultVo list() {
		ResultVo rv = new ResultVo();
		String page = getParams("pageNo","1");
		String rows = getParams("pageSize", LfConstant.PAGE_SIZE);
		String is_agent = getParams("is_agent","");
		Map<String,String> param = new HashMap<String,String>();
		param.put("category_id_equal","2");	
		if("0".equals(is_agent)){
			param.put("groupids_view_notequal","2");
		}else{
			param.put("groupids_view_notequal","1");
		}
		
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "article.list",para,""," add_time desc ");	
		rv.setReturnCode("0");
		rv.setReturnParams(grid);
		rv.setReturnMsg("");	
		return rv;
	}
	
	public ResultVo view() {
		ResultVo rv = new ResultVo();
		String id = getParams("id","");	
		Article article = service.findById(id);
		if(article==null){
			rv.setReturnCode("1");
			rv.setReturnMsg("没有数据");
		}else{
			rv.setReturnCode("0");
			rv.setReturnParams(article);
		}
		return rv;
	}
}

package com.lfgj.clinet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lfgj.article.model.Article;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.system.model.Attach;
import com.rrgy.system.model.Parameter;

@Service
@Client(name = "lf_index")
public class IndexClient extends RequestAbs{

	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		Parameter param = new Parameter();
		param.setCode("200");
		param = Blade.create(Parameter.class).findTopOne(param);		
		String vls = param.getPara();
		
		Map<String,Object> obj = new HashMap<String,Object>();
		List<Attach> attachs = Blade.create(Attach.class).findBy(" id in (#{join(id)})", Paras.create().set("id", vls.split(",")));
		if (attachs.size() > 1) {
			Attach attach1 = attachs.get(0);
			Attach attach2 = attachs.get(attachs.size() - 1);
			obj.put("imgList1", attach1);
			obj.put("imgList2", attach2);
		}
		
		if (attachs.size() > 0) {
			obj.put("imgList", attachs);
		}
	
		Article article = Blade.create(Article.class).findFirstBy(" category_id=#{category_id} order by add_time desc ", Paras.create().set("category_id",1));
		obj.put("article", article);

		rv.setReturnCode("0");
		rv.setReturnParams(obj);
		rv.setReturnMsg("");
		return rv;
	}

}

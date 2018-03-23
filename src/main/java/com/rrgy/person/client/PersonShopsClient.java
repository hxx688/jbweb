package com.rrgy.person.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.interfaces.IQuery;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.intercept.PersonIntercept;
import com.rrgy.person.service.PersonService;



@Service
@Client(name = "rrgy_2017_1.0_personShops")
public class PersonShopsClient extends RequestAbs{
	
	@Autowired
	PersonService service;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String group_id = getParams("group_id", "");
		String type = getParams("type", "");//类别
		String bili = getParams("bili","");
		String user_id = getParams("user_id","");
		String title = getParams("title","");
		
		Map<String,String> param = new HashMap<String,String>();

		param.put("group_id_equal", group_id);
		param.put("bili_skip", bili);
		if(!Func.isEmpty(user_id)){
			param.put("shangjilist", ","+user_id+",");
		}
		if(!Func.isEmpty(type)){
			param.put("hangye_skip", type);
		}
		
		String para = JsonKit.toJson(param);
		IQuery query = new PersonIntercept();
		Object grid = GridManager.paginate(null,Integer.valueOf(page), Integer.valueOf(rows), "person.listAll",para,"reg_time","desc",query,null);	
		rv.setReturnParams(grid);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取商家成功");
		return JsonKit.toJson(rv);
	}

}

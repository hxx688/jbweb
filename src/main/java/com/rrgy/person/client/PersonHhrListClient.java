package com.rrgy.person.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;

/**
 * 合伙人
 * @author Administrator
 *
 */

@Service
@Client(name = "rrgy_2017_1.0_personHhrs")
public class PersonHhrListClient extends RequestAbs{
	
	@Autowired
	PersonService service;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String user_id = getParams("user_id", "");
		String type = getParams("search_type", "");//类别
		
		Map<String,String> param = new HashMap<String,String>();
		String sqlid = "person.listHhr";
		if(Func.isEmpty(type)){
			param.put("group_id_gt", "2");
		}else{
			param.put("group_id_equal", type);
		}
		
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), sqlid,para);	
		rv.setReturnParams(grid);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取合伙人成功");
		return JsonKit.toJson(rv);
	}

}

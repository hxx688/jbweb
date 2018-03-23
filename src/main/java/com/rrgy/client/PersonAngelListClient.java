package com.rrgy.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.grid.JqGrid;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;

/**
 * 我的天使豆
 * @author Administrator
 *
 */

@Service
@Client(name = "rrgy_2017_1.0_personAngelList")
public class PersonAngelListClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String type = getParams("type","");//1激励，２推荐，３获赠
		String bili = getParams("bili","1");
		String user_id = getParams("user_id", "");
		Paras paras = Paras.create();
		Map<String,String> param = new HashMap<String,String>();

		param.put("userId_equal", user_id);
		param.put("bili_equal", bili);
		paras.set("userId", user_id);
		paras.set("bili", bili);
		if("1".equals(type)){
			//查看用户是商家还是天使
			Person person = Blade.create(Person.class).findById(user_id);
			if(null != person){
				if(person.getGroup_id() == 1){
					param.put("status_equal", "1");
					paras.set("status", "1");
				}else if(person.getGroup_id() == 2){
					param.put("status_equal", "2");
					paras.set("status", "2");
				}else{
					param.put("status_equal", "3");
					paras.set("status", "3");
				}
			}else{
				rv.setReturnCode("1");
				rv.setReturnMsg("用户不存在");
				return JsonKit.toJson(rv);
			}
		}
		if("2".equals(type)){
			param.put("status_equal", "4");
			paras.set("status", "4");
		}
		
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "fafangjilu.listAll",para,"time","desc");	
		JqGrid<Map<String, Object>> gd = (JqGrid<Map<String, Object>>)grid;
		String total = Md.queryStr("fafangjilu.getDouTotal", paras);
		gd.setBean(total);//总计天使豆
		
		rv.setReturnParams(gd);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取天使豆记录成功");
		return JsonKit.toJson(rv);
	}

}

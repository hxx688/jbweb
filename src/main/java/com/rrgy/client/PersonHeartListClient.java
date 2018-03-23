package com.rrgy.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.grid.JqGrid;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.person.model.Person;

/**
 * 我的爱心
 * @author Administrator
 *
 */

@Service
@Client(name = "rrgy_2017_1.0_personHeartList")
public class PersonHeartListClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		String bili = getParams("bili","");
		String user_id = getParams("user_id", "");
		String search_type = getParams("search_type","");//1增加,0减少
		Paras paras = Paras.create();
		Map<String,String> param = new HashMap<String,String>();
		//查看用户是商家还是天使
		Person person = Blade.create(Person.class).findById(user_id);
		if(null != person){
			if(person.getGroup_id() == 1){
				param.put("leixing_equal", "1");
				paras.set("leixing", "1");
				param.put("bili_equal", bili);
				paras.set("bili", bili);
				paras.set("userId", user_id);
			}else if(person.getGroup_id() == 2){
				param.put("leixing_equal", "2");
				paras.set("leixing", "2");
				paras.set("userId", user_id);
			}else{
				rv.setReturnCode("0");
				rv.setReturnMsg("没有数据");
				return JsonKit.toJson(rv);
			}
		}
		param.put("userid_equal", user_id);
		if("0".equals(search_type)){
			search_type = "2";
		}
		if(!Func.isEmpty(search_type)){
			param.put("isadd_equal", search_type);
		}
		
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "aixinjilu.list",para,"add_time","desc");	
		JqGrid<Map<String, Object>> gd = (JqGrid<Map<String, Object>>)grid;
		String total = Md.queryStr("xiaofeijilu.getAixinAllTotal", paras);
		gd.setBean(total);//总计爱心
		
		rv.setReturnParams(gd);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取爱心记录成功");
		return JsonKit.toJson(rv);
	}

}

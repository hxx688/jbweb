package com.rrgy.client;

import org.springframework.stereotype.Service;

import com.rrgy.client.bean.Yesterday;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * 获取昨日激励天使豆
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_yesterdayJili")
public class YesterdayJiliClient extends RequestAbs{
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		String user_id = getParams("user_id","");
		
		if(Func.isEmpty(user_id)){
			rv.setReturnMsg("无效用户");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
	 
		Yesterday yesterday = new Yesterday();
		String date = DateTimeKit.yesterday().toString(DateTimeKit.NORM_DATE_PATTERN);
		String oneDou = Md.queryStr("fafangjilu.getDouTotal", Paras.create().set("bili", "1").set("userId", user_id).set("time", "%"+date+"%"));
		yesterday.setOne_dou(oneDou);
		String threeDou = Md.queryStr("fafangjilu.getDouTotal", Paras.create().set("bili", "3").set("userId", user_id).set("time", "%"+date+"%"));
		yesterday.setThree_dou(threeDou);
		String twoDou = Md.queryStr("fafangjilu.getDouTotal", Paras.create().set("bili", "2").set("userId", user_id).set("time", "%"+date+"%"));
		yesterday.setTwo_dou(twoDou);
		String allDou = Md.queryStr("fafangjilu.getDouTotal", Paras.create().set("userId", user_id).set("time", "%"+date+"%"));
		yesterday.setTotal_dou(allDou);
		
		boolean temp = true;
		if (temp) {
			rv.setReturnCode("0");
			rv.setReturnParams(yesterday);
			rv.setReturnMsg("获取昨日激励天使豆成功");
		} else {
			rv.setReturnCode("3");
			rv.setReturnMsg("获取昨日激励天使豆失败");
		}
		
		return JsonKit.toJson(rv);
	}
}

package com.rrgy.platform.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.Des3;
import com.rrgy.common.iface.RegisterCodeImpl;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.JsonKit;

@Service
@Client(name = "rrgy_2017_1.0_noticeList")
public class NoticeListClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String page = getParams("page","1");
		String rows = getParams("rows", "20");
		
		Map<String,String> param = new HashMap<String,String>();
		param.put("f_it_lx_equal", "10");
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "notice.list",para,"F_DT_CJSJ","desc");
		rv.setReturnParams(grid);
		rv.setReturnCode("0");
		rv.setReturnMsg("获取公告列表成功");
		return JsonKit.toJson(rv);
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8281/rrgy/clientService";
		String param = "{\"serviceId\":\"rrgy_2017_1.0_noticeList\","
				+ "\"params\":{}}";
		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
		rs = Des3.decode(rs);
		System.out.println(rs);
	}
}

package com.lfgj.clinet.customer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.online.model.OnlineService;
import com.lfgj.online.service.OnlineServiceService;
import com.lfgj.util.LfConstant;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.JsonKit;

@Service
@Client(name = "lf_customer")
public class CustomerClient extends RequestMethod{
	@Autowired
	OnlineServiceService service;
	
	public ResultVo list() {
		ResultVo rv = new ResultVo();
		String page = getParams("pageNo","1");
		String rows = getParams("pageSize", LfConstant.PAGE_SIZE);
		String send_id = getParams("send_id","");
		if(Func.isEmpty(send_id)){
			rv.setReturnCode("1");
			rv.setReturnMsg("会员不存在");	
			return rv;
		}
		
		Map<String,String> param = new HashMap<String,String>();
		param.put("send_id_equal",send_id);	
		
		String para = JsonKit.toJson(param);
		
		Object grid = GridManager.paginate(Integer.valueOf(page), Integer.valueOf(rows), "onlineService.list",para, "","create_time desc");	
		rv.setReturnCode("0");
		rv.setReturnParams(grid);
		rv.setReturnMsg("");	
		return rv;
	}
	
	public ResultVo add() {
		ResultVo rv = new ResultVo();
		String msg = getParams("msg","");	
		String send_id = getParams("send_id","");
		OnlineService online = new OnlineService();
		online.setCreate_time(new Date());
		online.setMsg(msg);
		online.setSend_id(Integer.valueOf(send_id));
		online.setStatus(0);
		boolean rs = service.save(online);
		if(rs){
			rv.setReturnCode("0");
			rv.setReturnParams("保存成功");
		}else{
			rv.setReturnCode("1");
			rv.setReturnMsg("保存失败");
		}
		return rv;
	}
}

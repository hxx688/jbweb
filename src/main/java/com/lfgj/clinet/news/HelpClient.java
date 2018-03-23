package com.lfgj.clinet.news;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.platform.model.Notice;

@Service
@Client(name = "lf_help")
public class HelpClient extends RequestMethod{
	
	public ResultVo view() {
		ResultVo rv = new ResultVo();
		String id = getParams("id","");	
		Notice notice = Blade.create(Notice.class).findById(id);
		rv.setReturnCode("0");
		rv.setReturnParams(notice);
		return rv;
	}
}

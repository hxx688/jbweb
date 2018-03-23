package com.rrgy.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * 积分比例
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_jiFen")
public class JifenClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		
		List<String> mData= new ArrayList<String>();
		 mData.add("30%,70%");
		 mData.add("50%,50%");
		 mData.add("70%,30%");
		 
		rv.setReturnCode("0");
		rv.setReturnParams(mData);
		rv.setReturnMsg("获取数据成功");
		return JsonKit.toJson(rv);
	}

}

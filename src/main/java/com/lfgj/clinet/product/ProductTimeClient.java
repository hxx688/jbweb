package com.lfgj.clinet.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.product.model.Product;
import com.lfgj.product.util.ProductCacheUtil;
import com.lfgj.product.util.ProductListCacheUtil;
import com.lfgj.util.CommKit;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;

@Service
@Client(name = "lf_productTime")
public class ProductTimeClient extends RequestAbs{

	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		String code = this.getParams("code", "");
		CommKit.display("　ProductTimeClient－－编号："+code);
		Product product = new Product();
		product.setCode(code);
		JSONObject json = ProductCacheUtil.init(code).get();
		
		CommKit.display("　json－－结果："+json);
		if(json!=null){
			product.setJson(json.toString());
		}
		
		Map<String, String> jsonline = ProductCacheUtil.init(code).allLine();
	
		if(jsonline!=null){			
			Collection<String> keys = jsonline.keySet();			
			String[] dates = new String[keys.size()];
			dates = keys.toArray(dates);			
			product.setxAxis(dates);
			
			Collection<String> dataList = jsonline.values();
			String[] datas = new String[dataList.size()];
			datas = dataList.toArray(datas);
			product.setyAxis(datas);			
			
		}else{
			Product p = ProductListCacheUtil.init().get(code);
			List<String> keyList = new ArrayList<String>();
			String startDate = CommKit.getStartTime(p.getSale_time());
			String endDate = CommKit.getEndTime(p.getSale_time());
			keyList.add(startDate);
			keyList.add(endDate);
			
			String[] dates = new String[keyList.size()];
			dates = keyList.toArray(dates);			
			product.setxAxis(dates);
			
			String[] datas = new String[]{"0","0"};
			product.setyAxis(datas);
		}
		

		CommKit.display("　json－－结果："+ Arrays.toString(product.getxAxis()));
		CommKit.display("　json－－结果："+ Arrays.toString(product.getyAxis()));
		
		rv.setReturnCode("0");
		rv.setReturnParams(product);
		rv.setReturnMsg("");
		return rv;
	}
}

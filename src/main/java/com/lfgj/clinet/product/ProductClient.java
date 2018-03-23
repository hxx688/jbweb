package com.lfgj.clinet.product;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.product.model.Product;
import com.lfgj.product.util.ProductCacheUtil;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstCache;
import com.rrgy.core.constant.ConstCacheKey;

@Service
@Client(name = "lf_product")
public class ProductClient extends RequestAbs implements ConstCache, ConstCacheKey{

	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		String code = this.getParams("code", "");
		
		Product product = new Product();
		product.setCode(code);
		JSONObject json = ProductCacheUtil.init(code).get();
		if(json!=null){
			product.setJson(json.toString());
		}
		
		rv.setReturnCode("0");
		rv.setReturnParams(product);
		rv.setReturnMsg("");
		return rv;
	}
}

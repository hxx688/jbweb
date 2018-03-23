package com.lfgj.clinet.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lfgj.product.model.Product;
import com.lfgj.product.util.ProductCacheUtil;
import com.lfgj.product.util.ProductListCacheUtil;
import com.lfgj.util.CommKit;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstCache;
import com.rrgy.core.constant.ConstCacheKey;

@Service
@Client(name = "lf_productList")
public class ProductListClient extends RequestAbs implements ConstCache, ConstCacheKey{

	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		try{
			List<Product> products = ProductListCacheUtil.init().getList();
			CommKit.display("　首页--商品数："+products.size());
			for(Product p:products){			
				JSONObject obj = ProductCacheUtil.init(p.getCode()).get();
				boolean isTask = CommKit.isTask(p.getCode());
				if(!isTask){
					p.setIsSale("0");
				}else{
					p.setIsSale("1");
				}
				if(obj!=null){
					p.setJson(obj.toJSONString());
				}else{
					String value = "{'Symbol':'"+p.getCode()+"','Name':'"+p.getProduct_name()+"','Date':'0','Open':'0','High':'0','Low':'0','NewPrice':'0'}";			
					p.setJson(value);
				}
			}
	
			rv.setReturnCode("0");
			rv.setReturnParams(products);
			rv.setReturnMsg("");
		}catch(Exception ex){
			rv.setReturnCode("1");
			rv.setReturnMsg("");
		}
		return rv;
	}
}

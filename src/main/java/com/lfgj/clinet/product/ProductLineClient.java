package com.lfgj.clinet.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lfgj.product.model.Product;
import com.lfgj.product.model.ProductSale;
import com.lfgj.util.CommKit;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstCache;
import com.rrgy.core.constant.ConstCacheKey;
import com.rrgy.core.plugins.dao.Blade;

@Service
@Client(name = "lf_productline")
public class ProductLineClient extends RequestAbs implements ConstCache, ConstCacheKey{

	@Override
	public ResultVo getResult() {
		ResultVo rv = new ResultVo();
		String pid = this.getParams("pid", "");
		String code = this.getParams("code", "");
		
		ProductSale productSale = new ProductSale();
		productSale.setProduct_id(Integer.valueOf(pid));
		List<ProductSale> productSales = Blade.create(ProductSale.class).findByTemplate(productSale);
		
		Product product = Blade.create(Product.class).findById(pid);
	
		Map<String,Object> rs = new HashMap<String,Object>();
		
		boolean isTask = CommKit.isTask(product.getCode());
		if(!isTask){
			product.setProduct_name("非交易时间");	
			rs.put("productSales","");
		}else{
			rs.put("productSales", productSales);
		}
		rs.put("product", product);
		
		rv.setReturnCode("0");
		rv.setReturnParams(rs);
		rv.setReturnMsg("");
		return rv;
	}
	
}

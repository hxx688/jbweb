package com.lfgj.clinet.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lfgj.product.model.Product;
import com.lfgj.product.model.ProductSale;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstCache;
import com.rrgy.core.constant.ConstCacheKey;
import com.rrgy.core.plugins.dao.Blade;

@Service
@Client(name = "lf_productSale")
public class ProductSaleClient extends RequestMethod implements ConstCache, ConstCacheKey{

	public ResultVo list() {
		ResultVo rv = new ResultVo();
		String pid = this.getParams("pid", "");
		
		ProductSale product = new ProductSale();
		product.setProduct_id(Integer.valueOf(pid));
		List<ProductSale> productSales = Blade.create(ProductSale.class).findByTemplate(product);
		
		rv.setReturnCode("0");
		rv.setReturnParams(productSales);
		rv.setReturnMsg("");
		return rv;
	}
	
	public ResultVo view(){
		ResultVo rv = new ResultVo();
		String psid = this.getParams("psid", "");
		
		ProductSale productSale = Blade.create(ProductSale.class).findById(psid);
		Product product = Blade.create(Product.class).findById(productSale.getProduct_id());
		productSale.setProduct_name(product.getProduct_name());
		
		rv.setReturnCode("0");
		rv.setReturnParams(productSale);
		rv.setReturnMsg("");
		return rv;
	}
}

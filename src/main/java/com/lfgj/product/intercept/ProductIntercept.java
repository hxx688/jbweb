package com.lfgj.product.intercept;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.meta.PageIntercept;

public class ProductIntercept extends PageIntercept{
	
	private String from;
	public void setFrom(String from) {
		this.from = from;
	}
	
	public void queryBefore(AopContext ac) {		
		if(!"1".equals(from)){
			ac.setCondition(" select id ID,product_name TEXT from dt_product where status = 1");
		}
	}
}

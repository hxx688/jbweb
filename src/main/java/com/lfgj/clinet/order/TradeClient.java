package com.lfgj.clinet.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.order.model.Order;
import com.lfgj.order.service.OrderService;
import com.lfgj.util.CommKit;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.Func;

@Service
@Client(name = "lf_trade")
public class TradeClient extends RequestMethod{
	@Autowired
	OrderService service;
	
	public ResultVo buy() {
		ResultVo rv = new ResultVo();
		String person_id = getParams("id","");
		String psid = getParams("psid","");//产品
		String ct = getParams("ct","");//手数
		String price = getParams("nowprice","");//当前产品价
		String buytype = getParams("buytype","");//１：买涨 2:买跌		
		
		if(Func.isEmpty(price)){
			rv.setReturnCode("1");
			rv.setReturnMsg("当前价格无效");	
			return rv;
		}
		
		if(Integer.valueOf(ct)<1){
			rv.setReturnCode("1");
			rv.setReturnMsg("手数无效");	
			return rv;
		}
		
		if(Float.valueOf(price).floatValue()<=0){
			rv.setReturnCode("1");
			rv.setReturnMsg("当前价格无效");	
			return rv;
		}
		
		if(Func.isEmpty(person_id)){
			rv.setReturnCode("1");
			rv.setReturnMsg("会员不存在");	
			return rv;
		}
		
		if(Func.isEmpty(psid)){
			rv.setReturnCode("1");
			rv.setReturnMsg("产品不存在");	
			return rv;
		}
		
		String rs = service.saveOrder(person_id,psid,ct,buytype,price);
		if("1".equals(rs)){
			rv.setReturnCode("1");
			rv.setReturnMsg("余额不足");			
		}else if("2".equals(rs)){
			rv.setReturnCode("2");
			rv.setReturnMsg("非交易时间不能购买");
		}else if("0".equals(rs)){
			rv.setReturnCode("0");
			rv.setReturnMsg("交易成功");
		}else{
			rv.setReturnCode("2");
			rv.setReturnMsg("交易失败");
		}
		return rv;
	}
	
	public ResultVo sale(){
		ResultVo rv = new ResultVo();
		String order_id = getParams("order_id","");	
		Order order = service.findById(order_id);
		if(order==null){
			rv.setReturnCode("2");
			rv.setReturnMsg("平仓失败");
			return rv;
		}
		
		boolean isTask = CommKit.isTask(order.getCode());		
		if(!isTask){
			rv.setReturnCode("1");
			rv.setReturnMsg("非交易时间不能平仓");
			return rv;
		}
		
		String rs = service.updateOrder(order_id);
		if("0".equals(rs)){
			rv.setReturnCode("0");
			rv.setReturnMsg("平仓成功");
		}else{
			rv.setReturnCode("2");
			rv.setReturnMsg("平仓失败");
		}
		return rv;
	}

}

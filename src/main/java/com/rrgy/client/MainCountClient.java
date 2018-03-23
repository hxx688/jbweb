package com.rrgy.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.client.bean.MainInfo;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.HtmlKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.MathKit;
import com.rrgy.person.service.PersonService;
import com.rrgy.platform.model.Notice;

/**
 * 获取首页统计信息
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_mainCount")
public class MainCountClient extends RequestAbs{
	@Autowired
	PersonService service;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		MainInfo info = new MainInfo();
		long headCount = Md.queryLong("index.index_all", null);
		info.setHeartCount(headCount+"");
		
		String yesterday = DateTimeKit.yesterday().toString(DateTimeKit.NORM_DATE_PATTERN);
		Paras para = Paras.create().set("yesterday","%"+yesterday+"%");
		
		long yesterdayChangeCount = Md.queryLong("index.index_change", para);
		info.setYesterdayChangeCount(yesterdayChangeCount+"");
		
		long remainCount = Md.queryLong("index.index_remain", null);
		info.setRemainCount(""+remainCount);
	
		BigDecimal countOrders = Md.queryDecimal("index.countOrders", null);
		info.setBuyHeart(changeAmount(countOrders));
		BigDecimal angelCount = Md.queryDecimal("index.countAngel", null);
		info.setAngelCount(angelCount+"");
		BigDecimal salesCount = Md.queryDecimal("index.countSales", null);
		info.setSalesCount(salesCount+"");
		
		Notice firstNotice = Blade.create(Notice.class).findFirst(Md.getSql("notice.list_first"), null);
		String nr = firstNotice.getF_tx_nr();
		nr = HtmlKit.html2Str(nr);
		info.setFirstNotice(nr);
		
		rv.setReturnCode("0");
		rv.setReturnParams(info);
		rv.setReturnMsg("获取数据成功");
		return JsonKit.toJson(rv);
	}

	private String changeAmount(BigDecimal v){
		String val = "";
		if(v.longValue()>10000){
			val = MathKit.div(v,10000, 2)+"万";
		}else{
			val = v+"";
		}
		return val;
	}
	
}

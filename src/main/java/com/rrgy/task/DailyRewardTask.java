package com.rrgy.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.lfgj.order.service.OrderService;
import com.rrgy.benefit.model.Benefit;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.system.model.OperationLog;

/**
 * 每日激励定时器
 * 每天凌晨12点执行
 * @author Administrator
 *
 */
public class DailyRewardTask implements Runnable {
	
	@Autowired
	OrderService orderService;

	@Override
	public void run() {
		
		//System.out.println("任务调度执行:" + DateKit.getTime());
		Blade benefitBlade = Blade.create(Benefit.class);
		Blade logBlade = Blade.create(OperationLog.class);
		
		
	}
	
	
	public static void main(String[] args) {
		System.out.println(ShiroKit.md5("123456", "X68X28"));
		
	}
	
}

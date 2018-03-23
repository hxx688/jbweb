package com.lfgj.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.lfgj.order.model.Order;
import com.lfgj.order.service.OrderService;
import com.lfgj.order.util.OrderCacheUtil;
import com.rrgy.core.plugins.connection.ConnectionPlugin;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.CollectionKit;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.system.model.Parameter;

@Lazy(false)  
@Component
@EnableScheduling
public class TradeCronTask implements SchedulingConfigurer {
	@Autowired
	OrderService orderService;
	
	public static String cron = "0/1 * * * * ?";
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ConnectionPlugin.init().start();
		Parameter param = new Parameter();
		param.setCode("204");
		param = Blade.create(Parameter.class).findTopOne(param);
		cron = "0/"+param.getPara()+" * * * * ?";
		
		List<Order> orders = Md.selectList("order.listAll", Paras.create().set("status","1"),Order.class);
		if(!CollectionKit.isEmpty(orders)){
			for(Order order:orders){
				OrderCacheUtil.init().put(order);
			}
		}
		
		System.out.println("TradeTask(订单平仓):"+DateKit.getTime());
		TradeTask productTask = new TradeTask();
		productTask.setOrderService(orderService);
		taskRegistrar.addTriggerTask(productTask, new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				// 任务触发，可修改任务的执行周期
				CronTrigger trigger = new CronTrigger(cron);
				Date nextExec = trigger.nextExecutionTime(triggerContext);
				return nextExec;
			}
		});
	}
}

package com.lfgj.task;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.lfgj.util.CommKit;
import com.rrgy.core.plugins.connection.ConnectionPlugin;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.system.model.Parameter;

@Lazy(false)  
@Component
@EnableScheduling
public class ProductCronTask implements SchedulingConfigurer {
	
	public static String cron = "0/1 * * * * ?";
	 
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ConnectionPlugin.init().start();
		
		System.out.println("K线图");
		CommKit.initLine();
		
		Parameter param = new Parameter();
		param.setCode("202");
		param = Blade.create(Parameter.class).findTopOne(param);
		BigDecimal c = new BigDecimal(param.getPara());
		cron = "0/"+c.intValue()+" * * * * ?";
		
		System.out.println("实时价");
		ProductTask productTask = new ProductTask();
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

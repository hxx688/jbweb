package com.rrgy.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfgj.member.model.Member;
import com.lfgj.order.model.Order;
import com.lfgj.order.service.OrderService;
import com.lfgj.tixian.model.Tixianjilu;
import com.rrgy.benefit.model.Benefit;
import com.rrgy.client.bean.MainInfo;
import com.rrgy.common.base.BaseController;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.constant.Const;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.support.DateTime;
import com.rrgy.person.model.Person;
import com.rrgy.system.model.Main;
import com.rrgy.system.model.OperationLog;
import com.rrgy.system.model.Parameter;
import com.rrgy.system.model.User;

@Controller
@RequestMapping("/main")
public class MainController extends BaseController {
	
	@Autowired
	OrderService orderService;
	
	private static String CODE = "main";

	@GetMapping
	public String index(ModelMap mm) {
		ShiroUser user = ShiroKit.getUser();
		User u = Blade.create(User.class).findById(user.getId());
		mm.put("user", u);
		mm.put("code", CODE);
		long startTime=System.currentTimeMillis();
		
		Parameter param = new Parameter();
		param.setCode("102");
		param = Blade.create(Parameter.class).findTopOne(param);
		
		Main main = new Main();
		main.setSite_name(param.getPara());
		mm.put("main", main);
		
		Member m = new Member();
		m.setStatus(0);
		m.setIs_agent(0);
		if(u.getIs_agent() == 1){
			m.setAgent_id(u.getId());
		}
		long member_count = Blade.create(Member.class).count(m);
		mm.put("member_count", member_count);
		
		m = new Member();
		m.setStatus(0);
		m.setIs_agent(1);
		if(u.getIs_agent() == 1){
			m.setAgent_id(u.getId());
		}
		long agent_count = Blade.create(Member.class).count(m);
		mm.put("agent_count", agent_count);
		
		String day = DateTimeKit.yesterday().toString(DateKit.DATE_FORMATE);
		Paras yesterday = Paras.create().set("yesterday","%"+day+"%");
		
		String sql = " reg_time like '%"+day+"%'";
		if(u.getIs_agent() == 1){
			sql = " reg_time like '%"+day+"%' and agent_id = "+u.getId();
			yesterday.set("agent_id", u.getId());
		}
		
		List<Member> today_m = Blade.create(Member.class).findBy(sql,null);
		mm.put("today_count", today_m.size());
		
		String tixain_count = Md.queryStr("index.indextixian", yesterday);
		mm.put("tixin_count", tixain_count);
		
		String order_count = Md.queryStr("index.countOrders", yesterday);
		mm.put("order_count", order_count);
		
		List<Order> toporders = Md.selectList("index.topOrders", yesterday, Order.class);		
		mm.put("toporders", toporders);
		
		List<Order> bottomorders = Md.selectList("index.bottomOrders", yesterday, Order.class);		
		mm.put("bottomorders", bottomorders);
		
		long endTime=System.currentTimeMillis(); 
		System.out.println("执行成功，程序运行时间： "+(endTime-startTime)+"ms");
		return Const.INDEX_MAIN_REALPATH;
	}
	
	@ResponseBody
	@RequestMapping("/getYingyee")
	public AjaxResult getYingyee(){
		MainInfo info = new MainInfo();
		String nowDate = DateTimeKit.date().toString("yyyy-MM-dd");
		
		Paras para1 = Paras.create().set("yesterday","%"+nowDate+"%").set("bili", "1");
		Paras para2 = Paras.create().set("yesterday","%"+nowDate+"%").set("bili", "2");
		Paras para3 = Paras.create().set("yesterday","%"+nowDate+"%").set("bili", "3");
		//获取今日商家营业额
		String sixNowCount = Md.queryStr("index.shopOrder", para1);
		info.setSixNowCount(sixNowCount);
		String twelveNowCount = Md.queryStr("index.shopOrder", para2);
		info.setTwelveNowCount(twelveNowCount);
		String twentyFourNowCount = Md.queryStr("index.shopOrder", para3);
		info.setTwentyFourNowCount(twentyFourNowCount);
		
		AjaxResult a = new AjaxResult();
		a.setData(info);
		return a;
	}
	
	@ResponseBody
	@RequestMapping("/six")
	public AjaxResult doSix(){
		Blade benefitBlade = Blade.create(Benefit.class);
		Benefit benefit =benefitBlade.findById(1);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Person> personList = Blade.create(Person.class).findBy(" group_id in (1,2) and status = #{status} ",
				Paras.create().set("status", 0));
		boolean temp = false;
		try {
			//map = orderService.addReward(benefit,1,personList,map);//天使
			//map = orderService.addReward(benefit,2,personList,map);//商家
			temp = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (temp) {
			return success("执行成功");
		} else {
			return error("执行失败");
		}
		
	}
	
	@ResponseBody
	@RequestMapping("/twelve")
	public AjaxResult doTwelve(){
		Blade benefitBlade = Blade.create(Benefit.class);
		Benefit benefit =benefitBlade.findById(2);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Person> personList = Blade.create(Person.class).findBy(" group_id in (1,2) and status = #{status} ",
				Paras.create().set("status", 0));
		boolean temp = false;
		try {
			//map = orderService.addReward(benefit,1,personList,map);//天使
			//map = orderService.addReward(benefit,2,personList,map);//商家
			temp = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (temp) {
			return success("执行成功");
		} else {
			return error("执行失败");
		}
		
	}
	
	@ResponseBody
	@RequestMapping("/twentyFour")
	public AjaxResult getTwentyFour(){
		Blade benefitBlade = Blade.create(Benefit.class);
		Benefit benefit =benefitBlade.findById(3);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Person> personList = Blade.create(Person.class).findBy(" group_id in (1,2) and status = #{status} ",
				Paras.create().set("status", 0));
		boolean temp = false;
		try {
			//map = orderService.addReward(benefit,1,personList,map);//天使
			//map = orderService.addReward(benefit,2,personList,map);//商家
			temp = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (temp) {
			return success("执行成功");
		} else {
			return error("执行失败");
		}
		
	}
	
	
	@ResponseBody
	@RequestMapping("/runReward")
	public AjaxResult runReward(){
		Blade benefitBlade = Blade.create(Benefit.class);
		Blade logBlade = Blade.create(OperationLog.class);
		//获取优惠组别列表
		List<Benefit> benefitList = benefitBlade.findAll();
	
		return success("执行完成");
		
	}
	
}

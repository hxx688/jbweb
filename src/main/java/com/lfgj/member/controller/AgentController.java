package com.lfgj.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.MD5;
import com.rrgy.common.base.BaseController;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.annotation.Before;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.meta.intercept.AgentIntercept;
import com.rrgy.system.meta.intercept.AgentRechargeValidator;
import com.rrgy.system.meta.intercept.MemberValidator;
import com.rrgy.system.meta.intercept.MyMemberIntercept;
import com.rrgy.system.model.User;

/**
 * Generated by Blade.
 * 2017-09-12 16:12:39
 * 代理商管理
 */
@Controller
@RequestMapping("/agent")
public class AgentController extends BaseController {
	private static String CODE = "agent";
	private static String PREFIX = "lf_member";
	private static String LIST_SOURCE = "member.listAgent";
	private static String BASE_PATH = "/agent/";
	
	@Autowired
	MemberService service;
	
	@RequestMapping(KEY_MAIN)
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		mm.put("member_id", "");
		return BASE_PATH + "agent.html";
	}
	
	@RequestMapping(KEY_MAIN+ "/{id}")
	public String index(@PathVariable String id,ModelMap mm) {
		mm.put("code", CODE);
		mm.put("member_id", id);
		return BASE_PATH + "agent.html";
	}
	
	@RequestMapping("/audit")
	public String auditlist(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "agent_audit.html";
	}
	
	@ResponseBody
	@RequestMapping("/auditOk")
	public AjaxResult auditOk() {
		String ids = getParameter("ids");
		boolean temp = service.auditOK(ids);
		
		if (temp) {
			return success("审核成功!");
		} else {
			return error("审核失败!");
		}
	}
	
	@ResponseBody
	@RequestMapping("/auditRefuse")
	public AjaxResult auditRefuse() {
		String ids = getParameter("ids");
		Paras updateMap = Paras.create().set("status", 4).set("ids", ids.split(","));
		boolean temp = Blade.create(Member.class).updateBy("status = #{status}", "id in (#{join(ids)})", updateMap);
		if (temp) {
			return success("审核拒绝成功!");
		} else {
			return error("审核拒绝失败!");
		}
	}
	
	@RequestMapping(KEY_ADD)
	public String add(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "agent_add.html";
	}

	@RequestMapping(KEY_EDIT + "/{id}")
	public String edit(@PathVariable String id, ModelMap mm) {
		Member member = service.findById(id);
		member.setPassword("");
		member.setPay_password("");
		mm.put("model", JsonKit.toJson(member));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "agent_edit.html";
	}
	
	@RequestMapping("/recharge"+ "/{id}")
	public String recharge(@PathVariable String id,ModelMap mm) {
		Member member = service.findById(id);
		mm.put("model", JsonKit.toJson(member));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "agent_recharge.html";
	}
	
	@RequestMapping("/sub"+ "/{id}")
	public String sub(@PathVariable String id,ModelMap mm) {
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "agent_sub.html";
	}
	
	@ResponseBody
	@RequestMapping("/sublist"+ "/{id}")
	public Object sublist(@PathVariable String id) {
		MyMemberIntercept mi = new MyMemberIntercept();
		Member member = Blade.create(Member.class).findById(id);
		mi.is_agent = "0";
		mi.agentid = member.getAgent_id();
		Object grid = paginate(LIST_SOURCE, mi);
		return grid;
	}
	
	@ResponseBody
	@Before(AgentRechargeValidator.class)
	@RequestMapping("/rechargeSave")
	public AjaxResult rechargeSave() {
		Member member = mapping(PREFIX, Member.class);
		boolean temp = service.addAgentRecharge(member.getId(), member.getWithdraw());
		if (temp) {
			return success("充值成功！");
		}else {
			return error("充值失败！");
		}
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	public String view(@PathVariable String id, ModelMap mm) {
		Member member = service.findById(id);
		
		ShiroUser shiroUser = ShiroKit.getUser();
		User user = Blade.create(User.class).findById(shiroUser.getId());
		if(user.getIs_agent() == 1){
			member.setMobile(StrKit.hidePhone(member.getMobile()));
			member.setBank_mobile(StrKit.hidePhone(member.getBank_mobile()));
		}
		
		mm.put("model", JsonKit.toJson(member));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "agent_view.html";
	}

	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		String member_id = getRequest().getParameter("member_id");
		AgentIntercept agentInter = new AgentIntercept();
		if(!Func.isEmpty(member_id)){
			agentInter.member_id = member_id;
		}
		Object grid = paginate(LIST_SOURCE, agentInter);
		return grid;
	}
	
	@ResponseBody
	@RequestMapping("/auditList")
	public Object auditList() {
		AgentIntercept ai = new AgentIntercept();
		ai.status = "2,4";
		Object grid = paginate(LIST_SOURCE, ai);
		return grid;
	}

	@ResponseBody
	@Before(MemberValidator.class)
	@RequestMapping(KEY_SAVE)
	public AjaxResult save() {
		Member member = mapping(PREFIX, Member.class);
		try{
			
			ShiroUser shiroUser = ShiroKit.getUser();
			User user = Blade.create(User.class).findById(shiroUser.getId());
			
			boolean temp = false;
			if(user.getIs_agent() == 1){ // 添加下级，需审核
				Member pAgent = Blade.create(Member.class).findById(user.getMember_id());
				temp = service.addAgent(member, pAgent);
			}else{ // 平台添加一级代理商
				temp = service.addAgent(member, null);
			}
			if (temp) {
				CacheKit.removeAll(DEPT_CACHE);
				CacheKit.removeAll(DICT_CACHE);
				CacheKit.removeAll(USER_CACHE);
				return success(SAVE_SUCCESS_MSG);
			}else {
			return error(SAVE_FAIL_MSG);
			}
		}catch(Exception e){
			return error(SAVE_FAIL_MSG + ":" + e.getMessage());
		}
		
	}

	@ResponseBody
	@Before(MemberValidator.class)
	@RequestMapping(KEY_UPDATE)
	public AjaxResult update() {
		Member member = mapping(PREFIX, Member.class);
		
		MD5 md5 = new MD5();
		boolean isModifySysUser = false; // 是否要修改系统用户表
		String password = member.getPassword();
		if(StrKit.isNotEmpty(password.trim())){
			member.setPassword(md5.enCodeByMD5(password, member.getMobile()));
			isModifySysUser = true;
		}else{
			member.setPassword(null);
		}
		
		Member m = service.findById(member.getId());
		
		if(!member.getMobile().equals(m.getMobile())
				|| !member.getReal_name().equals(m.getReal_name())){
			isModifySysUser = true;
		}
		
		if(StrKit.isNotEmpty(member.getPay_password().trim())){
			member.setPay_password(md5.enCodeByMD5(member.getPay_password(), member.getMobile()));
		}else{
			member.setPay_password(null);
		}
		
		if(m.getStatus() == 4){ // 如果是拒绝，改成待审核
			member.setStatus(2);
		}
		
		boolean temp = service.update(member);
		if (temp) {
			if(isModifySysUser){
				// 修改系统用户表的密码
				User user = Md.selectUnique("user.findOneMember", Paras.create().set("member_id", member.getId()), User.class);
				if(StrKit.isNotEmpty(member.getPassword())){
					String salt = user.getSalt();
					user.setPassword(ShiroKit.md5(password, salt));
				}
				user.setAccount(member.getMobile());
				user.setName(member.getReal_name());
				user.setVersion(user.getVersion() + 1);
				temp = Blade.create(User.class).update(user);
			}
			
			return success(UPDATE_SUCCESS_MSG);
		} else {
			return error(UPDATE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_REMOVE)
	public AjaxResult remove(@RequestParam String ids) {
		int cnt = service.deleteByIds(ids);
		if (cnt > 0) {
			return success(DEL_SUCCESS_MSG);
		} else {
			return error(DEL_FAIL_MSG);
		}
	}
	
	@ResponseBody
	@RequestMapping("/ban")
	public AjaxResult ban() {
		String ids = getParameter("ids");
		boolean temp = service.agentFrozen(ids);
		if (temp) {
			return success("操作成功!");
		} else {
			return error("操作失败!");
		}
	}
}

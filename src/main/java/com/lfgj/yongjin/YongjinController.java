package com.lfgj.yongjin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rrgy.common.base.BaseController;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.model.User;
import com.lfgj.member.model.Member;
import com.lfgj.tixian.model.Tixianjilu;
import com.lfgj.tixian.service.TixianjiluService;
import com.lfgj.util.LfConstant;
import com.lfgj.yongjin.intercept.YongjinIntercept;
import com.lfgj.yongjin.service.YongjinService;

/**
 * 佣金管理 Generated by Blade. 2017-01-09 15:38:45
 */
@Controller
@RequestMapping("/yongjins")
public class YongjinController extends BaseController {
	private static String CODE = "yongjins";
	private static String LIST_SOURCE = "yongjin.listYongjin";
	private static String BASE_PATH = "/yongjin/";

	@Autowired
	YongjinService service;

	@RequestMapping(KEY_MAIN)
	public String index(ModelMap mm) {
		ShiroUser shiroUser = ShiroKit.getUser();
		User user = Blade.create(User.class).findById(shiroUser.getId());
		if(!LfConstant.agent_role.equals(user.getRoleid())){
			mm.put("member_id", "");
		}else{
			mm.put("member_id", user.getId());
		}
		
		mm.put("code", CODE);
		return BASE_PATH + "yongjin.html";
	}
	
	@RequestMapping("/sub"+ "/{id}")
	public Object sub(@PathVariable String id,ModelMap mm) {
		mm.put("code", CODE);
		Member member = Blade.create(Member.class).findById(id);
		if(member.getIs_agent() == 1){ // 代理商
			mm.put("member_id", member.getAgent_id());
		}else{
			mm.put("member_id", "-1");
		}
		
		return BASE_PATH + "yongjin.html";
	}
	
	@ResponseBody
	@RequestMapping("/sum")
	public AjaxResult querySum() {
		Map<String,Object> params = new HashMap<String,Object>();
		String agent_id = this.getParameter("member_id");
		if(!StrKit.isEmpty(agent_id)){
			params.put("agent_id", agent_id);
			User user = Blade.create(User.class).findById(agent_id);
			if(user != null){
				params.put("member_id", user.getMember_id());
			}
		}
		
		String type = this.getParameter("type");
		String create_time_gt_skip = this.getParameter("create_time_gt_skip");
		String create_time_lt_skip = this.getParameter("create_time_lt_skip");
		String user_id_skip = this.getParameter("user_id_skip");
		String mobile = this.getParameter("mobile");
		
		params.put("type", type);
		params.put("create_time_gt_skip", create_time_gt_skip);
		params.put("create_time_lt_skip", create_time_lt_skip);
		params.put("user_id_skip", user_id_skip);
		params.put("mobile", mobile);
		
		String sum = service.querySum(params);
		
		AjaxResult rst = new AjaxResult();
		rst.setData(sum);
		rst.setCode(0);
		rst.setMessage(type);
		
		return rst;
	}
	
	

	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		YongjinIntercept yi = new YongjinIntercept();
		String agent_id = this.getParameter("member_id");
		if(!StrKit.isEmpty(agent_id)){
			yi.agent_id = agent_id;
			User user = Blade.create(User.class).findById(agent_id);
			if(user != null){
				yi.member_id = user.getMember_id().toString();
			}
		}
		
		Object grid = paginate(LIST_SOURCE, yi);
		return grid;
	}
}

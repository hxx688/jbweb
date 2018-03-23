/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (cbjr@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rrgy.system.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfgj.util.CommKit;
import com.rrgy.common.base.BaseController;
import com.rrgy.core.annotation.Before;
import com.rrgy.core.constant.Const;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.captcha.Captcha;
import com.rrgy.core.toolbox.kit.LogKit;
import com.rrgy.core.toolbox.log.BladeLogManager;
import com.rrgy.system.meta.intercept.LoginValidator;
import com.rrgy.system.model.LoginLog;
import com.rrgy.system.model.Main;
import com.rrgy.system.model.Parameter;
import com.rrgy.system.model.User;

@Controller
public class LoginController extends BaseController implements Const{

	@RequestMapping("/")
	public String index(ModelMap mm) {
		Parameter param = new Parameter();
		param.setCode("102");
		param = Blade.create(Parameter.class).findTopOne(param);
		
		Main main = new Main();
		main.setSite_name(param.getPara());
		mm.put("main", main);
		
		return INDEX_REALPATH;
	}
	
	/**
	 * 登陆地址
	 */
	@GetMapping("/login__main")
	public String login() {
		if (ShiroKit.isAuthenticated()) {
			return redirect("/");
		}
		return LOGIN_REALPATH;
	}
	
	/**
	 * 登陆地址
	 */
	@GetMapping("/login_agent")
	public String login_agent() {
		if (ShiroKit.isAuthenticated()) {
			return redirect("/");
		}
		return LOGIN_AGENT_REALPATH;
	}

	/**
	 * 登陆请求
	 */
	@Before(LoginValidator.class)
	@ResponseBody
	@PostMapping("/loginAgent")
	public AjaxResult loginAgent(HttpServletRequest request, HttpServletResponse response) {
		String account = getParameter("account");
		String password = getParameter("password");
		String imgCode = getParameter("imgCode");
		if (!Captcha.validate(request, response, imgCode)) {
			return error("验证码错误");
		}

		Subject currentUser = ShiroKit.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(account, password.toCharArray());
		token.setRememberMe(true);
		try {
			currentUser.login(token);
			Session session = ShiroKit.getSession();
			LogKit.println("\nsessionID	: {} ", session.getId());
			LogKit.println("sessionHost	: {}", session.getHost());
			LogKit.println("sessionTimeOut	: {}", session.getTimeout());
		} catch (UnknownAccountException e) {
			return error("账号不存在");
		} catch (DisabledAccountException e) {
			return error("账号未启用");
		} catch (IncorrectCredentialsException e) {
			return error("密码错误");
		} catch (RuntimeException e) {
			return error("未知错误,请联系管理员");
		}
		doLog(ShiroKit.getSession(), "登录");
		return success("登录成功");
	}
	
	/**
	 * 登陆请求
	 */
	@Before(LoginValidator.class)
	@ResponseBody
	@PostMapping("/login")
	public AjaxResult login(HttpServletRequest request, HttpServletResponse response) {
		String account = getParameter("account");
		String password = getParameter("password");
		String imgCode = getParameter("imgCode");
		if (!Captcha.validate(request, response, imgCode)) {
			return error("验证码错误");
		}
		
		User u = new User();
		u.setAccount(account);
		User user = Blade.create(User.class).findTopOne(u);
		if(user.getIs_agent()==1){
			return error("帐号不存在");
		}
//		String p = ShiroKit.md5(password, user.getSalt());
		Subject currentUser = ShiroKit.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(account, password.toCharArray());
		token.setRememberMe(true);
		try {
			currentUser.login(token);
			Session session = ShiroKit.getSession();
			LogKit.println("\nsessionID	: {} ", session.getId());
			LogKit.println("sessionHost	: {}", session.getHost());
			LogKit.println("sessionTimeOut	: {}", session.getTimeout());
		} catch (UnknownAccountException e) {
			return error("账号不存在");
		} catch (DisabledAccountException e) {
			return error("账号未启用");
		} catch (IncorrectCredentialsException e) {
			return error("密码错误");
		} catch (RuntimeException e) {
			return error("未知错误,请联系管理员");
		}
		doLog(ShiroKit.getSession(), "登录");
		return success("登录成功");
	}

	@RequestMapping("/logout")
	public String logout() {
		doLog(ShiroKit.getSession(), "登出");
		Subject currentUser = ShiroKit.getSubject();
		currentUser.logout();
		String type = this.getRequest().getParameter("type");
		if("1".equals(type)){
			return redirect("/login_agent");
		}else{
			return redirect("/login__main");
		}
	}

	@RequestMapping("/unauth")
	public String unauth() {
		if (ShiroKit.notAuthenticated()) {
			return redirect("/login");
		}
		return NOPERMISSION_PATH;
	}

	@RequestMapping("/captcha")
	public void captcha(HttpServletResponse response) {
		Captcha.init(response).render();
	}

	/**
	 * 注册地址
	 */
	@GetMapping("/register/{id}")
	public String register(@PathVariable String id, ModelMap mm) {
		mm.put("agent", id);
		return "/wap/lf_register.html";
	}
	
	/**
	 * 注册地址
	 */
	@GetMapping("/download")
	public String download(ModelMap mm) {
		Parameter parameter1 = CommKit.getParameter("109");
		mm.put("android", parameter1.getPara());
		Parameter parameter2 = CommKit.getParameter("110");
		mm.put("ios", parameter2.getPara());
		return "/wap/lf_download.html";
	}

	public void doLog(Session session, String type){
		if(!BladeLogManager.isDoLog()){
			return;
		}
		try{
			LoginLog log = new LoginLog();
			String msg = Func.format("[sessionID]: {} [sessionHost]: {} [sessionHost]: {}", session.getId(), session.getHost(), session.getTimeout());
			log.setLogname(type);
			log.setMethod(msg);
			log.setCreatetime(new Date());
			log.setSucceed("1");
			log.setUserid(Func.toStr(ShiroKit.getUser().getId()));
			Blade.create(LoginLog.class).save(log);
		}catch(Exception ex){
			LogKit.logNothing(ex);
		}
	}
	
}

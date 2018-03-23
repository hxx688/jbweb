package com.rrgy.core.constant;

public interface ConstShiro {
	/**
	 * 超级管理员
	 */
	String ADMINISTRATOR = "administrator";
	/**
	 * 普通管理员
	 */
	String ADMIN = "admin";
	/**
	 * 用户
	 */
	String USER = "user";
	/**
	 * 定义无权操作信息
	 */
	String NO_PERMISSION = "当前用户无权操作!";
	
	/**
	 * 定义session过期信息
	 */
	String NO_USER = "无法获取当前用户,session已过期,请重新登录!";
	
	/**
	 * 定义无请求方法信息
	 */
	String NO_METHOD = "请求方法错误!";	
	
	/**
	 * 定义无权限转向
	 */
	String REDIRECT_UNAUTH = "redirect:/unauth";
}

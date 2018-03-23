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
package com.rrgy.core.base.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.rrgy.core.constant.Const;
import com.rrgy.core.constant.ConstShiro;
import com.rrgy.core.exception.NoPermissionException;
import com.rrgy.core.exception.NoUserException;
import com.rrgy.core.interfaces.IQuery;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.file.BladeFile;
import com.rrgy.core.toolbox.grid.GridManager;
import com.rrgy.core.toolbox.kit.CharsetKit;
import com.rrgy.core.toolbox.kit.LogKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.core.toolbox.kit.URLKit;
import com.rrgy.core.toolbox.log.BladeLogManager;
import com.rrgy.core.toolbox.support.BeanInjector;
import com.rrgy.core.toolbox.support.Convert;
import com.rrgy.core.toolbox.support.WafRequestWrapper;

/**
 * Blade控制器封装类
 */
public class BladeController {

	protected Logger LOGGER = LogManager.getLogger(this.getClass());
	
	/** ============================     requset    =================================================  */

	@Resource
	private HttpServletRequest request;
	
	protected HttpServletRequest getRequest() {
		return new WafRequestWrapper(this.request);
	}
	
	public boolean isAjax() {
		String header = getRequest().getHeader("X-Requested-With");
		boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(header);
		return isAjax;
	}
	
	public boolean isPost() {
		String method = getRequest().getMethod();
		return StrKit.equalsIgnoreCase("POST", method);
	}

	public String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	public String getParameter(String name, String defaultValue) {
		return Convert.toStr(getRequest().getParameter(name), defaultValue);
	}

	public Integer getParameterToInt(String name) {
		return Convert.toInt(getRequest().getParameter(name));
	}

	public Integer getParameterToInt(String name, Integer defaultValue) {
		return Convert.toInt(getRequest().getParameter(name), defaultValue);
	}

	public Long getParameterToLong(String name) {
		return Convert.toLong(getRequest().getParameter(name));
	}

	public Long getParameterToLong(String name, Long defaultValue) {
		return Convert.toLong(getRequest().getParameter(name), defaultValue);
	}

	public Float getParameterToFloat(String name) {
		return Convert.toFloat(getRequest().getParameter(name));
	}

	public Float getParameterToFloat(String name, Float defaultValue) {
		return Convert.toFloat(getRequest().getParameter(name), defaultValue);
	}
	
	public String getParameterToEncode(String para) {
		return URLKit.encode(getRequest().getParameter(para), CharsetKit.UTF_8);
	}

	public String getParameterToDecode(String para) {
		return URLKit.decode(getRequest().getParameter(para), CharsetKit.UTF_8);
	}

	public String getContextPath() {
		return getRequest().getContextPath();
	}

	public String redirect(String url) {
		return StrKit.format("redirect:{}", url);
	}
	
	/** ============================     mapping    =================================================  */
	
	/**
	 * 表单值映射为javabean
	 * 
	 * @param beanClass
	 *            javabean.class
	 * @return T
	 */
	public <T> T mapping(Class<T> beanClass) {
		return (T) BeanInjector.inject(beanClass, getRequest());
	}

	/**
	 * 表单值映射为javabean
	 * 
	 * @param paraPrefix
	 *            name前缀
	 * @param beanClass
	 *            javabean.class
	 * @return T
	 */
	public <T> T mapping(String paraPrefix, Class<T> beanClass) {
		return (T) BeanInjector.inject(beanClass, paraPrefix, getRequest());
	}

	/**
	 * 表单值映射为Maps
	 * 
	 * @return Maps
	 */
	public Paras getParas() {
		return BeanInjector.injectMaps(getRequest());
	}

	/**
	 * 表单值映射为Maps
	 * 
	 * @param paraPrefix  name前缀
	 * @return Maps
	 */
	public Paras getParas(String paraPrefix) {
		return BeanInjector.injectMaps(paraPrefix, getRequest());
	}
	
	/**============================     file    =================================================  */
	
	/**
	 * 获取BladeFile封装类
	 * @param file
	 * @return
	 */
	public BladeFile getFile(MultipartFile file){
		return BladeFile.getFile(file);
	}
	
	/**
	 * 获取BladeFile封装类
	 * @param file
	 * @param dir
	 * @return
	 */
	public BladeFile getFile(MultipartFile file, String dir){
		return BladeFile.getFile(file, dir);
	}
	
	/**
	 * 获取BladeFile封装类
	 * @param file
	 * @param dir
	 * @param path
	 * @param virtualPath
	 * @return
	 */
	public BladeFile getFile(MultipartFile file, String dir, String path, String virtualPath){
		return BladeFile.getFile(file, dir, path, virtualPath);
	}
	
	/**
	 * 获取BladeFile封装类
	 * @param files
	 * @return
	 */
	public List<BladeFile> getFiles(List<MultipartFile> files){
		return BladeFile.getFiles(files);
	}
	
	/**
	 * 获取BladeFile封装类
	 * @param files
	 * @param dir
	 * @return
	 */
	public List<BladeFile> getFiles(List<MultipartFile> files, String dir){
		return BladeFile.getFiles(files, dir);
	}
	
	/**
	 * 获取BladeFile封装类
	 * @param files
	 * @param path
	 * @param virtualPath
	 * @return
	 */
	public List<BladeFile> getFiles(List<MultipartFile> files, String dir, String path, String virtualPath){
		return BladeFile.getFiles(files, dir, path, virtualPath);
	}


	/** ============================     ajax    =================================================  */
	
	/**   
	 * 返回ajaxresult
	 * @param data
	 * @return AjaxResult
	*/
	public AjaxResult json(Object data) {
		return new AjaxResult().success(data);
	}
	
	/**   
	 * 返回ajaxresult
	 * @param data
	 * @param message
	 * @return AjaxResult
	*/
	public AjaxResult json(Object data, String message) {
		return json(data).setMessage(message);
	}
	
	/**   
	 * 返回ajaxresult
	 * @param data
	 * @param message
	 * @param code
	 * @return AjaxResult
	*/
	public AjaxResult json(Object data, String message, int code) {
		return json(data, message).setCode(code);
	}
	
	/**   
	 * 返回ajaxresult
	 * @param message
	 * @return AjaxResult
	*/
	public AjaxResult success(String message) {
		return new AjaxResult().addSuccess(message);
	}
	
	/**   
	 * 返回ajaxresult
	 * @param message
	 * @return AjaxResult
	*/
	public AjaxResult error(String message) {
		return new AjaxResult().addError(message);
	}
	
	/**   
	 * 返回ajaxresult
	 * @param message
	 * @return AjaxResult
	*/
	public AjaxResult warn(String message) {
		return new AjaxResult().addWarn(message);
	}
	
	/**   
	 * 返回ajaxresult
	 * @param message
	 * @return AjaxResult
	*/
	public AjaxResult fail(String message) {
		return new AjaxResult().addFail(message);
	}
	
	
	/** ============================     paginate    =================================================  */
	
	/**   
	 * 分页
	 * @param dbName 数据库别名
	 * @param source 数据源
	 * @param intercept 分页拦截器
	 * @return Object
	*/
	private Object basepage(String dbName, String source, IQuery intercept){
		Integer page = getParameterToInt("page", 1);
		Integer rows = getParameterToInt("rows", 20);
		String where = getParameter("where", StrKit.EMPTY);
		String sidx =  getParameter("sidx", StrKit.EMPTY);
		String sord =  getParameter("sord", StrKit.EMPTY);
		String sort =  getParameter("sort", StrKit.EMPTY);
		String order =  getParameter("order", StrKit.EMPTY);
		if (StrKit.notBlank(sidx)) {
			sort = sidx + " " + sord
					+ (StrKit.notBlank(sort) ? ("," + sort) : StrKit.EMPTY);
		}
		Object grid = GridManager.paginate(dbName, page, rows, source, where, sort, order, intercept, this);
		return grid;
	}
	

	/**   
	 * 分页
	 * @param source 数据源
	 * @return Object
	*/
	protected Object paginate(String source){
		return basepage(null, source, null);
	}
	

	/**   
	 * 分页
	 * @param source 数据源
	 * @param intercept 分页拦截器
	 * @return Object
	*/
	protected Object paginate(String source, IQuery intercept){
		return basepage(null, source, intercept);
	}
	

	/**   
	 * 分页
	 * @param dbName 数据库别名
	 * @param source 数据源
	 * @return Object
	*/
	protected Object paginate(String dbName, String source){
		return basepage(dbName, source, null);
	}
	

	/**   
	 * 分页
	 * @param dbName 数据库别名
	 * @param source 数据源
	 * @param intercept 分页拦截器
	 * @return Object
	*/
	protected Object paginate(String dbName, String source, IQuery intercept){
		return basepage(dbName, source, intercept);
	}
	
	
	/** ============================     exception    =================================================  */

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public Object exceptionHandler(Exception ex, HttpServletResponse response, HttpServletRequest request) throws IOException {
		AjaxResult result = new AjaxResult();
		String url = Const.ERROR_500;
		String msg = ex.getMessage();
		Object resultModel = null;
		try {
			if (ex.getClass() == HttpRequestMethodNotSupportedException.class) {
				url = Const.ERROR_500;// 请求方式不允许抛出的异常,后面可自定义页面
			} else if (ex.getClass() == NoPermissionException.class) {
				url = Const.NOPERMISSION_PATH;// 无权限抛出的异常
				msg = ConstShiro.NO_PERMISSION;
			} else if (ex.getClass() == NoUserException.class) {
				url = Const.LOGIN_REALPATH;// session过期抛出的异常
				msg = ConstShiro.NO_USER;
			}
			if (isAjax() || isPost()) {
				result.addFail(msg);
				resultModel = result;
			} else {
				ModelAndView view = new ModelAndView(url);
				view.addObject("error", msg);
				view.addObject("class", ex.getClass());
				view.addObject("method", request.getRequestURI());
				resultModel = view;
			}
			try {
				if(StrKit.notBlank(msg)){
					BladeLogManager.doLog("异常日志", msg, false);
				}
			} catch (Exception logex) {
				LogKit.logNothing(logex);
			}
			return resultModel;
		} catch (Exception exception) {
//			LOGGER.error(exception.getMessage(), exception);
			System.out.println(exception.getMessage());
			return resultModel;
		} finally {
//			LOGGER.error(msg, ex);
			System.out.println("来源："+request.getRequestURL()+"?"+request.getQueryString());
			System.out.println(msg);
		}
	}

}

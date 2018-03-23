package com.rrgy.platform.controller;

import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rrgy.common.base.BaseController;
import com.rrgy.common.tool.SysCache;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.annotation.DoLog;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.platform.model.Notice;

@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {
	private static String CODE = "notice";
	private static String PREFIX = "TB_YW_TZGG";
	private static String DATA_SOURCE = "notice.data";
	private static String LIST_SOURCE = "notice.list";
	private static String BASE_PATH = "/platform/notice/";
	
	@RequestMapping(KEY_MAIN)
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "notice.html";
	}
	
	
	@RequestMapping(KEY_ADD)
	public String add(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "notice_add.html";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(KEY_EDIT + "/{id}")
	public String edit(@PathVariable String id, ModelMap mm) {
		Map map = Md.selectOne(DATA_SOURCE, Paras.create().set("id", id), Map.class);
		mm.put("model", JsonKit.toJson(map));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "notice_edit.html";
	}
	
	
	@RequestMapping(KEY_VIEW + "/{id}")
	public String view(@PathVariable String id, ModelMap mm) {
		Notice notice = Blade.create(Notice.class).findById(id);
		//将javabean转化为map
		Paras ps = Paras.parse(notice);
		//使用SysCache.getDictName方法从缓存中获取对应字典项的中文值
		ps.set("dic_f_it_lx", SysCache.getDictName(102, notice.getF_it_lx()));
		//将结果传回前台
		mm.put("model", JsonKit.toJson(ps));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "notice_view.html";
	}

	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		Object grid = paginate(LIST_SOURCE);
		return grid;
	}

	@RequestMapping("index_protocol")
	public String indexProtocol(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "notice_protocol.html";
	}

	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/protocol_edit/{id}")
	public String editProtocol(@PathVariable String id, ModelMap mm) {
		Map map = Md.selectOne(DATA_SOURCE, Paras.create().set("id", id), Map.class);
		mm.put("model", JsonKit.toJson(map));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "notice_protocol_edit.html";
	}

	@ResponseBody
	@RequestMapping("list_protocol")
	public Object listProtocol() {
		Object grid = paginate("notice.list_protocol");
		return grid;
	}
	
	@ResponseBody
	@RequestMapping(KEY_SAVE)
	@DoLog(name = "[公告管理]新增")
	public AjaxResult save() {
		Notice notice = mapping(PREFIX, Notice.class);
		notice.setF_dt_cjsj(new Date());
		ShiroUser user = ShiroKit.getUser();
		if (null == user) {
			return error(SAVE_FAIL_MSG);
		}
		notice.setF_it_cjr(new Integer(user.getId().toString()));
		boolean temp = Blade.create(Notice.class).save(notice);
		if (temp) {
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_UPDATE)
	@DoLog(name = "[公告管理]修改")
	public AjaxResult update() {
		Notice notice = mapping(PREFIX, Notice.class);
		//1.使用mapper
		//NoticeMapper mapper = Md.getMapper(NoticeMapper.class);
		//boolean temp = mapper.updateTemplateById(notice) > 0;
		//2.使用sql模板
		//boolean temp = Md.update("notice.update", notice) > 0;
		//3.使用自动生成api
		boolean temp = Blade.create(Notice.class).update(notice);
		if (temp) {
			return success(UPDATE_SUCCESS_MSG);
		} else {
			return error(UPDATE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_REMOVE)
	public AjaxResult remove() {
		int cnt = Blade.create(Notice.class).deleteByIds(getParameter("ids"));
		if (cnt > 0) {
			return success(DEL_SUCCESS_MSG);
		} else {
			return error(DEL_FAIL_MSG);
		}
	}

}

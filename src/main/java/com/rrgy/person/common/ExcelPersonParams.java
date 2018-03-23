package com.rrgy.person.common;

import javax.servlet.http.HttpServletRequest;

import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.core.toolbox.kit.URLKit;

public class ExcelPersonParams {

	/**
	 * 获取导出查询条件并拼接SQL语句
	 * @param request
	 * @param sql
	 * @param para
	 * @return
	 */
	public static String getParamsAndSql(HttpServletRequest request, String sql, Paras para) {
		
		String id_equal = request.getParameter("id_equal");
		String nick_name = request.getParameter("nick_name");
		String mobile = request.getParameter("mobile");
		String status = request.getParameter("status");
		String reg_time_dategt = request.getParameter("reg_time_dategt");
		String reg_time_datelt = request.getParameter("reg_time_datelt");
		
		if (!StrKit.isBlank(id_equal)) {
			sql += " and id = #{id_equal}";
			para.set("id_equal", id_equal);
		}
		if (!StrKit.isBlank(nick_name)) {
			nick_name = URLKit.decode(URLKit.decode(nick_name, "UTF-8"),"UTF-8");
			sql += " and nick_name like #{nick_name}";
			para.set("nick_name", "%" + nick_name + "%");
		}
		if (!StrKit.isBlank(mobile)) {
			sql += " and mobile like #{mobile}";
			para.set("mobile", "%" + mobile + "%");
		}
		if (!StrKit.isBlank(status)) {
			sql += " and status = #{status}";
			para.set("status", status);
		}
		if (!StrKit.isBlank(reg_time_dategt)) {
			reg_time_dategt = URLKit.decode(URLKit.decode(reg_time_dategt, "UTF-8"),"UTF-8");
			sql += " and reg_time > #{reg_time_dategt}";
			para.set("reg_time_dategt", reg_time_dategt);
		}
		if (!StrKit.isBlank(reg_time_datelt)) {
			reg_time_datelt = URLKit.decode(URLKit.decode(reg_time_datelt, "UTF-8"),"UTF-8");
			sql += " and reg_time < #{reg_time_datelt}";
			para.set("reg_time_datelt", reg_time_datelt);
		}
		sql += " order by reg_time desc";
		return sql;
	}

}

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
package com.rrgy.core.toolbox.grid;

import java.util.HashMap;
import java.util.Map;

import com.rrgy.core.aop.AopContext;
import com.rrgy.core.base.controller.BladeController;
import com.rrgy.core.constant.Const;
import com.rrgy.core.interfaces.IGrid;
import com.rrgy.core.interfaces.IQuery;
import com.rrgy.core.plugins.dao.Db;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.core.toolbox.support.SqlKeyword;

/**
 * grid工厂基类,封装通用分页方法
 */
public abstract class BaseGridFactory implements IGrid{

	/**
	 * 封装grid返回数据类型
	 * 
	 * @param dbName
	 *            数据库别名
	 * @param page
	 *            当前页号
	 * @param rows
	 *            每页的数量
	 * @param sql
	 *            数据源
	 * @param para
	 *            额外条件 {"id":1,"title":"test"}
	 * @param sort
	 *            排序列名 (id)
	 * @param order
	 *            排序方式 (desc)
	 * @param intercept
	 *            业务拦截器
	 * @param ctrl
	 *            控制器
	 * @return Object
	 */
	protected Object basePaginate(String dbName, Integer page, Integer rows, String source, String para, String sort, String order, IQuery intercept, BladeController ctrl) {
		if (source.toLowerCase().indexOf("select") == -1) {
			return paginateById(dbName, page, rows, source, para, sort, order, intercept, ctrl);
		} else {
			return paginateBySql(dbName, page, rows, source, para, sort, order, intercept, ctrl);
		}
	}
	
	private Object paginateById(String dbName, Integer page, Integer rows, String sqlId, String para, String sort, String order, IQuery intercept, BladeController ctrl) {	
		String sqlTemplate = Md.getSql(sqlId);
		return paginateBySql(dbName, page, rows, sqlTemplate, para, sort, order, intercept, ctrl);
	}

	private Object paginateBySql(String dbName, Integer page, Integer rows, String sqlTemplate, String para, String sort, String order, IQuery intercept, BladeController ctrl) {
		String statement = "select * from (" + sqlTemplate + ") blade_statement";
		String sqlex = SqlKeyword.getWhere(para,"where");
		int ix = sqlTemplate.toLowerCase().indexOf("0 from");
		if(ix != -1){
			statement = sqlTemplate;
			String lastSql = sqlTemplate.toLowerCase().substring(ix);
			if(lastSql.toLowerCase().indexOf("where")!=-1){
				sqlex = SqlKeyword.getWhere(para,"and");
			}
		}
		
		Map<String, Object> map = getSqlMap(para, sort, order);	
		String orderBy = (Func.isEmpty(map.get(Const.ORDER_BY_STR))) ? " " : (" order by " + Func.toStr(map.get(Const.ORDER_BY_STR)));
		String sqlCount = "";
		
		// 查询前拦截
		AopContext ac = null;
		if (null != intercept) {
			ac = new AopContext(ctrl);
			ac.setSql(sqlTemplate);
			ac.setSqlEx(sqlex);
			ac.setCondition("");
			ac.setOrderBy(orderBy);
			ac.setSqlStatement("");
			ac.setSqlCount("");
			ac.setParam(map);
			intercept.queryBefore(ac);
			sqlCount = ac.getSqlCount();
			statement = (StrKit.isBlank(ac.getSqlStatement()) ? (statement + (StrKit.isBlank(ac.getWhere()) ? (sqlex + ac.getCondition()) : ac.getWhere()) + orderBy) : ac.getSqlStatement());
		} else {
			statement = statement + sqlex + orderBy;
		}

		Object list = null;
		
		if(StrKit.notBlank(dbName)){
			list = Db.init(dbName).paginate(statement, sqlCount, map, page, rows);			
		} else {
			list = Db.paginate(statement, sqlCount, map, page, rows);
		}

		// 查询后拦截
		if (null != intercept) {
			ac.setObject(list);
			intercept.queryAfter(ac);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> getSqlMap(String para, String sort, String order){
		Map<String, Object> map = JsonKit.parse(Func.isEmpty(Func.decodeUrl(para)) ? null : Func.decodeUrl(para), HashMap.class);
		if (Func.isEmpty(map)) {
			map = new HashMap<>();
		}
		
		Map<String, Object> nmap = new HashMap<>();
		for(String key:map.keySet()){
			Object val = map.get(key);
			if(key.indexOf(".")!=-1){
				String nkey = SqlKeyword.clearASname(key);
				nmap.put(nkey, val);
			}else{
				nmap.put(key, val);
			}
		}
		map.clear();
		map.putAll(nmap);
		
		map.put(Const.ORDER_BY_STR, Func.isAllEmpty(sort, order) ? "" : (sort + " " + order));
		return map;
	}
}

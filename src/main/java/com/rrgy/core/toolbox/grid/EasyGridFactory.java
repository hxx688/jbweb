package com.rrgy.core.toolbox.grid;

import java.util.List;
import java.util.Map;

import com.rrgy.core.base.controller.BladeController;
import com.rrgy.core.interfaces.IQuery;

@SuppressWarnings("unchecked")
public class EasyGridFactory extends BaseGridFactory{

	public EasyGrid<Map<String, Object>> paginate(String dbName, Integer page, Integer rows,
			String source, String para, String sort, String order,
			IQuery intercept, BladeController ctrl) {
		
		BladePage<Map<String, Object>> list = (BladePage<Map<String, Object>>) super.basePaginate(dbName, page, rows, source, para, sort, order, intercept, ctrl);
		
		List<Map<String, Object>> _rows = list.getRows();
		long _total = list.getTotal();
		int _nowPage = (int) list.getPage();
		int _pageSize = (int) list.getPageSize();
		
		EasyGrid<Map<String, Object>> grid = new EasyGrid<>(_total, _rows, _nowPage, _pageSize);
		return grid;
	}

}
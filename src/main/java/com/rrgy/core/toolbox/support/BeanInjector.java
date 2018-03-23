package com.rrgy.core.toolbox.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.rrgy.core.constant.Const;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.BeanKit;
import com.rrgy.core.toolbox.kit.CollectionKit;
import com.rrgy.core.toolbox.kit.StrKit;

/**
 * javabean 、 paras映射
 */
public class BeanInjector {

	public static final <T> T inject(Class<T> beanClass, HttpServletRequest request) {
		try {
			return BeanKit.mapToBeanIgnoreCase(getParameterMap(request), beanClass);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static final <T> T inject(Class<T> beanClass, String paraPrefix, HttpServletRequest request) {
		try {
			Map<String, Object> map = injectPara(paraPrefix, request);
			return BeanKit.mapToBeanIgnoreCase(map, beanClass);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static final Paras injectMaps(HttpServletRequest request) {
		return Paras.parse(getParameterMap(request));
	}

	public static final Paras injectMaps(String paraPrefix, HttpServletRequest request) {
		Map<String, Object> map = injectPara(paraPrefix, request);
		return Paras.parse(map);
	}

	private static final Map<String, Object> injectPara(String paraPrefix, HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		Map<String, Object> map = new HashMap<>();
		String start = paraPrefix.toLowerCase() + ".";
		String[] value = null;
		for (Entry<String, String[]> param : paramMap.entrySet()) {
			if (!param.getKey().toLowerCase().startsWith(start)) {
				continue;
			}
			value = param.getValue();
			Object o = null;
			if (CollectionKit.isNotEmpty(value)) {
				if (value.length > 1) {
					o = CollectionKit.join(value, ",");
				} else {
					o = value[0];					
				}
			}
			map.put(StrKit.removePrefixIgnoreCase(param.getKey(), start).toLowerCase(), o);
		}
		String versionL = request.getParameter(Const.OPTIMISTIC_LOCK.toLowerCase());
		String versionU = request.getParameter(Const.OPTIMISTIC_LOCK);
	    if (StrKit.notBlank(versionL)){
			map.put(Const.OPTIMISTIC_LOCK.toLowerCase(), Func.toInt(versionL) + 1);
		} else if(StrKit.notBlank(versionU)){
			map.put(Const.OPTIMISTIC_LOCK.toLowerCase(), Func.toInt(versionU) + 1);
		}
		return map;
	}
	
	private static final Map<String, Object> getParameterMap(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		Map<String, Object> map = new HashMap<>();
		String[] value = null;
		for (Entry<String, String[]> param : paramMap.entrySet()) {
			value = param.getValue();
			Object o = null;
			if (CollectionKit.isNotEmpty(value)) {
				if (value.length > 1) {
					o = CollectionKit.join(value, ",");
				} else {
					o = value[0];				
				}
			}
			map.put(param.getKey(), o);
		}
		return map;
	}
	
}

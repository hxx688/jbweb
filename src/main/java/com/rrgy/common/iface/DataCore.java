package com.rrgy.common.iface;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataCore {
	private static Map<String, Class> modules = new LinkedHashMap<String, Class>();
	
	public static void loadModules(String flag,Class clz){
		modules.put(flag, clz);
	}
	
	public static Class findMdoules(String flag){
		return modules.get(flag);
	}
	
	public static Map<String, Class> findAllModules(){
		return modules;
	}
}

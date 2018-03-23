package com.rrgy.core.constant;

import java.util.Map;

import com.rrgy.core.listener.ConfigListener;

public interface ConstConfig {

	Map<String, String> pool = ConfigListener.getConf();
	
	String DBTYPE = pool.get("master.dbType");
	String DRIVER = pool.get("master.driver");
	String URL = pool.get("master.url");
	String USERNAME = pool.get("master.username");
	String PASSWORD = pool.get("master.password");
	String INITIALSIZE = pool.get("druid.initialSize");
	String MAXACTIVE = pool.get("druid.maxActive");
	String MINIDLE = pool.get("druid.minIdle");
	String MAXWAIT = pool.get("druid.maxWait");

	String DOMAIN = pool.get("config.domain");
	String REMOTE_MODE = pool.get("config.remoteMode");
	String REMOTE_PATH = pool.get("config.remotePath");
	String UPLOAD_PATH = pool.get("config.uploadPath");
	String DOWNLOAD_PATH = pool.get("config.downloadPath");
	
	String APPCODE = pool.get("AppCode");
	
}

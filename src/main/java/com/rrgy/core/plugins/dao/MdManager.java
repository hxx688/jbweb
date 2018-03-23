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
package com.rrgy.core.plugins.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLResult;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.PageQuery;

import com.rrgy.core.beetl.BeetlTemplate;
import com.rrgy.core.interfaces.ILoader;
import com.rrgy.core.plugins.connection.ConnectionPlugin;
import com.rrgy.core.toolbox.grid.BladePage;
import com.rrgy.core.toolbox.kit.CacheKit;
import com.rrgy.core.toolbox.kit.StrKit;

public class MdManager {
	private static Map<String, MdManager> pool = new ConcurrentHashMap<String, MdManager>();
	
	private volatile SQLManager sql = null;
	
	public static MdManager init() {
		return init(ConnectionPlugin.init().MASTER);
	}

	public static MdManager init(String name) {
		MdManager db = pool.get(name);
		if (null == db) {
			synchronized (MdManager.class) {
				db = pool.get(name);
				if (null == db) {
					db = new MdManager(name);
					pool.put(name, db);
				}
			}
		}
		return db;
	}
	
	private MdManager(String dbName) {
		this.sql = ConnectionPlugin.init().getPool().get(dbName);
	}

	private MdManager() {}
	
	private SQLManager getSqlManager() {
		if (null == sql) {
			synchronized (MdManager.class) {
				sql = ConnectionPlugin.init().getPool().get(ConnectionPlugin.init().MASTER);
			}
		}
		return sql;
	}
	
	
	/************   ↓↓↓   ********    mapper    *********   ↓↓↓   **************/
	
	/**
	 * 获取Mapper
	 * @param mapperInterface mapper接口类
	 * @return
	 */
	public <T> T getMapper(Class<T> mapperInterface){
		return getSqlManager().getMapper(mapperInterface);
	}
	
	
	/************   ↓↓↓   ********     通用     *********   ↓↓↓   ****************/
	
	/**
	 * 获取一条数据
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @param clazz 返回类型
	 * @return
	 */
	public <T> T selectOne(String sqlId, Object paras, Class<T> clazz){
		return getSqlManager().selectSingle(sqlId, paras, clazz);
	}
	
	/**
	 * 获取一条数据
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @param clazz 返回类型
	 * @return
	 */
	public <T> T selectUnique(String sqlId, Object paras, Class<T> clazz){
		return getSqlManager().selectUnique(sqlId, paras, clazz);
	}
	
	/**
	 * 获取多条数据
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @param clazz 返回类型
	 * @return
	 */
	public <T> List<T> selectList(String sqlId, Object paras, Class<T> clazz){
		return getSqlManager().select(sqlId, clazz, paras);
	}
	
	/**
	 * 获取str
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @return
	 */
	public String queryStr(String sqlId, Object paras){
		return selectOne(sqlId, paras, String.class);
	}

	/**
	 * 获取integer
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @return
	 */
	public Integer queryInt(String sqlId, Object paras){
		return selectOne(sqlId, paras, Integer.class);
	}
	
	/**
	 * 获取long
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @return
	 */
	public Long queryLong(String sqlId, Object paras){
		return selectOne(sqlId, paras, Long.class);
	}
	
	/**
	 * 获取decimal
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @return
	 */
	public BigDecimal queryDecimal(String sqlId, Object paras){
		return selectOne(sqlId, paras, BigDecimal.class);
	}
	
	/**
	 * 获取一条数据
	 * @param cacheName 缓存名
	 * @param key   缓存key
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @param clazz 返回类型
	 * @return
	 */
	public <T> T selectOneByCache(String cacheName, String key, String sqlId, Object paras, Class<T> clazz){
		final String _sqlId = sqlId;
		final Object _paras = paras;
		final Class<T> _clazz = clazz;
		return CacheKit.get(cacheName, key, new ILoader() {
			@Override
			public Object load() {
				return getSqlManager().selectSingle(_sqlId, _paras, _clazz);
			}
		});
	}
	
	/**
	 * 获取一条数据
	 * @param cacheName 缓存名
	 * @param key   缓存key
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @param clazz 返回类型
	 * @return
	 */
	public <T> T selectUniqueByCache(String cacheName, String key, String sqlId, Object paras, Class<T> clazz){
		final String _sqlId = sqlId;
		final Object _paras = paras;
		final Class<T> _clazz = clazz;
		return CacheKit.get(cacheName, key, new ILoader() {
			@Override
			public Object load() {
				return getSqlManager().selectUnique(_sqlId, _paras, _clazz);
			}
		});
	}
	
	/**
	 * 获取多条数据
	 * @param cacheName 缓存名
	 * @param key   缓存key
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @param clazz 返回类型
	 * @return
	 */
	public <T> List<T> selectListByCache(String cacheName, String key, String sqlId, Object paras, Class<T> clazz){
		final String _sqlId = sqlId;
		final Object _paras = paras;
		final Class<T> _clazz = clazz;
		return CacheKit.get(cacheName, key, new ILoader() {
			@Override
			public Object load() {
				return getSqlManager().select(_sqlId, _clazz, _paras);
			}
		});
	}
	
	/**
	 * 分页
	 * @param sqlId sqlId
	 * @param clazz	返回类型
	 * @param paras	参数
	 * @param pageNum	页号
	 * @param pageSize	数量
	 * @return
	 */
	public <T> BladePage<T> paginate(String sqlId, Class<T> clazz, Object paras, int pageNum, int pageSize){
		return paginate(sqlId, clazz, paras, pageNum, pageSize, null);
	}
	
	
	/**
	 * 分页
	 * @param sqlId sqlId
	 * @param clazz	返回类型
	 * @param paras	参数
	 * @param pageNum	页号
	 * @param pageSize	数量
	 * @param orderBy	排序
	 * @return
	 */
	public <T> BladePage<T> paginate(String sqlId, Class<T> clazz, Object paras, int pageNum, int pageSize, String orderBy){
		PageQuery<T> query = new PageQuery<T>();
		query.setPageNumber(pageNum);
		query.setPageSize(pageSize);
		query.setParas(paras);
		if(StrKit.notBlank(orderBy)){
			query.setOrderBy(orderBy);
		}
		getSqlManager().pageQuery(sqlId, clazz, query);
		BladePage<T> page = new BladePage<>(query.getList(), pageNum, pageSize, query.getTotalRow());
		return page;
	}
	
	/**
	 * 新增
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @return
	 */
	public int insert(String sqlId, Object paras){
		return getSqlManager().insert(sqlId, paras, null, null);
	}
	
	/**
	 * 新增
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @param keyName 主键名
	 * @return
	 */
	public KeyHolder insert(String sqlId, Object paras, String keyName) {
		if (StrKit.isBlank(keyName))
			return null;
		KeyHolder holder = new KeyHolder();
		getSqlManager().insert(sqlId, paras, holder, keyName);
		return (getSqlManager().insert(sqlId, paras, holder, keyName) > 0) ? holder : null;
	}
	
	/**
	 * 新增
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @param holder 主键holder
	 * @param keyName 主键名
	 * @return
	 */
	public int insert(String sqlId, Object paras, KeyHolder holder, String keyName) {
		return getSqlManager().insert(sqlId, paras, holder, keyName);
	}
	
	/**修改
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @return
	 */
	public int update(String sqlId, Object paras){
		return getSqlManager().update(sqlId, paras);
	}
	
	/**批量修改
	 * @param sqlId sqlId
	 * @param list 参数
	 * @return
	 */
	public int[] updateBatch(String sqlId, List<?> list){
		return getSqlManager().updateBatch(sqlId, list);
	}
	
	/**删除
	 * @param sqlId sqlId
	 * @param paras 参数
	 * @return
	 */
	public int delete(String sqlId, Object paras){
		return getSqlManager().update(sqlId, paras);
	}
	
	/**
	 * 根据sqlId获取beetlsql的sql语句
	 * @param sqlId
	 * @return
	 */
	public String getSql(String sqlId) {
		return getSqlManager().getScript(sqlId).getSql();
	}
	
	/**
	 * 根据sqlId获取beetlsql的sql语句
	 * @param sqlId
	 * @param paras
	 * @return
	 */
	public String getSql(String sqlId, Map<String, Object> paras) {
		return BeetlTemplate.build(getSql(sqlId), paras);
	}
	
	/**
	 * 根据sqlId获取beetlsql的sql语句
	 * @param sqlId
	 * @param paras
	 * @return
	 */
	public SQLResult getSQLResult(String sqlId, Object paras){
		return getSqlManager().getSQLResult(sqlId, paras);
	}
}

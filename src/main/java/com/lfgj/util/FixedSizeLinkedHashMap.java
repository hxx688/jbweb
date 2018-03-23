package com.lfgj.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class FixedSizeLinkedHashMap<K,V> extends LinkedHashMap<K,V> {
	private static final long serialVersionUID = 6918023506928428613L;
	private int MAX_ENTRIES = 10;

	/**
	 * 获得允许存放的最大容量
	 * 
	 * @return int
	 */
	public int getMAX_ENTRIES() {
		return MAX_ENTRIES;
	}

	/**
	 * 设置允许存放的最大容量
	 * 
	 * @param int
	 *            max_entries
	 */
	public void setMAX_ENTRIES(int max_entries) {
		MAX_ENTRIES = max_entries;
	}

	/**
	 * 如果Map的尺寸大于设定的最大长度，返回true，再新加入对象时删除最老的对象
	 * 
	 * @param Map.Entry
	 *            eldest
	 * @return int
	 */
	protected boolean removeEldestEntry(Map.Entry eldest) {
		return size() > MAX_ENTRIES;
	}
}

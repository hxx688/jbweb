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
package com.rrgy.system.meta.intercept;

import java.util.List;
import java.util.Map;

import com.rrgy.common.tool.SysCache;
import com.rrgy.core.aop.AopContext;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.meta.MetaIntercept;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.grid.BladePage;

public class AttachIntercept extends MetaIntercept {
	/**
	 * 查询后操作
	 * 
	 * @param ac
	 */
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			map.put("ATTACHURL", ConstConfig.DOMAIN + map.get("URL"));
			map.put("STATUSNAME", SysCache.getDictName(902, map.get("STATUS")));
			map.put("CREATERNAME", SysCache.getUserName(map.get("CREATER")));
		}
	}

	/**
	 * 查看转向前操作
	 * 
	 * @param ac
	 */
	public void renderViewBefore(AopContext ac) {
		Paras ps = (Paras) ac.getObject();
		ps.set("attachUrl", ConstConfig.DOMAIN + ps.get("url"))
		.set("statusName", SysCache.getDictName(902, ps.get("status")))
		.set("createrName", SysCache.getUserName(ps.get("creater")));
	}
	
	/**
	 * 物理删除前操作(事务内)
	 * 
	 * @param ac
	 */
	public void removeBefore(AopContext ac) {
		/*Map<String, Object> file = Db.findById("TFW_ATTACH", ac.getId().toString());
		if (Func.isEmpty(file)) {
			throw new RuntimeException("文件不存在!");
		} else {
			String url = file.get("URL").toString();
			File f = new File(Cst.me().getUploadRealPath() + url);
			if(null == f || !f.isFile()){
				throw new RuntimeException("文件不存在!");
			}
			f.delete();
		}*/
	}
	
}
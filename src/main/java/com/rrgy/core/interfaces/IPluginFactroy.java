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
package com.rrgy.core.interfaces;

import java.util.List;

/**
 *  IPluginFactroy插件管理接口
 */
public interface IPluginFactroy {
	
	/**   
	 * 插件注册
	 * @param plugin void
	*/
	void register(IPlugin plugin);

	/**   
	 * 获取插件
	*/
	List<IPlugin> getPlugins();
	
}

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
package com.rrgy.core.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import com.rrgy.core.plugins.PluginManager;

/**
 * 关闭监听器
 */
@Component
public class StopListener implements ApplicationListener<ContextStoppedEvent> {

	@Override
	public void onApplicationEvent(ContextStoppedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			destroyPlugin();
		}
	}

	/**
	 * 插件的停用
	 */
	private void destroyPlugin() {
		PluginManager.init().stop();
	}

}
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
package com.rrgy.common.plugins;

import com.lfgj.common.socket.SocketThread;
import com.rrgy.core.interfaces.IPlugin;

public class GlobalPlugin implements IPlugin {
	private SocketThread socketThread;

	public void start() {
		System.out.println("\n插件启动测试");
//		if (null == socketThread) {
//			// 新建线程类
//			socketThread = new SocketThread(null);
//			// 启动线程
//			socketThread.start();
//		}
	}

	public void stop() {
		System.out.println("\n插件关闭测试");
//		if (null != socketThread && !socketThread.isInterrupted()) {
//			socketThread.closeSocketServer();
//			socketThread.interrupt();
//		}
	}

}

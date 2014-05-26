/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.cepcore.test.context;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RemoteExecuteThread implements Runnable {
	RemoteExecute remoteExecute;

	public RemoteExecuteThread() throws Exception {
		remoteExecute = new RemoteExecuteImpl();
		// 本地主机上的远程对象注册表Registry的实例，并指定端口为8888，这一步必不可少（Java默认端口是1099），必不可缺的一步，缺少注册表创建，则无法绑定对象到远程注册表上
		LocateRegistry.createRegistry(8888);

		// 把远程对象注册到RMI注册服务器上，并命名为RHello
		// 绑定的URL标准格式为：rmi://host:port/name(其中协议名可以省略，下面两种写法都是正确的）
		Naming.bind("rmi://localhost:8888/RemoteExecute", remoteExecute);
		System.out.println(">>>>>INFO:远程remoteExecute对象绑定成功！");

	}

	public void run() {
		try {
			while (true) {
				Thread.sleep(1);
			}
			// 创建一个远程对象
		} catch (Exception e) {
			System.out.println("创建远程对象发生异常！");
		}
	}

}

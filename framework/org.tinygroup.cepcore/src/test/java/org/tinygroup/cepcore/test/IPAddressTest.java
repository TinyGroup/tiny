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
package org.tinygroup.cepcore.test;

import java.net.*;

public class IPAddressTest {
	public static void main(String[] args) {
		try {
			// 获得本机的InetAddress信息
			InetAddress[] ips = InetAddress
					.getAllByName("luog-PC.hs.handsome.com.cn");
			for (InetAddress ip : ips) {
				showInfo(ip);
			}
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		}
	}

	// 将InetAddress 中的信息显示出来
	public static void showInfo(InetAddress ip) {
		if (ip.isAnyLocalAddress()) {
			return;
		}
		System.out.println(ip.isAnyLocalAddress());
		System.out.println(ip.isLinkLocalAddress());
		System.out.println(ip.isLoopbackAddress());
		System.out.println(ip.isMCGlobal());
		System.out.println(ip.isMCLinkLocal());
		System.out.println(ip.isMCNodeLocal());
		System.out.println(ip.isMCOrgLocal());
		System.out.println(ip.isMCSiteLocal());
		System.out.println(ip.isSiteLocalAddress());
		String name = ip.getHostName();
		String address = ip.getHostAddress();
		System.out.println(name);
		System.out.println(address);
		System.out.println("------------------------------");
	}
}

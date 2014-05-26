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
package org.tinygroup.net.applicationprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.config.Configuration;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.net.Server;
import org.tinygroup.net.daemon.DaemonRunnable;
import org.tinygroup.net.daemon.DaemonUtils;
import org.tinygroup.xmlparser.node.XmlNode;

public class NetProcessor implements Configuration, ApplicationProcessor {
	private Logger logger = LoggerFactory.getLogger(NetProcessor.class);
	private static final String NET_NODE_PATH = "/application/net-config";
	private XmlNode xmlConfig;
	private List<ClientConfig> clientConfigs = new ArrayList<ClientConfig>();
	private List<ServerConfig> serverConfigs = new ArrayList<ServerConfig>();
	private Map<String, DaemonRunnable> clientMaps = new HashMap<String, DaemonRunnable>();
	private Map<String, Server> serverMaps = new HashMap<String, Server>();

	public String getApplicationNodePath() {
		return NET_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		xmlConfig = applicationConfig;
		initXML();
	}

	public XmlNode getComponentConfig() {
		return null;
	}

	public XmlNode getApplicationConfig() {
		return xmlConfig;
	}

	public void setApplication(Application application) {
	}

	protected void initXML() {
		if(xmlConfig==null)
			return;
		initServerConfigs();
		initClintConfigs();

	}

	protected void initServerConfigs() {
		List<XmlNode> serverNodes = xmlConfig.getSubNodes("server");
		if (serverNodes == null || serverNodes.size() == 0)
			return;
		for (XmlNode serverNode : serverNodes) {
			ServerConfig serverConfig = new ServerConfig();
			serverConfig.setPort(Integer.parseInt(serverNode
					.getAttribute("port")));
			serverConfig.setMode(serverNode.getAttribute("mode"));
			serverConfig.setType(serverNode.getAttribute("type"));
			serverConfig.setName(serverNode.getAttribute("name"));
			serverConfig.setConfig(serverNode);
			serverConfigs.add(serverConfig);
		}
	}

	protected void initClintConfigs() {
		List<XmlNode> clientNodes = xmlConfig.getSubNodes("client");
		if (clientNodes == null || clientNodes.size() == 0)
			return;
		for (XmlNode clientNode : clientNodes) {
			ClientConfig clientConfig = new ClientConfig();
			clientConfig.setIp(clientNode.getAttribute("ip"));
			clientConfig.setPort(Integer.parseInt(clientNode
					.getAttribute("port")));
			clientConfig.setMode(clientNode.getAttribute("mode"));
			clientConfig.setName(clientNode.getAttribute("name"));
			clientConfig.setType(clientNode.getAttribute("type"));
			clientConfig.setConfig(clientNode);
			clientConfigs.add(clientConfig);
		}
	}

	public void start() {
		startNet();
	}

	private void startNet() {
		startServers();
		startClients();
	}

	protected void startServers() {
		for (ServerConfig serverConfig : serverConfigs) {
			try {
				Class<?> clazz = Class.forName(serverConfig.getType());
				Server server = (Server) clazz.getConstructor(int.class)
						.newInstance(serverConfig.getPort());
				serverMaps.put(serverConfig.getName(), server);
				server.run();
			} catch (Exception e) {
				logger.errorMessage("执行默认服务端启动程序时出错:构造服务端对象实例失败,服务端类型:{0}", e,
						serverConfig.getType());
			}
		}
	}

	protected void startClients() {
		for (ClientConfig clientConfig : clientConfigs) {
			try {
				Class<?> clazz = Class.forName(clientConfig.getType());
				DaemonRunnable clientThread = (DaemonRunnable) clazz
						.getConstructor(String.class, int.class).newInstance(
								clientConfig.getIp(), clientConfig.getPort());
				clientMaps.put(clientConfig.getName(), clientThread);
				DaemonUtils.daemon(clientConfig.getName(), clientThread);
			} catch (Exception e) {
				logger.errorMessage("执行默认客户端启动程序时出错:构造客户端对象实例失败,客户端类型:{0}", e,
						clientConfig.getType());
			}
		}
	}

	public void stop() {
		stopNet();
	}
	
	private void stopNet() {
		stopServers();
		stopClients();
	}

	protected void stopServers() {
		for (String name : serverMaps.keySet()) {
			try {
				serverMaps.get(name).stop();
			} catch (Exception e) {
				logger.errorMessage("执行默认服务端停止程序时出错,服务端名:{0}", e, name);
			}
		}
	}

	protected void stopClients() {
		for (String name : clientMaps.keySet()) {
			try {
				clientMaps.get(name).stop();
			} catch (Exception e) {
				logger.errorMessage("执行默客户端停止程序时出错,客户端名:{0}", e, name);
			}
		}
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}
}

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
package org.tinygroup.netplugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.net.Server;
import org.tinygroup.net.daemon.DaemonRunnable;
import org.tinygroup.net.daemon.DaemonUtils;
import org.tinygroup.netplugin.config.ClientConfig;
import org.tinygroup.netplugin.config.ServerConfig;
import org.tinygroup.plugin.Plugin;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 
 * 功能说明:net插件 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-18 <br>
 * <br>
 */
public class NetPlugin extends AbstractConfiguration implements Plugin {
	private static final String NET_CONFIG_PATH="net-config";
	private List<ClientConfig> clientConfigs = new ArrayList<ClientConfig>();
	private List<ServerConfig> serverConfigs = new ArrayList<ServerConfig>();
	private Map<String, DaemonRunnable> clientMaps = new HashMap<String, DaemonRunnable>();
	private Map<String,Server> serverMaps = new HashMap<String,Server>();
	
	public DaemonRunnable getClientThread(String name){
		return clientMaps.get(name);
	}
	public Server getServer(String name){
		return serverMaps.get(name);
	}

	public String getApplicationNodePath() {
		return NET_CONFIG_PATH;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void start() {
		initXML();
		startNet();
	}

	public void stop() {
		stopServers();
		stopClients();
	}
	
	private void initXML() {
		initServerConfigs();
		initClintConfigs();
		
	}
	
	private void initServerConfigs(){
		List<XmlNode> serverNodes = null;
		if(applicationConfig!=null){
			serverNodes= applicationConfig.getSubNodes("server");
		}
		if(serverNodes==null||serverNodes.size()==0)
			return;
		for(XmlNode serverNode:serverNodes){
			ServerConfig serverConfig = new ServerConfig();
			serverConfig.setPort(Integer.parseInt(serverNode.getAttribute("port")));
			serverConfig.setMode(serverNode.getAttribute("mode"));
			serverConfig.setType(serverNode.getAttribute("type"));
			serverConfig.setName(serverNode.getAttribute("name"));
			serverConfig.setConfig(serverNode);
			serverConfigs.add(serverConfig);
		}
	}
	
	private void initClintConfigs(){
		List<XmlNode> clientNodes= null;
		if(applicationConfig!=null){
		    clientNodes= applicationConfig.getSubNodes("client");
		}
		if(clientNodes==null||clientNodes.size()==0)
			return;
		for(XmlNode clientNode:clientNodes){
			ClientConfig clientConfig = new ClientConfig();
			clientConfig.setIp(clientNode.getAttribute("ip"));
			clientConfig.setPort(Integer.parseInt(clientNode.getAttribute("port")));
			clientConfig.setMode(clientNode.getAttribute("mode"));
			clientConfig.setName(clientNode.getAttribute("name"));
			clientConfig.setType(clientNode.getAttribute("type"));
			clientConfig.setConfig(clientNode);
			clientConfigs.add(clientConfig);
		}
	}
	
	void startNet(){

		startServers();
		startClients();
	}
	
	private void startServers() {
		for(ServerConfig serverConfig:serverConfigs){
			try {
				Class<?> clazz = Class.forName(serverConfig.getType());
				Server server = (Server) clazz.getConstructor(int.class).newInstance(serverConfig.getPort());
				serverMaps.put(serverConfig.getName(), server);
				server.run();
			} catch (Exception e) {
				logger.errorMessage("执行默认服务端启动程序时出错:构造服务端对象实例失败,服务端类型:{0}", e,serverConfig.getType());
			}
		}
	}

	private void startClients() {
		for(ClientConfig clientConfig:clientConfigs){
			try {
				Class<?> clazz = Class.forName(clientConfig.getType());
				DaemonRunnable clientThread = (DaemonRunnable) clazz.getConstructor(String.class,int.class).newInstance(clientConfig.getIp(),clientConfig.getPort());
				clientMaps.put(clientConfig.getName(), clientThread);
				DaemonUtils.daemon(clientConfig.getName(), clientThread);
			} catch (Exception e) {
				logger.errorMessage("执行默认客户端启动程序时出错:构造客户端对象实例失败,客户端类型:{0}", e,clientConfig.getType());
			}
		}
	}
	
	private void stopServers(){
		for(String name:serverMaps.keySet()){
			try {
				serverMaps.get(name).stop();
			} catch (Exception e) {
				logger.errorMessage("执行默认服务端停止程序时出错,服务端名:{0}", e,name);
			}
		}
	}
	
	private void stopClients(){
		for(String name:clientMaps.keySet()){
			try {
				clientMaps.get(name).stop();
			} catch (Exception e) {
				logger.errorMessage("执行默客户端停止程序时出错,客户端名:{0}", e,name);
			}
		}
	}
	

}

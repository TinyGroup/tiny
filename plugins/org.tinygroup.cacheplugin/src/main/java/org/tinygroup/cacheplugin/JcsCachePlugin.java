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
package org.tinygroup.cacheplugin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

import org.apache.jcs.auxiliary.remote.server.RemoteCacheServerFactory;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.utils.props.PropertyLoader;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.plugin.Plugin;
import org.tinygroup.xmlparser.node.XmlNode;

public class JcsCachePlugin extends AbstractConfiguration implements Plugin {
	
	private static final String JCS_CONFIG_PATH = "/application/jcs-config";
	private static final int DEFAULT_REGISTRY_PORT = 1101;
	private static final String DEFAULT_PROPS_FILE_NAME = "cache.ccf";
	private boolean enable = false;

	public void start() {
		XmlNode combineNode=ConfigurationUtil.combineXmlNode(applicationConfig, componentConfig);
		if(combineNode!=null){
			String enableString = combineNode.getAttribute("enable");
			if (!StringUtil.isBlank(enableString)) {
				enable = Boolean.valueOf(enableString);
			}
		}
		if (enable) {
			logger.logMessage(LogLevel.INFO, "jcscacheplugin is starting...");
			startCluster();
			logger.logMessage(LogLevel.INFO, "jcscacheplugin is started");
		}
	}
	private void startCluster() {
		int registryPort = DEFAULT_REGISTRY_PORT;
		try {
			Properties props = PropertyLoader
					.loadProperties(DEFAULT_PROPS_FILE_NAME);
			if (props != null) {
				String portS = props.getProperty("registry.port",
						String.valueOf(DEFAULT_REGISTRY_PORT));

				try {
					registryPort = Integer.parseInt(portS);
				} catch (NumberFormatException e) {
					logger.errorMessage("Problem converting port to an int.", e);
				}
			}
		} catch (Exception e) {
			logger.errorMessage("Problem loading props.", e);
		}

		// we will always use the local machine for the registry
		String registryHost;
		try {
			registryHost = InetAddress.getLocalHost().getHostAddress();
			logger.logMessage(LogLevel.DEBUG, "registryHost =[{}]",
					registryHost);

			if ("localhost".equals(registryHost)
					|| "127.0.0.1".equals(registryHost)) {
				logger.logMessage(
						LogLevel.WARN,
						"The local address [{}] is INVALID.  Other machines must be able to use the address to reach this server.",
						registryHost);
			}

			try {
				LocateRegistry.createRegistry(registryPort);
			} catch (RemoteException e) {
				logger.errorMessage(
						"Problem creating registry.  It may already be started. {}",
						e, e.getMessage());
			} catch (Exception t) {
				logger.errorMessage("Problem creating registry.", t);
			}

			try {
				RemoteCacheServerFactory.startup(registryHost, registryPort,
						"/" + DEFAULT_PROPS_FILE_NAME);
			} catch (IOException e) {
				logger.errorMessage("Problem starting remote cache server.", e);
			}

			catch (Exception t) {
				logger.errorMessage("Problem starting remote cache server.", t);
			}
		} catch (UnknownHostException e) {
			logger.errorMessage(
					"Could not get local address to use for the registry!", e);
		}
	}

	public void stop() {
		if (enable) {
			logger.logMessage(LogLevel.INFO, "Shutting down remote cache ");
			CompositeCacheManager.getInstance().shutDown();
			CompositeCache.elementEventQ.destroy();//关闭jcs开启的后台线程
		    logger.logMessage(LogLevel.INFO, "jcscacheplugin is stopped");
		}
	}

	public String getApplicationNodePath() {
		return JCS_CONFIG_PATH;
	}

	public String getComponentConfigPath() {
		return "/jcscache.config.xml";
	}

}

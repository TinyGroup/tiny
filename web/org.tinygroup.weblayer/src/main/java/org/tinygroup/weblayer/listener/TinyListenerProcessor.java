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
package org.tinygroup.weblayer.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * ServletListener的应用程序处理器
 * 
 * @author renhui
 * 
 */
public class TinyListenerProcessor extends AbstractConfiguration implements ApplicationProcessor  {

	private static final String CONTEXT_PARAM = "context-param";

	private static final String LISTENER_BEAN = "listener-bean";

	private static final String LISTENER_NODE_PATH = "/application/tiny-listener";

	public static final String LISTENER_NODE_CONFIG = "tiny-listener-config";

	private List<ServletContextListener> listeners = CollectionUtil
			.createArrayList();

	private ServletContextEvent event;

	public void start() {

		initListeners();
		startListeners();

	}

	private void startListeners() {
		for (ServletContextListener listener : listeners) {
			listener.contextInitialized(event);
		}

	}

	private void initListeners() {
		List<XmlNode> listenerNodes = ConfigurationUtil.combineSubList(
				LISTENER_BEAN, applicationConfig, componentConfig);
		for (XmlNode node : listenerNodes) {
			ServletContextListener listener = SpringUtil.getBean(node
					.getAttribute("name"));
			listeners.add(listener);
		}
		TinyServletContext servletContext = (TinyServletContext) ServletContextHolder
				.getServletContext();
		if(servletContext!=null){//不是web程序启动，比如是测试用例启动插件
			servletContext.setFullContextFileRepository(SpringUtil.getBean(FullContextFileRepository.class));
			event = new ServletContextEvent(servletContext);
			List<XmlNode> paramNodes = ConfigurationUtil.combineSubList(
					CONTEXT_PARAM, applicationConfig, componentConfig);
			for (XmlNode node : paramNodes) {
				String name = node.getAttribute("name");
				String value = node.getAttribute("value");
				servletContext.setInitParameter(name, value);
			}
			servletContext.setInitParameter(LISTENER_NODE_CONFIG,
					applicationConfig.toString());
		}
		
	}

	public void stop() {

		stopListeners();

	}

	private void stopListeners() {
		for (ServletContextListener listener : listeners) {
			listener.contextDestroyed(event);
		}

	}

	public String getApplicationNodePath() {
		return LISTENER_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return "/tinylistener.config.xml";
	}

	public void setApplication(Application application) {
		//如果程序中需要访问application方法，则需要设置为属性
		
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}
}

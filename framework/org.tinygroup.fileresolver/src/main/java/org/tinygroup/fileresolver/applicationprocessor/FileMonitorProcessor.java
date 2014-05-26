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
package org.tinygroup.fileresolver.applicationprocessor;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.xmlparser.node.XmlNode;

public class FileMonitorProcessor extends AbstractConfiguration implements
		ApplicationProcessor {

	private static final String FILE_MONITOR_NODE_PATH = "/application/file-monitor";
	private static final int DEFAULT_INTERVAL = 5;
	private FileMonitorThread thread = null;
	private int interval = DEFAULT_INTERVAL;
	private boolean enable = false;
	private FileResolver resolver;
	private byte[] synObject = new byte[0];

	public FileResolver getResolver() {
		return resolver;
	}

	public void setResolver(FileResolver resolver) {
		this.resolver = resolver;
	}

	public String getApplicationNodePath() {
		return FILE_MONITOR_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return "/filemonitor.config.xml";
	}

	public void start() {
		XmlNode combineNode = ConfigurationUtil.combineXmlNode(
				applicationConfig, componentConfig);
		if (combineNode != null) {
			String strInterrupt = combineNode.getAttribute("interval");
			if (strInterrupt != null && strInterrupt.length() > 0) {
				interval = Integer.parseInt(strInterrupt);
			}
			String enableString = combineNode.getAttribute("enable");
			if (enableString != null && enableString.length() > 0) {
				enable = Boolean.valueOf(enableString);
			}
		}

		if (enable) {
			if (thread != null) {
				thread.stop = true;
			}
			thread = new FileMonitorThread();
			thread.setDaemon(true);
			thread.start();
		}

	}

	public void stop() {
		synchronized (synObject) {
			if (enable && thread != null && thread.isAlive()) {
				thread.stop = true;
				synObject.notify();//唤醒其他等待的线程
			}
		}
	}

	private class FileMonitorThread extends Thread {
		private static final int MILLISECOND_PER_SECOND = 1000;
		private volatile boolean stop = false;

		public void run() {
			while (!stop) {
				try {
					synchronized (synObject) {
						synObject.wait(interval * MILLISECOND_PER_SECOND);//没有线程唤醒，那么等待这么多时间
						if(!stop){//唤醒时，或者等待时间后，发现stop=false时去执行重新搜索。
							logger.logMessage(LogLevel.INFO, "定时扫描文件变化......");
							resolver = SpringUtil.getBean("fileResolver");
							resolver.refresh();
							logger.logMessage(LogLevel.INFO, "定时扫描文件结束.");
						}
					}
				} catch (InterruptedException e) {
					logger.errorMessage(e.getMessage(), e);
				}
			}
		}
	}

	public void setApplication(Application application) {

	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}
}

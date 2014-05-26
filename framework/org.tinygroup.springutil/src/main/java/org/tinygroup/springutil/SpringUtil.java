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
package org.tinygroup.springutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.StringUtils;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;

public final class SpringUtil {

	private static Logger logger = LoggerFactory.getLogger(SpringUtil.class);
	// 当BU放在ClassPath外时候，顶层ApplicationContext
	static ApplicationContext applicationContext = null;
	static List<String> configs = new ArrayList<String>();
	static boolean inited = false;
	static {
		init();
	}

	public static void init() {
		if (inited == true)
			return;
		inited = true;
		FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext();
		fileSystemXmlApplicationContext.setAllowBeanDefinitionOverriding(true);
		applicationContext = fileSystemXmlApplicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		String[] beanNames = applicationContext.getBeanNamesForType(clazz);
		if (beanNames.length == 1) {
			return (T) applicationContext.getBean(beanNames[0], clazz);
		} else {
			throw new NoSuchBeanDefinitionException(clazz,
					"expected single bean but found "
							+ beanNames.length
							+ ": "
							+ StringUtils
									.arrayToCommaDelimitedString(beanNames));
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name, Class<T> clazz) {
		return (T) applicationContext.getBean(name, clazz);
	}

	public static Map<?, ?> getCustomEditors() {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((FileSystemXmlApplicationContext) applicationContext)
				.getBeanFactory();
		return beanFactory.getCustomEditors();
	}

	private static String[] listToArray(List<String> list) {
		String[] a = new String[0];
		return (String[]) list.toArray(a);
	}

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> getBeansOfType(Class<T> type) {
		return applicationContext.getBeansOfType(type).values();
	}

	/**
	 * 当Bu全部放在lib下时的BeansFileProcessor调用此方法
	 * 
	 * @param files
	 */
	public static void regSpringConfigXml(List<FileObject> files) {
		for (FileObject fileObject : files) {
			String urlString = fileObject.getURL().toString();
			addUrl(urlString);
		}
	}

	public static void regSpringConfigXml(String files) {
		String urlString = SpringUtil.class.getResource(files).toString();
		addUrl(urlString);
	}

	private static void addUrl(String urlString) {
		if (!configs.contains(urlString)) {
			configs.add(urlString);
			logger.logMessage(LogLevel.INFO, "添加Spring配置文件:{urlString}",
					urlString);
		}
	}

	public static void refresh() {
		FileSystemXmlApplicationContext app = (FileSystemXmlApplicationContext) applicationContext;
		app.setConfigLocations(listToArray(configs));
		app.refresh();
	}

	public static void destory() {
		FileSystemXmlApplicationContext app = (FileSystemXmlApplicationContext) applicationContext;
		app.close();
	}

}

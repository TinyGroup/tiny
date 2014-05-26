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
package org.tinygroup.bundle.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.bundle.BundleActivator;
import org.tinygroup.bundle.BundleContext;
import org.tinygroup.bundle.BundleEvent;
import org.tinygroup.bundle.BundleException;
import org.tinygroup.bundle.BundleManager;
import org.tinygroup.bundle.config.BundleDefine;
import org.tinygroup.bundle.loader.TinyClassLoader;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;

/**
 * Created by luoguo on 2014/5/4.
 */
public class BundleManagerDefault implements BundleManager {
	private static Logger logger = LoggerFactory
			.getLogger(BundleManagerDefault.class);
	private Map<String, BundleDefine> bundleDefineMap = new HashMap<String, BundleDefine>();
	private Map<BundleDefine, TinyClassLoader> tinyClassLoaderMap = new HashMap<BundleDefine, TinyClassLoader>();
	private String bundleRoot;
	private String commonRoot;
	private BundleContext bundleContext = new BundleContextImpl();
	private TinyClassLoader tinyClassLoader = new TinyClassLoader();
	private List<BundleEvent> beforeStartBundleEvent = new ArrayList<BundleEvent>();
	private List<BundleEvent> afterStartBundleEvent = new ArrayList<BundleEvent>();
	private List<BundleEvent> beforeStopBundleEvent = new ArrayList<BundleEvent>();
	private List<BundleEvent> afterStopBundleEvent = new ArrayList<BundleEvent>();

	public void addBundleDefine(BundleDefine bundleDefine) {
		bundleDefineMap.put(bundleDefine.getName(), bundleDefine);
	}

	public BundleDefine getBundleDefine(String bundleName)
			throws BundleException {
		BundleDefine bundleDefine = bundleDefineMap.get(bundleName);
		if (bundleDefine != null) {
			return bundleDefine;
		}
		throw new BundleException("找不到杂物箱定义：" + bundleName);
	}

	/*
	 * 
	 * @see
	 * org.tinygroup.bundle.BundleManager#removeBundle(org.tinygroup.bundle.
	 * config.BundleDefine)
	 */
	public void removeBundle(BundleDefine bundleDefine) throws BundleException {
		logger.logMessage(LogLevel.INFO, "开始移除Bundle:{0}", bundleDefine.getName());
		if (bundleDefineMap.get(bundleDefine.getName()) != null) {
			stop(bundleDefine);
			bundleDefineMap.remove(bundleDefine.getName());
			logger.logMessage(LogLevel.INFO, "移除Bundle:{0}完毕", bundleDefine.getName());
			return ;
		}
		throw new BundleException("找不到杂物箱定义：" + bundleDefine.getName());
	}

	public void setBundleRoot(String path) {
		this.bundleRoot = path;
	}

	public void setCommonRoot(String path) {
		this.commonRoot = path;
	}

	public String getBundleRoot() {
		return bundleRoot;
	}

	public String getCommonRoot() {
		return commonRoot;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public TinyClassLoader getTinyClassLoader() {
		return tinyClassLoader;
	}

	public void setBeforeStartBundleEvent(List<BundleEvent> bundleEvents) {
		this.beforeStartBundleEvent = bundleEvents;
	}

	public void setAfterStartBundleEvent(List<BundleEvent> bundleEvents) {
		this.afterStartBundleEvent = bundleEvents;
	}

	public void setBeforeStopBundleEvent(List<BundleEvent> bundleEvents) {
		this.beforeStopBundleEvent = bundleEvents;
	}

	public void setAfterStopBundleEvent(List<BundleEvent> bundleEvents) {
		this.afterStopBundleEvent = bundleEvents;
	}

	public TinyClassLoader getTinyClassLoader(BundleDefine bundleDefine) {
		return tinyClassLoaderMap.get(bundleDefine);
	}

	public void start() {
		logger.logMessage(LogLevel.INFO, "开始启动所有Bundle");
		for (BundleDefine bundleDefine : bundleDefineMap.values()) {
			if (!tinyClassLoaderMap.containsKey(bundleDefine)) {// 如果说没有loader，就代表还没启动
				start( bundleDefine);
			}
		}
		logger.logMessage(LogLevel.INFO, "启动所有Bundle完毕");
	}

	public void stop() {
		logger.logMessage(LogLevel.INFO, "开始停止所有Bundle");
		for (BundleDefine bundleDefine : bundleDefineMap.values()) {
			if (tinyClassLoaderMap.containsKey(bundleDefine)) {// 如果说有loader，就代表还没停止
				stop(bundleDefine);
			}
		}
		logger.logMessage(LogLevel.INFO, "停止所有Bundle完毕");
	}

	public void start( String bundle) {
		logger.logMessage(LogLevel.INFO, "开始启动Bundle:{0}", bundle);
		BundleDefine bundleDefine = bundleDefineMap.get(bundle);
		if (tinyClassLoaderMap.containsKey(bundleDefine)) {// 如果说有loader，就代表还已经启动
			logger.logMessage(LogLevel.INFO, "Bundle:{0}已启动，无需再次启动", bundle);
			return;
		}
		if(!checkDepend(bundleDefine, bundle)){
			return;
		}
		startBundle(bundleDefine, bundle);
		logger.logMessage(LogLevel.INFO, "启动Bundle:{0}完毕", bundle);
	}

	public void start(BundleDefine bundleDefine) {
		String bundle = bundleDefine.getName();
		logger.logMessage(LogLevel.INFO, "开始启动Bundle:{0}", bundle);
		if (tinyClassLoaderMap.containsKey(bundleDefine)) {// 如果说有loader，就代表还已经启动
			logger.logMessage(LogLevel.INFO, "Bundle:{0}已启动，无需再次启动", bundle);
			return;
		}
		if(!checkDepend(bundleDefine, bundle)){
			return;
		}
		startBundle( bundleDefine, bundle);
		logger.logMessage(LogLevel.INFO, "启动Bundle:{0}完毕", bundle);
	}
	
	private boolean checkDepend(BundleDefine bundleDefine,String bundle){
		String[] dependens = bundleDefine.getDependencyArray(); // 获取所依赖的bundle项
		for (String dependen : dependens) {
			if (!bundleDefineMap.containsKey(dependen)) {
				logger.errorMessage("Bundle:" + bundle + "所依赖的Bundle:"
						+ dependen + "不存在，退出启动");
				return false;
			}
		}
		return true;
	}

	private void startBundle(
			BundleDefine bundleDefine, String bundle) {

		startBundleDepend(bundleDefine, bundle);

		processEvents(beforeStartBundleEvent, bundleContext, bundleDefine);

		loadBundleLoader(bundleDefine, bundle);

		startBundleActivator(bundleDefine, bundle);

		processEvents(afterStartBundleEvent, bundleContext, bundleDefine);

	}

	private void startBundleDepend(
			BundleDefine bundleDefine, String bundle) {
		String[] dependens = bundleDefine.getDependencyArray(); // 获取所依赖的bundle项

		for (String dependen : dependens) { // 启动所有的依赖项
			logger.logMessage(LogLevel.DEBUG, "开始启动Bundle:{0}所依赖Bundle:{1}",
					bundle, dependen);
			start(dependen);
			logger.logMessage(LogLevel.DEBUG, "启动Bundle:{0}所依赖Bundle:{1}完毕",
					bundle, dependen);
		}
	}

	private void startBundleActivator(BundleDefine bundleDefine, String bundle) {
		// 执行activator
		String activatorBean = bundleDefine.getBundleActivator();
		if (activatorBean != null && !"".equals(activatorBean)) {
			BundleActivator activator = SpringUtil.getBean(activatorBean);
			try {
				activator.start(bundleContext);
			} catch (BundleException e) {
				logger.errorMessage("启动Bundle:{0}的Activator:{1}时出错", e, bundle,
						activatorBean);
			}
		}

	}

	private void loadBundleLoader(BundleDefine bundleDefine, String bundle) {
		String[] jars = getBundleComJar(bundleDefine.getCommonJars().split(","));
		String bundleDir = getBundleDir(bundle);
		URL[] urls = new URL[jars.length + 1];
		for (int i = 0; i < jars.length; i++) {
			File f = new File(jars[i]);
			try {
				urls[i] = f.toURL();
			} catch (MalformedURLException e) {
				logger.errorMessage("为路径{0}生成url时出错", e, jars[i]);
			}
		}
		File f = new File(bundleDir);
		try {
			urls[urls.length - 1] = f.toURL();
		} catch (MalformedURLException e1) {
			logger.errorMessage("为路径{0}生成url时出错", e1, bundleDir);
		}
		TinyClassLoader bundleLoder = new TinyClassLoader(urls, tinyClassLoader);
		tinyClassLoaderMap.put(bundleDefine, bundleLoder);
		
		String[] dependens = bundleDefine.getDependencyArray(); // 获取所依赖的bundle项
		for (String dependen : dependens) { // 启动所有的依赖项
			BundleDefine dependenBundle = bundleDefineMap.get(dependen);
			bundleLoder.addDependClassLoader(tinyClassLoaderMap.get(dependenBundle));
		}
	}

	private String getBundleDir(String bundleName) {
		String root = getBundleRoot();
		if (root.endsWith("/") || root.endsWith("\\")) {
			return getPath(root, "", bundleName);
		} else {
			return getPath(root, "/", bundleName);
		}
	}

	private String getPath(String root, String plus, String name) {
		return root + plus + name;

	}

	private String[] getBundleComJar(String[] jars) {
		String[] paths = new String[jars.length];
		String root = getCommonRoot();
		if (root.endsWith("/") || root.endsWith("\\")) {
			for (int i = 0; i < paths.length; i++) {
				paths[i] = getPath(root, "", jars[i]);
			}
		} else {
			for (int i = 0; i < paths.length; i++) {
				paths[i] = getPath(root, "/", jars[i]);
			}
		}
		return paths;
	}

	public void stop(String bundle) {
		logger.logMessage(LogLevel.INFO, "开始停止Bundle:{0}", bundle);
		BundleDefine bundleDefine = bundleDefineMap.get(bundle);
		if (!tinyClassLoaderMap.containsKey(bundleDefine)) {// 如果没有loader，就代表已停止
			logger.logMessage(LogLevel.INFO, "Bundle:{0}已停止，无需再次停止", bundle);
			return;
		}
		
		stopBundle(bundleDefine, bundle);
		logger.logMessage(LogLevel.INFO, "停止Bundle:{0}完毕", bundle);
	}

	public void stop(BundleDefine bundleDefine) {
		String bundle = bundleDefine.getName();
		logger.logMessage(LogLevel.INFO, "开始停止Bundle:{0}", bundle);
		if (!tinyClassLoaderMap.containsKey(bundleDefine)) {// 如果没有loader，就代表已停止
			logger.logMessage(LogLevel.INFO, "Bundle:{0}已停止，无需再次停止", bundle);
			return;
		}
		stopBundle(bundleDefine, bundle);
		logger.logMessage(LogLevel.INFO, "停止Bundle:{0}完毕", bundle);
	}

	private void stopBundle(
			BundleDefine bundleDefine, String bundle) {

		stopBundleDependBy(bundle);

		processEvents(beforeStopBundleEvent, bundleContext, bundleDefine);
		
		tinyClassLoader.removeDependTinyClassLoader(tinyClassLoaderMap.get(bundleDefine));
		tinyClassLoaderMap.remove(bundleDefine);
		
		stopBundleActivator(bundleContext,bundleDefine, bundle);

		processEvents(afterStopBundleEvent, bundleContext, bundleDefine);
	}

	private void stopBundleDependBy(
			String bundle) {
		List<String> dependencyByList = new ArrayList<String>();
		for (BundleDefine b : bundleDefineMap.values()) {
			String[] dependencyArray = b.getDependencyArray();
			for (String dependency : dependencyArray) {
				if (bundle.equals(dependency)) {
					dependencyByList.add(b.getName());
				}
			}
		}
		
		for (String dependenBy : dependencyByList) { // 启动所有的依赖项
			logger.logMessage(LogLevel.DEBUG, "开始停止依赖Bundle:{0}的Bundle:{1}",
					bundle, dependenBy);
			stop( dependenBy);
			logger.logMessage(LogLevel.DEBUG, "停止依赖Bundle:{0}的Bundle:{1}完毕",
					bundle, dependenBy);
		}
	}

	private void stopBundleActivator(BundleContext bundleContext,BundleDefine bundleDefine, String bundle) {
		// 执行activator
		String activatorBean = bundleDefine.getBundleActivator();
		if (activatorBean != null && !"".equals(activatorBean)) {
			BundleActivator activator = SpringUtil.getBean(activatorBean);
			try {
				activator.stop(bundleContext);
			} catch (BundleException e) {
				logger.errorMessage("停止Bundle:{0}的Activator:{1}时出错", e, bundle,
						activatorBean);
			}
		}
	}

	/**
	 * 针对某个bundleDefine处理事件
	 * 
	 * @param events
	 *            需要处理的事件列表
	 * @param bundleContext
	 * @param bundleDefine
	 */
	private void processEvents(List<BundleEvent> events,
			BundleContext bundleContext, BundleDefine bundleDefine) {
		for (BundleEvent event : events) {
			event.process(bundleContext, bundleDefine);
		}
	}

	public TinyClassLoader getTinyClassLoader(String bundle) {
		BundleDefine bundleDefine = bundleDefineMap.get(bundle);
		return tinyClassLoaderMap.get(bundleDefine);
	}

}

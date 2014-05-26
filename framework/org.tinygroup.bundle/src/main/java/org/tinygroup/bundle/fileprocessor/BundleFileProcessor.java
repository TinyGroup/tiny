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
package org.tinygroup.bundle.fileprocessor;

import org.tinygroup.bundle.BundleException;
import org.tinygroup.bundle.BundleManager;
import org.tinygroup.bundle.config.BundleDefine;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class BundleFileProcessor extends AbstractFileProcessor{
	private static final String BUNDLE_EXT_FILENAME = ".bundle.xml";
	private static final String BUNDLE_XSTREAM = "bundle";
	private BundleManager bundleManager ;
	
	public BundleManager getBundleManager() {
		return bundleManager;
	}

	public void setBundleManager(BundleManager bundleManager) {
		this.bundleManager = bundleManager;
	}

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(BUNDLE_EXT_FILENAME);
	}

	public void process() {
		XStream stream = XStreamFactory.getXStream(BUNDLE_XSTREAM);
		for (FileObject file : deleteList) {
			logger.logMessage(LogLevel.INFO, "移除Bundle配置文件:{0}",
					file.getFileName());
			BundleDefine bundle = (BundleDefine) caches.get(file.getAbsolutePath());
			if(bundle!=null){
				try {
					bundleManager.removeBundle(bundle);
				} catch (BundleException e) {
					logger.errorMessage("移除Bundle:{0}时出错", e,bundle.getName());
				}
				caches.remove(file.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "移除Bundle配置文件:{0}完成",
					file.getFileName());
		}
		for (FileObject file : changeList) {
			logger.logMessage(LogLevel.INFO, "开始读取Bundle配置文件:{0}",
					file.getFileName());
			BundleDefine oldBundle = (BundleDefine) caches.get(file.getAbsolutePath());
			if(oldBundle!=null){
				try {
					bundleManager.removeBundle(oldBundle);
				} catch (BundleException e) {
					logger.errorMessage("移除Bundle:{0}时出错", e,oldBundle.getName());
				}
			}
			BundleDefine bundle = (BundleDefine)  stream.fromXML(file
					.getInputStream());
			bundleManager.addBundleDefine(bundle);
			caches.put(file.getAbsolutePath(), bundle);
			logger.logMessage(LogLevel.INFO, "读取Bundle配置文件:{0}完成",
					file.getFileName());
		}
	}

}

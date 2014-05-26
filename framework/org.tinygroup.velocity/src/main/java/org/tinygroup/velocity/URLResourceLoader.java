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
package org.tinygroup.velocity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

/**
 * 原来的Velocity的资源加载器是用得相对路径，而Tiny用得是完整路径，所以自己实现之
 * 
 * @author luoguo
 * 
 */
public class URLResourceLoader extends ResourceLoader {
	private Map<String, Long> resourceModifiedTimeMap = new HashMap<String, Long>();
	private FullContextFileRepository fullContextFileRepository;

	
	public void init(ExtendedProperties configuration) {
		fullContextFileRepository = SpringUtil
				.getBean("fullContextFileRepository");
	}

	
	public InputStream getResourceStream(String source) {
		FileObject fileObject = fullContextFileRepository.getFileObject(source);
		if (fileObject == null || !fileObject.isExist()) {
			throw new ResourceNotFoundException(source);
		}
		return fileObject.getInputStream();
	}

	
	public boolean isSourceModified(Resource resource) {
		Long oldTime = resourceModifiedTimeMap.get(resource.getName());
		if (oldTime == null || oldTime != getLastModified(resource)) {
			return true;
		}
		long lastModifiedTime = VFS.resolveFile(resource.getName())
				.getLastModifiedTime();
		return oldTime == lastModifiedTime;
	}

	
	public long getLastModified(Resource resource) {
		long lastModifiedTime = VFS.resolveFile(resource.getName())
				.getLastModifiedTime();
		resourceModifiedTimeMap.put(resource.getName(), lastModifiedTime);
		return lastModifiedTime;
	}

}

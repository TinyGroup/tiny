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
package org.tinygroup.bundle.loader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.VFS;

/**
 * TinyClassLoader主要用于动态加载类及资源处理 Created by luoguo on 14-2-25.
 */
public class TinyClassLoader extends URLClassLoader {

	private FileObject[] fileObjects = null;
	private List<TinyClassLoader> dependClassLoaderList = new ArrayList<TinyClassLoader>();

	/**
	 * 构造方法
	 * 
	 * @param urls
	 *            类加载器要加载的jar包列表
	 * @param parent
	 *            父亲类加载器
	 */
	public TinyClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	/**
	 * 构造方法
	 * 
	 * @param urls
	 *            类加载器要加载的jar包列表
	 */
	public TinyClassLoader(URL[] urls) {
		super(urls);
	}

	public TinyClassLoader() {
		super(new URL[0]);
	}

	/**
	 * 构造方法
	 * 
	 * @param urls
	 *            类加载器要加载的jar包列表
	 * @param parent
	 *            父亲类加载器
	 * @param factory
	 */
	public TinyClassLoader(URL[] urls, ClassLoader parent,
			URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		for(TinyClassLoader loader: dependClassLoaderList){
			try {
				return loader.loadClass(name);
			} catch (ClassNotFoundException e) {
				// TODO: handle exception
			}
		}
		return loadClass(name, false);
	}

	/**
	 * 返回Jar包对应文件的FileObject数组，如果包含了子TinyClassLoader，
	 * 也返回子TinyClassLoader包含的Jar对应的FileObject
	 * 
	 * @return
	 */
	public FileObject[] getAllFileObjects() {
		List<FileObject> result = new ArrayList<FileObject>();
		getFileObjects(result);
		return result.toArray(new FileObject[0]);
	}

	private void getFileObjects(List<FileObject> result) {
		for (FileObject fileObject : getFileObjects()) {
			result.add(fileObject);
		}
		for (TinyClassLoader tinyClassLoader : dependClassLoaderList) {
			tinyClassLoader.getFileObjects(result);
		}
	}

	/**
	 * 返回当前TinyClassLoader中包含Jar包对应的FileObject数组
	 * 
	 * @return
	 */
	public FileObject[] getFileObjects() {
		URL[] urLs = getURLs();
		if (fileObjects == null) {
			fileObjects = new FileObject[urLs.length];
			for (int i = 0; i < urLs.length; i++) {
				fileObjects[i] = VFS.resolveURL(urLs[i]);
			}
		}
		return fileObjects;
	}

	/**
	 * 添加子TinyClassLoade
	 * 
	 * @param tinyClassLoader
	 */
	public void addDependClassLoader(TinyClassLoader tinyClassLoader) {
		dependClassLoaderList.add(tinyClassLoader);
	}

	/**
	 * 删除子TinyClassLoade
	 * 
	 * @param tinyClassLoader
	 */
	public void removeDependTinyClassLoader(TinyClassLoader tinyClassLoader) {
		dependClassLoaderList.remove(tinyClassLoader);
	}

	/**
	 * 对所有层级的TinyClassLoader中的FileObject对象进行过滤
	 * 
	 * @param fileObjectFilter
	 * @param fileObjectProcessor
	 */
	public void foreachAll(FileObjectFilter fileObjectFilter,
			FileObjectProcessor fileObjectProcessor) {
		foreach(fileObjectFilter, fileObjectProcessor);
		for (TinyClassLoader tinyClassLoader : dependClassLoaderList) {
			tinyClassLoader.foreachAll(fileObjectFilter, fileObjectProcessor);
		}
	}

	/**
	 * 对当前层级的TinyClassLoader中的FileObject对象进行过滤
	 * 
	 * @param fileObjectFilter
	 * @param fileObjectProcessor
	 */
	public void foreach(FileObjectFilter fileObjectFilter,
			FileObjectProcessor fileObjectProcessor) {
		if (fileObjects != null) {
			for (FileObject fileObject : fileObjects) {
				fileObject.foreach(fileObjectFilter, fileObjectProcessor);
			}
		}
	}

	/**
	 * 覆盖父类的方法,在自己里找不到还找儿子
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> findClass(final String name)
			throws ClassNotFoundException {
		for (TinyClassLoader tinyClassLoader : dependClassLoaderList) {
			try {
				return tinyClassLoader.findClass(name);
			} catch (ClassNotFoundException e1) {
				// 如果子classloader找不到，不抛异常
			}
		}
		try {
			return super.findClass(name);
		} catch (ClassNotFoundException e) {

		}
		// 如果所有的都找不到，则抛异常
		throw new ClassNotFoundException(name);
	}

	/**
	 * 覆盖父类的findResource方法
	 * 
	 * @param name
	 * @return
	 */
	public URL findResource(final String name) {
		URL resource = super.findResource(name);
		if (resource == null) {
			for (TinyClassLoader tinyClassLoader : dependClassLoaderList) {
				resource = tinyClassLoader.findResource(name);
				if (resource != null) {
					return resource;
				}
			}
		}
		// 如果所有的都找不到，则返回null
		return null;
	}

	/**
	 * 覆盖父类的findResource方法
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public Enumeration<URL> findResources(final String name) throws IOException {
		final Enumeration<URL>[] enumerations = new Enumeration[1 + dependClassLoaderList
				.size()];
		enumerations[0] = super.findResources(name);
		for (int i = 0; i < dependClassLoaderList.size(); i++) {
			enumerations[i + 1] = dependClassLoaderList.get(i).findResources(
					name);
		}
		return new Enumeration<URL>() {
			private int index = 0;

			public boolean hasMoreElements() {
				boolean hasMoreElements = enumerations[index].hasMoreElements();
				while (!hasMoreElements && index < enumerations.length - 1) {
					index++;
					hasMoreElements = enumerations[index].hasMoreElements();
				}
				return hasMoreElements;
			}

			public URL nextElement() {
				if (index < enumerations.length) {
					return enumerations[index].nextElement();
				}
				return null;
			}
		};
	}
}

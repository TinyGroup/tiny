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
package org.tinygroup.annotation.impl;

import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.springutil.SpringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类路径管理
 * 
 * @author renhui
 * 
 */
public class JavaPathManage {

	private static final String FILE_RESOLVER = "fileResolver";
	private static final String BACK_SLASH = "\\";
	private static final String POINT = ".";
	// Jar包分格符
	private static final String EXCLAMATION_MARK = "!";
	// Jar包后缀名
	private static final String JAR_SUFFIX = ".jar";
	// !/符号
	private static final String  EXCLAMATION_AND_SLASH_MARK="!/";
	//  /符号
	private static final String SLASH_MARK="/";
	
	// 保存的类根路径列表
	private List<String> javaPaths = new ArrayList<String>();
	// 文件扫描器对象
	private FileResolver fileResolver;

	public void init() {
		fileResolver = SpringUtil.getBean(FILE_RESOLVER);
		List<String> paths=new ArrayList<String>();
		paths.addAll(fileResolver.getAllScanningPath());
		for (String path : paths) {
			if(path.indexOf(JAR_SUFFIX)==-1){
				if(!path.endsWith(SLASH_MARK)){
					path=path.concat(SLASH_MARK);
				}
			javaPaths.add(path.replace(BACK_SLASH, SLASH_MARK));
			}
		}
	}

	/**
	 * 根据路径获取类全路径
	 * 
	 * @param fileName
	 * @return
	 */
	public String obtainClassName(String fileName) {
		String className = null;
		int jarIndex = fileName.indexOf(EXCLAMATION_MARK);
		if (jarIndex != -1) {
			className = fileName.substring(jarIndex + EXCLAMATION_AND_SLASH_MARK.length(), fileName.length());
		} else {
			for (String class_path : javaPaths) {
				int index = fileName.indexOf(class_path);
				if (index != -1) {
					className = fileName.substring(
							index + class_path.length(), fileName.length());
					break;
				}
			}
		}
		return className.replace(SLASH_MARK, POINT);

	}

}

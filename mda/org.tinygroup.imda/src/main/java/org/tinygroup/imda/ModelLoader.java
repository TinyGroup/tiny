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
package org.tinygroup.imda;

import org.tinygroup.vfs.FileObject;

/**
 * 文件类型模型加载器，用于读取文件，并返回模型对象
 * 
 * @author luoguo
 * 
 */
public interface ModelLoader {
	/**
	 * 返回加载的文件扩展名
	 * 
	 * @return
	 */
	String getExtFileName();

	/**
	 * 输入文件对象，返回模型对象
	 * 
	 * @param fileObject
	 * @return
	 */
	Object loadModel(FileObject fileObject);
}

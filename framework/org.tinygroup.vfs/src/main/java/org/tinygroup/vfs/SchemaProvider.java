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
package org.tinygroup.vfs;

/**
 * 模式提供者接口
 * 
 * @author luoguo
 * 
 */
public interface SchemaProvider {
	/**
	 * 是否匹配
	 * 
	 * @param resource
	 * @return 如果返回true，表示此提供者可以处理，返回false表示不能处理
	 */
	boolean isMatch(String resource);

	/**
	 * 返回处理的模式
	 * 
	 * @return
	 */
	String getSchema();

	/**
	 * 解析资源，并返回文件对象
	 * 
	 * @param resource
	 * @return
	 */
	FileObject resolver(String resource);
}

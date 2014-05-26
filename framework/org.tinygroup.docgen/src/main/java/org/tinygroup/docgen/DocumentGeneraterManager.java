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
package org.tinygroup.docgen;

/**
 * 
 * 功能说明: 文档生成管理器
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-7-25 <br>
 * <br>
 */
public interface DocumentGeneraterManager {
	String MANAGER_BEAN_NAME = "documentGeneraterManager";

	/**
	 * 
	 * 
	 * @param type
	 *            文档类型
	 * @param generater
	 *            对应的文档生成器
	 */
	void putDocumentGenerater(String type, DocumentGenerater generater);

	/**
	 * 
	 * 根据文档类型获取文档生成器
	 * 
	 * @param type
	 *            文档类型
	 * @return
	 */
	DocumentGenerater getFileGenerater(String type);
}

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
package org.tinygroup.fileresolver;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.config.Configuration;
import org.tinygroup.vfs.FileObject;

/**
 * 文件处理器
 * 
 * @author luoguo
 * 
 */
public interface FileProcessor extends Configuration,Ordered {
	/**
	 * 返回要处理的扩展名
	 * 
	 * @return
	 */
	boolean isMatch(FileObject fileObject);

	/**
	 * 是否支持刷新
	 * 
	 * @return
	 */
	boolean supportRefresh();

	/**
	 * 设置文件查找器
	 * 
	 * @param fileResolver
	 */
	void setFileResolver(FileResolver fileResolver);

	/**
	 * 新增文件
	 * 
	 * @param fileObject
	 */
	void add(FileObject fileObject);

	/**
	 * 没有变化
	 * 
	 * @param fileObject
	 */
	void noChange(FileObject fileObject);

	/**
	 * 修改文件时的处理
	 * 
	 * @param fileObject
	 */
	void modify(FileObject fileObject);

	/**
	 * 删除文件时的处理
	 * 
	 * @param fileObject
	 */
	void delete(FileObject fileObject);

	/**
	 * 对文件进行处理
	 */
	void process();

	/**
	 * 处理完成后执行
	 */
	void clean();
}

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
package org.tinygroup.parser;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 一个标记语言的文档
 * 
 * @author luoguo
 * 
 * @param <T>
 */
public interface Document<T extends Node<T>> {
	/**
	 * 获取根结点
	 * 
	 * @return T
	 */
	T getRoot();

	/**
	 * 设置根结点
	 * 
	 * @param root
	 */
	void setRoot(T root);

	/**
	 * 将XML文档写入指定的输入流中
	 * 
	 * @param out
	 * @throws IOException
	 */
	void write(OutputStream out) throws IOException;
}

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

import java.io.File;
import java.io.Writer;

import org.tinygroup.context.Context;
import org.tinygroup.vfs.FileObject;

/**
 * 
 * 功能说明: 文档生成器
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-7-25 <br>
 * <br>
 */
public interface DocumentGenerater {
	String DOCUMENT_GENERATE_BEAN_NAME = "documentGenerater";

	/**
	 * 
	 * 根据velocity模板文件生成文件
	 * 
	 * @param path
	 *            velocity模板文件
	 * @param context
	 *            默认文件需要用到的上下文环境
	 * @param writer
	 *            输出的流
	 */
	void generate(String path, Context context, Writer writer);

	/**
	 * 
	 * 根据velocity模板文件生成文件
	 * 
	 * @param path
	 *            velocity模板文件
	 * @param context
	 *            默认文件需要用到的上下文环境
	 * @param outputFile
	 *            输出文件
	 */
	void generate(String path, Context context, File outputFile);

	/**
	 * 
	 * 增加宏文件模板
	 * 
	 * @param fileObject
	 *            宏模板文件
	 */
	void addMacroFile(FileObject fileObject);
	
	/**
	 * 
	 * 移除宏文件模板
	 * 
	 * @param fileObject
	 *            宏模板文件
	 */
	void removeMacroFile(FileObject fileObject);
}

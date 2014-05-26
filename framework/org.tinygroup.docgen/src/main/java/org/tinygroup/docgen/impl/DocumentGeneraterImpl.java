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
package org.tinygroup.docgen.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.tinygroup.context.Context;
import org.tinygroup.docgen.DocumentGenerater;
import org.tinygroup.velocity.VelocityHelper;
import org.tinygroup.vfs.FileObject;

/**
 * 
 * 功能说明:文档生成器
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-7-25 <br>
 * <br>
 */
public class DocumentGeneraterImpl implements DocumentGenerater {
	private VelocityHelper documentGeneraterVelocityHelper;

	public VelocityHelper getDocumentGeneraterVelocityHelper() {
		return documentGeneraterVelocityHelper;
	}

	public void setDocumentGeneraterVelocityHelper(VelocityHelper helper) {
		this.documentGeneraterVelocityHelper = helper;
	}

	public void generate(String path, Context context, Writer writer) {
		try {
			documentGeneraterVelocityHelper.processTempleate(context, writer,
					path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addMacroFile(FileObject fileObject) {
		documentGeneraterVelocityHelper.addMacroFile(fileObject);
	}
	
	public void removeMacroFile(FileObject fileObject) {
		documentGeneraterVelocityHelper.removeMacroFile(fileObject);
	}
	

	public void generate(String path, Context context, File outputFile) {
		try {
			documentGeneraterVelocityHelper.processTempleate(context,
					new FileWriter(outputFile), path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

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
package org.tinygroup.compiler.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.tinygroup.compiler.CompileException;

/**
 * Created by luoguo on 2014/5/21.
 */
public class MemorySourceJdtCompiler extends JdtCompiler<MemorySource> {
	// String sourceFolder;
	//
	// public String getSourceFolder() {
	// return sourceFolder;
	// }
	//
	// public void setSourceFolder(String sourceFolder) {
	// this.sourceFolder = sourceFolder;
	// }
	public MemorySourceJdtCompiler(String outputDirectory) {
		setOutputDirectory(outputDirectory);
	}

	public void writeJavaFile(MemorySource source) throws IOException {
		File file = new File(getOutputDirectory() + File.separatorChar
				+ getJavaFileName(source));
		if (!file.exists()) {
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			file.createNewFile();

		}
		FileOutputStream stream = new FileOutputStream(file);
		stream.write(source.getContent().getBytes(getEncode()));
		stream.close();
	}

	private String getJavaFileName(MemorySource source) {
		return source.getQualifiedClassName().replace(".", File.separator)
				+ ".java";
	}

	public boolean compile(MemorySource source) throws CompileException {
		try {
			writeJavaFile(source);
			StringBuffer commandLine = getCommandLine().append(" \"")
					.append(getOutputDirectory()).append("\"");
			return executeCommand(commandLine.toString());
		} catch (IOException e) {
			throw new CompileException(e);
		}
	}

	public boolean compile(MemorySource[] sources) throws CompileException {
		try {
			for (MemorySource source : sources) {
				writeJavaFile(source);
			}
			StringBuffer commandLine = getCommandLine().append(" \"")
					.append(getOutputDirectory()).append("\"");
			return executeCommand(commandLine.toString());
		} catch (IOException e) {
			throw new CompileException(e);
		}
	}

	public boolean compile(Collection<MemorySource> sources)
			throws CompileException {
		try {
			for (MemorySource source : sources) {
				writeJavaFile(source);
			}
			StringBuffer commandLine = getCommandLine().append(" \"")
					.append(getOutputDirectory()).append("\"");
			return executeCommand(commandLine.toString());
		} catch (IOException e) {
			throw new CompileException(e);
		}
	}
}

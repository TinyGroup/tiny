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
package org.tinygroup.compiler.test;

import junit.framework.TestCase;

import org.tinygroup.compiler.CompileException;
import org.tinygroup.compiler.impl.FileSource;
import org.tinygroup.compiler.impl.FileSourceJdtCompiler;

import java.io.File;

public class FileSourceJdtCompilerTest extends TestCase{

//	public static void main(String[] args) throws CompileException {
//		String path = System.getProperty("user.dir");
//		String newPath = path+"\\test";
//		String newPath1 = path+"\\test1";
//		FileSourceJdtCompiler compiler = new FileSourceJdtCompiler();
//		FileSource[] fileSources = { new FileSource(newPath),
//				new FileSource(newPath1) };
//		compiler.compile(fileSources);
//	}
	
	public  void testCompile() {
		String path = System.getProperty("user.dir");
		String newPath = path+ File.separatorChar+"test";
		String newPath1 = path+File.separatorChar+"test1";
		FileSourceJdtCompiler compiler = new FileSourceJdtCompiler();
		FileSource[] fileSources = { new FileSource(newPath),
				new FileSource(newPath1) };
		try {
			boolean flag = compiler.compile(fileSources);
			assertTrue(flag);
		} catch (CompileException e) {
			assertTrue(false);
		}
	}
	
	
}

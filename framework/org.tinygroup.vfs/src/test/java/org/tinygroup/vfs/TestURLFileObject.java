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

import junit.framework.TestCase;
import org.tinygroup.vfs.impl.HttpFileObject;
import org.tinygroup.vfs.impl.HttpsFileObject;

import java.io.IOException;

public class TestURLFileObject extends TestCase {

	public void testHttpFileObject() throws IOException {
		
		FileObject fileObject=VFS.resolveFile("http://www.baidu.com/");
		assertTrue(fileObject instanceof HttpFileObject);
		FileUtils.printFileObject(fileObject);
		
	}
	
   public void testHttpsFileObject() throws IOException {
		
		FileObject fileObject=VFS.resolveFile("https://www.alipay.com/");
		assertTrue(fileObject instanceof HttpsFileObject);
		FileUtils.printFileObject(fileObject);
		
	}
   
//   public void testFtpFileObject(){
//		
//		FileObject fileObject=VFS.resolveFile("http://home.hundsun.com/WebSite.aspx");
//		assertTrue(fileObject instanceof FtpFileObject);
//		FileUtils.printFileObject(fileObject);
//		
//	}
	
	
}

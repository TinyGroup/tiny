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
package org.tinygroup.bundle.test.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.tinygroup.bundle.loader.TinyClassLoader;
import org.tinygroup.bundle.test.util.TestUtil;

import junit.framework.TestCase;

public class LoaderAgainTest extends TestCase{

	public void testLoadAgain(){
		String path1 = TestUtil.class.getResource("/").getPath()+"/"+"test1-0.0.1-SNAPSHOT.jar";
		String path2 = TestUtil.class.getResource("/").getPath()+"/"+"test2-0.0.1-SNAPSHOT.jar";
		try {
			URL u1 = new File(path1).toURL();
			TinyClassLoader l1 = new TinyClassLoader(new URL[]{u1});
			
			URL u2 = new File(path2).toURL();
			TinyClassLoader l2 = new TinyClassLoader(new URL[]{u2});
			
			l2.addDependClassLoader(l1);
			l1.loadClass("org.tinygroup.MyTestInterface");
			l1.loadClass("org.tinygroup.MyTestImpl");
			l2.loadClass("org.tinygroup.MyTestInterface");
			l2.loadClass("org.tinygroup.MyTestImpl");
			l2.loadClass("org.tinygroup.MyTestImpl2");
			
			
			TinyClassLoader l3 = new TinyClassLoader(new URL[]{u1});
			l2.removeDependTinyClassLoader(l1);
			l2.addDependClassLoader(l3);
			l3.loadClass("org.tinygroup.MyTestInterface");
			l3.loadClass("org.tinygroup.MyTestImpl");
			l2.loadClass("org.tinygroup.MyTestInterface");
			l2.loadClass("org.tinygroup.MyTestImpl");
			l2.loadClass("org.tinygroup.MyTestImpl2");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

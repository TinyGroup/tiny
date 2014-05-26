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
import java.net.URL;
import java.util.Enumeration;

import junit.framework.TestCase;

import org.tinygroup.bundle.loader.TinyClassLoader;
import org.tinygroup.vfs.FileObject;

/**
 * TinyClassLoader Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>02/25/2014</pre>
 */
public class TinyClassLoaderTest2 extends TestCase {
    TinyClassLoader grandClassLoader = null;
    TinyClassLoader fatherClassLoader;
    TinyClassLoader sonClassLoader;
    public TinyClassLoaderTest2() {
    }

    public void setUp() throws Exception {
        super.setUp();
        URL[] urls0 = {new File("src/test/resources/org.tinygroup.loader-0.0.13-SNAPSHOT.jar-tests.jar").toURL()};
        URL[] urls1 = {new File("src/test/resources/org.tinygroup.loader-0.0.13-SNAPSHOT.jar1-tests.jar").toURL()};
        URL[] urls2 = {new File("src/test/resources/org.tinygroup.loader-0.0.13-SNAPSHOT.jar2-tests.jar").toURL()};
        grandClassLoader = new TinyClassLoader(urls0);
        fatherClassLoader=new TinyClassLoader(urls1,grandClassLoader);
        sonClassLoader=new TinyClassLoader(urls2,fatherClassLoader);
        grandClassLoader.addDependClassLoader(fatherClassLoader);
        fatherClassLoader.addDependClassLoader(sonClassLoader);
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    
    public void testFindClass(){
    	
    	try {
    		
			Class clazz=Class.forName("org.tinygroup.hello.HelloImpl", true, grandClassLoader);
			assertTrue(clazz!=null);
			clazz=Class.forName("org.tinygroup.hello.HelloImpl1", true, grandClassLoader);
			assertTrue(clazz!=null);
			clazz=Class.forName("org.tinygroup.hello.HelloImpl2", true, grandClassLoader);
			assertTrue(clazz!=null);
			clazz=grandClassLoader.loadClass("org.tinygroup.hello.HelloImpl");
			assertTrue(clazz!=null);
			clazz=grandClassLoader.loadClass("org.tinygroup.hello.HelloImpl1");
			assertTrue(clazz!=null);
			clazz=grandClassLoader.loadClass("org.tinygroup.hello.HelloImpl2");
			assertTrue(clazz!=null);
			
			Class clazz2=Class.forName("org.tinygroup.hello.HelloImpl", true, fatherClassLoader);
			assertTrue(clazz2!=null);
			clazz2=Class.forName("org.tinygroup.hello.HelloImpl1", true, fatherClassLoader);
			assertTrue(clazz2!=null);
			clazz2=Class.forName("org.tinygroup.hello.HelloImpl2", true, fatherClassLoader);
			assertTrue(clazz2!=null);
			clazz2=fatherClassLoader.loadClass("org.tinygroup.hello.HelloImpl");
			assertTrue(clazz2!=null);
			clazz2=fatherClassLoader.loadClass("org.tinygroup.hello.HelloImpl1");
			assertTrue(clazz2!=null);
			clazz2=fatherClassLoader.loadClass("org.tinygroup.hello.HelloImpl2");
			assertTrue(clazz2!=null);
			
			Class clazz3=Class.forName("org.tinygroup.hello.HelloImpl", true, sonClassLoader);
			assertTrue(clazz3!=null);
			clazz3=Class.forName("org.tinygroup.hello.HelloImpl1", true, sonClassLoader);
			assertTrue(clazz3!=null);
			clazz3=Class.forName("org.tinygroup.hello.HelloImpl2", true, sonClassLoader);
			assertTrue(clazz3!=null);
			clazz3=sonClassLoader.loadClass("org.tinygroup.hello.HelloImpl");
			assertTrue(clazz3!=null);
			clazz3=sonClassLoader.loadClass("org.tinygroup.hello.HelloImpl1");
			assertTrue(clazz3!=null);
			clazz3=sonClassLoader.loadClass("org.tinygroup.hello.HelloImpl2");
			assertTrue(clazz3!=null);
			
			
		} catch (ClassNotFoundException e) {
			fail(e.getMessage());
		}
    	
    }
    
    
    
    
    
    
    
    
    /**
     * Method: getFileObjects()
     */
    public void testGetAllFileObjects() throws Exception {
        FileObject[] fileObjects = grandClassLoader.getAllFileObjects();
        assertEquals(3, fileObjects.length);
        for (FileObject fileObject : fileObjects) {
            assertEquals(true, fileObject.isExist());
        }
    }

    public void testGetResources() throws Exception {
        Enumeration<URL> urls = grandClassLoader.findResources("META-INF/MANIFEST.MF");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                //            System.out.println(url.toString());
            }
        }
        long end = System.currentTimeMillis();
        System.out.printf("time:%d\n", end - start);
    }
}

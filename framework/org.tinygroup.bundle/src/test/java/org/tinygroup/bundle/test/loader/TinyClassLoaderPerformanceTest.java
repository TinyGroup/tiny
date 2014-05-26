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
import java.io.FileFilter;
import java.net.URL;
import java.util.Enumeration;

import junit.framework.TestCase;

import org.tinygroup.bundle.loader.TinyClassLoader;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.impl.filter.FileExtNameFileObjectFilter;

/**
 * TinyClassLoader Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>02/25/2014</pre>
 */
public class TinyClassLoaderPerformanceTest extends TestCase {
    TinyClassLoader tinyClassLoader = null;

    public TinyClassLoaderPerformanceTest() {
    }

    public void setUp() throws Exception {
        super.setUp();
        File file = new File("src/test/resources");
        File[] files = file.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.isFile() && f.getName().endsWith("jar");
            }
        });
        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++) {
            urls[i] = files[i].toURL();
        }
        tinyClassLoader = new TinyClassLoader(urls);

    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Method: getFileObjects()
     */
    public void testGetFileObjects() throws Exception {
        FileObject[] fileObjects = tinyClassLoader.getFileObjects();
        assertEquals(6, fileObjects.length);
        for (FileObject fileObject : fileObjects) {
            assertEquals(true, fileObject.isExist());
        }
    }

    public void testGetManifectResources() throws Exception {
        Enumeration<URL> urls = tinyClassLoader.findResources("META-INF/MANIFEST.MF");
        for (int k = 1; k < 10; k++) {
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

    public void testGetClassResources() throws Exception {
        Enumeration<URL> urls = tinyClassLoader.findResources("org");
        for (int k = 1; k < 10; k++) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                int count = 0;
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    count++;
                    //System.out.println(url.toString());
                }
            }
            long end = System.currentTimeMillis();
            System.out.printf("time:%d\n", end - start);
        }
    }
    public void testFileObjectResources() throws Exception {
        FileObject[]fileObjects=tinyClassLoader.getFileObjects();
        long start = System.currentTimeMillis();
        for(FileObject fileObject:fileObjects){
            FileObjectFilter filter;
            fileObject.foreach(new FileExtNameFileObjectFilter("class"),new FileObjectProcessor() {
                public void process(FileObject fileObject) {
                    //System.out.println(fileObject.getPath());
                }
            });
        }
        long end = System.currentTimeMillis();
        System.out.printf("time:%d\n", end - start);
    }
    public void testFileObjectMFResources() throws Exception {
        FileObject[]fileObjects=tinyClassLoader.getFileObjects();
        long start = System.currentTimeMillis();
        for(FileObject fileObject:fileObjects){
            FileObjectFilter filter;
            fileObject.foreach(new FileExtNameFileObjectFilter("mf"),new FileObjectProcessor() {
                public void process(FileObject fileObject) {
                    System.out.println(fileObject.getPath());
                }
            });
        }
        long end = System.currentTimeMillis();
        System.out.printf("time:%d\n", end - start);
    }

}

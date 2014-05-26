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
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.impl.filter.FileExtNameFileObjectFilter;

/**
 * TinyClassLoader Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>02/25/2014</pre>
 */
public class TinyClassLoader1Test extends TestCase {
    TinyClassLoader tinyClassLoader = null;

    public TinyClassLoader1Test() {
    }

    public void setUp() throws Exception {
        super.setUp();
        URL[] urls0 = {new File("src/test/resources/org.tinygroup.loader-0.0.13-SNAPSHOT.jar-tests.jar").toURL()};
        URL[] urls1 = {new File("src/test/resources/org.tinygroup.loader-0.0.13-SNAPSHOT.jar1-tests.jar").toURL()};
        URL[] urls2 = {new File("src/test/resources/org.tinygroup.loader-0.0.13-SNAPSHOT.jar2-tests.jar").toURL()};
        tinyClassLoader = new TinyClassLoader(urls0);
        tinyClassLoader.addDependClassLoader(new TinyClassLoader(urls1,tinyClassLoader));
        tinyClassLoader.addDependClassLoader(new TinyClassLoader(urls2,tinyClassLoader));
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Method: getFileObjects()
     */
    public void testGetAllFileObjects() throws Exception {
        FileObject[] fileObjects = tinyClassLoader.getAllFileObjects();
        assertEquals(3, fileObjects.length);
        for (FileObject fileObject : fileObjects) {
            assertEquals(true, fileObject.isExist());
        }
    }

    public void testForEachByExtFileName() throws Exception {
        for (FileObject fileObject : tinyClassLoader.getFileObjects()) {
            fileObject.foreach(new FileExtNameFileObjectFilter("class"), new FileObjectProcessor() {
                public void process(FileObject fileObject) {
                    System.out.println(fileObject.getPath());
                }
            });
        }
    }
    public void testPrintMF() throws Exception {
        for (FileObject fileObject : tinyClassLoader.getFileObjects()) {
            fileObject.foreach(new FileExtNameFileObjectFilter("mf"), new FileObjectProcessor() {
                public void process(FileObject fileObject) {
                    System.out.println(fileObject.getPath());
                }
            });
        }
    }
    public void testFindResources() throws Exception {
        Enumeration<URL> enumerator = tinyClassLoader.findResources("META-INF/MANIFEST.MF");
        while(enumerator.hasMoreElements()){
            URL url=enumerator.nextElement();
            System.out.println(url.toString());
        }

    }
}

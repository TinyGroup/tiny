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
import org.eclipse.jdt.core.compiler.CompilationProgress;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by luoguo on 2014/5/21.
 */
public class ClassTest extends TestCase {
    public void test() {
        CompilationProgress progress = null; // instantiate your subclass
        String path = System.getProperty("user.dir");
        String filePath = path + File.separatorChar + "test1" + File.separatorChar + "org" + File.separatorChar + "tinygroup" + File.separatorChar + "Grade.java";
        String filePath1 = path
                + "" + File.separatorChar + "test1" + File.separatorChar + "org" + File.separatorChar + "tinygroup" + File.separatorChar + "MyTestInterface.java";
        boolean flag = BatchCompiler.compile(
                "-classpath rt.jar " + filePath + " " + filePath1,
                new PrintWriter(System.out), new PrintWriter(System.err),
                progress);
        assertTrue(flag);
    }
}

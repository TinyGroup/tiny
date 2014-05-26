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
package org.tinygroup.annotation.action;

import java.io.File;

public class FileTest {
    public static void main(String[] args) throws Exception {      

  System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));     

  System.out.println(FileTest.class.getClassLoader().getResource(""));        

  System.out.println(ClassLoader.getSystemResource(""));        
  System.out.println(FileTest.class.getResource(""));        
  System.out.println(FileTest.class.getResource("/")); //Class文件所在路径  
  System.out.println(new File("/").getAbsolutePath());        
  System.out.println(System.getProperty("user.dir"));    
 }
}
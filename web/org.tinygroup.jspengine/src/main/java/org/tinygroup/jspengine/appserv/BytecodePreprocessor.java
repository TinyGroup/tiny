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
/*
 * BytecodePreprocessor.java
 *
 * Created on May 22, 2002, 4:54 PM
 */

package org.tinygroup.jspengine.appserv;

import java.util.Hashtable;

/** Third party tool vendors may implement this interface to provide code
 * instrumentation to the application server.
 */
public interface BytecodePreprocessor {
    
    /** Initialize the profiler instance.  This method should be called exactly 
     * once before any calls to preprocess.
     * @param parameters Initialization parameters.
     * @return true if initialization succeeded.
     */    
    public boolean initialize(Hashtable parameters);
    
    /** This function profiler-enables the given class.  This method should not 
     * be called until the initialization method has completed.  It is thread-
     * safe.
     * @param classname The name of the class to process.  Used for efficient 
     * filtering.
     * @param classBytes Actual contents of class to process
     * @return The instrumented class bytes.
     */    
    public byte[] preprocess(String classname, byte[] classBytes);
    
}

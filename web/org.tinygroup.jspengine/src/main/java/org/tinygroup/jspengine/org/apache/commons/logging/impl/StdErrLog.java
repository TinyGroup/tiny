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
//========================================================================
//$Id: StdErrLog.java 3264 2008-07-16 05:58:33Z janb $
//Copyright 2006 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package org.tinygroup.jspengine.org.apache.commons.logging.impl;

import org.tinygroup.jspengine.org.apache.commons.logging.Log;

/**
 * Log
 * 
 * Bridges the com.sun.org.apache.commons.logging.Log to std err.
 *
 **/
public class StdErrLog implements Log
{
    private String _name;
    
    /**
     * 
     */
    public StdErrLog(String name)
    {
        _name = name;
    }
    public  void fatal (Object message)
    {
        System.err.println("FATAL: " + (message==null?"":message.toString()));
    }
    
    public  void fatal (Object message, Throwable t)
    {
        System.err.println("FATAL: "+(message==null?"":message.toString()));
        if (t!=null)
            t.printStackTrace();
    }
    
    public  void debug(Object message)
    {
        System.err.println("DEBUG: "+(message==null?"":message.toString()));
    }
    
    public  void debug (Object message, Throwable t)
    {
        System.err.println("DEBUG: "+(message==null?"":message.toString()));
        if (t!=null)
            t.printStackTrace();
    }
    
    public  void trace (Object message)
    {
        System.err.println("TRACE: "+(message==null?"":message.toString()));
    }
    
  
    public  void info(Object message)
    {
       System.err.println("INFO: "+(message==null?"":message.toString()));
    }

    public  void error(Object message)
    {
       System.err.println("ERROR: "+(message==null?"":message.toString()));
    }
    
    public  void error(Object message, Throwable cause)
    {
        System.err.println("ERROR: "+(message==null?"":message.toString()));
        if (cause!=null)
          cause.printStackTrace();
    }

    public  void warn(Object message)
    {
        System.err.println("WARN: "+(message==null?"":message.toString()));
    }
    
    public  boolean isDebugEnabled ()
    {
        return false;
    }
    
    public  boolean isWarnEnabled ()
    {
        return true;
    }
    
    public  boolean isInfoEnabled ()
    {
        return true;
    }
    
    
    public  boolean isErrorEnabled ()
    {
        return true;
    }
    
  
    public  boolean isTraceEnabled ()
    {
        return false;
    }
    
}

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
// ========================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.tinygroup.jspengine.org.apache.commons.logging;

import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.jspengine.org.apache.commons.logging.impl.StdErrLog;


/**
 * LogFactory
 *
 * Bridges com.sun.org.apache.commons.logging.LogFactory to
 * Jetty's log.
 *
 */
public class LogFactory
{
    private static Map _logs = new HashMap();
    private static String _clazzName = "org.tinygroup.sun.org.apache.commons.logging.impl.StdErrLog";
    private static Class _clazz = StdErrLog.class;
    private static Constructor _ctor;

    static
    {
        try
        {
            _ctor = _clazz.getDeclaredConstructor(new Class[]{String.class});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setLogImplClassName (String className)
    {
        if (className==null || className.trim().equals(""))
        {
            System.err.println("Ignoring null log impl");
            return;
        }

        try
        {
            _clazzName=className;
            _clazz = Thread.currentThread().getContextClassLoader().loadClass(_clazzName);
            _ctor = _clazz.getDeclaredConstructor(new Class[]{String.class});
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Log impl "+_clazzName+" not found on classpath, falling back to StdErrLog logger");
        }
        catch (NoSuchMethodException e)
        {
            System.err.println("No default constructor for "+className+", falling back to StdErrLog logger");
            _clazz = StdErrLog.class;
        }
    }
    
    public static Log getLog (Class c)
    {
        Log log = (Log)_logs.get(c.getName());
        if (log == null)
        {
            
            log = newLog(c.getName());
            _logs.put(c.getName(), log);
        }
            
        return log;
    }
    
    public static Log getLog (String str)
    {
        Log log = (Log)_logs.get(str);
        if (log == null)
        {
            log = newLog(str);
            _logs.put(str, log);
        }
        return log;
    }
    
    public static void release (URLClassLoader cl)
    {
        releaseAll ();
    }
    
    public static void releaseAll ()
    {
        _logs.clear();
    }

    private static Log newLog (String name)
    {
        Log log = null;
        try
        {
            log =  (Log)_ctor.newInstance(new Object[]{name});       
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage()+", falling back to StdErrLog");
            log = new StdErrLog(name);
        }
        return log;
    }
}

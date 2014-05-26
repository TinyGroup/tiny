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
package org.tinygroup.jspengine.org.apache.commons.logging.impl;

import org.tinygroup.jspengine.org.apache.commons.logging.Log;


public class NoOpLog implements Log
{

    /**
     * 
     */
    public NoOpLog()
    {
        super();
    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#fatal(java.lang.Object)
     */
    public void fatal(Object message)
    {
        //noop
    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#fatal(java.lang.Object, java.lang.Throwable)
     */
    public void fatal(Object message, Throwable t)
    {
    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#debug(java.lang.Object)
     */
    public void debug(Object message)
    {

    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#debug(java.lang.Object, java.lang.Throwable)
     */
    public void debug(Object message, Throwable t)
    {

    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#trace(java.lang.Object)
     */
    public void trace(Object message)
    {

    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#info(java.lang.Object)
     */
    public void info(Object message)
    {

    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#error(java.lang.Object)
     */
    public void error(Object message)
    {

    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#error(java.lang.Object, java.lang.Throwable)
     */
    public void error(Object message, Throwable cause)
    {

    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#warn(java.lang.Object)
     */
    public void warn(Object message)
    {

    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#isDebugEnabled()
     */
    public boolean isDebugEnabled()
    {
        return false;
    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#isWarnEnabled()
     */
    public boolean isWarnEnabled()
    {
        return false;
    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#isInfoEnabled()
     */
    public boolean isInfoEnabled()
    {
        return false;
    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#isErrorEnabled()
     */
    public boolean isErrorEnabled()
    {
        return false;
    }

    /**
     * @see org.tinygroup.jspengine.org.apache.commons.logging.Log#isTraceEnabled()
     */
    public boolean isTraceEnabled()
    {
        return false;
    }

}

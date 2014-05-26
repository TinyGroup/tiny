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

package org.tinygroup.jspengine.runtime;

/**
 * Interface for tracking the source files dependencies, for the purpose
 * of compiling out of date pages.  This is used for
 * 1) files that are included by page directives
 * 2) files that are included by include-prelude and include-coda in jsp:config
 * 3) files that are tag files and referenced
 * 4) TLDs referenced
 */

public interface JspSourceDependent {

   /**
    * Returns a list of files names that the current page has a source
    * dependency on.
    */
    /* GlassFish Issue 812
    public java.util.List getDependants();
    */
    // START GlassFish Issue 812
    // FIXME: Use java.lang.Object instead of java.util.List as return type
    // due to weird behavior with Eclipse JDT 3.1 in Java 5 mode
    public Object getDependants();
    // END GlassFish Issue 812
}

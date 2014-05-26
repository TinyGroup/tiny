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

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.VariableResolver;


/**
 * <p>This is the implementation of VariableResolver in JSP 2.0,
 * using ELResolver in JSP2.1.
 * It looks up variable references in the PageContext, and also
 * recognizes references to implicit objects.
 * 
 * @author Kin-man Chung
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: jluehe $
 **/

public class VariableResolverImpl
    implements VariableResolver
{
    private PageContext pageContext;

    //-------------------------------------
    /**
     * Constructor
     **/
    public VariableResolverImpl (PageContext pageContext) {
        this.pageContext = pageContext;
    }
  
    //-------------------------------------
    /**
     * Resolves the specified variable within the given context.
     * Returns null if the variable is not found.
     **/
    public Object resolveVariable (String pName)
            throws javax.servlet.jsp.el.ELException {

        ELContext elContext = pageContext.getELContext();
        ELResolver elResolver = elContext.getELResolver();
        try {
            return elResolver.getValue(elContext, null, pName);
        } catch (javax.el.ELException ex) {
            throw new javax.servlet.jsp.el.ELException();
        }
    }
}

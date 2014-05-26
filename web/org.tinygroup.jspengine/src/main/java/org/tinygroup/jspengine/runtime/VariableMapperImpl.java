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

import java.util.HashMap;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.VariableMapper;
import javax.el.ValueExpression;


/**
 * <p>This is the implementation of VariableMapper.
 * The compiler creates an empty variable mapper when an ELContext is created.
 * The variable mapper will be updated by tag handlers, if necessary.
 * 
 * @author Kin-man Chung
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: jluehe $
 **/

public class VariableMapperImpl extends VariableMapper
{
    //-------------------------------------
    /**
     * Constructor
     **/
    public VariableMapperImpl () {
        map = new HashMap();
    }
  
    //-------------------------------------
    /**
     * Resolves the specified variable within the given context.
     * Returns null if the variable is not found.
     **/
    public ValueExpression resolveVariable (String variable) {
        return (ValueExpression) map.get(variable);
    }

    public ValueExpression setVariable(String variable,
                                       ValueExpression expression) {
        ValueExpression prev = null;
        if (expression == null) {
            map.remove(variable);
        } else {
            prev = (ValueExpression) map.get(variable);
            map.put(variable, expression);
        }
        return prev;
    }

    private HashMap map;
}

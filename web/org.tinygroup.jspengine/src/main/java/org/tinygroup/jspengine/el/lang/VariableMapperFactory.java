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
package org.tinygroup.jspengine.el.lang;

import javax.el.ValueExpression;
import javax.el.VariableMapper;

public class VariableMapperFactory extends VariableMapper {

    private final VariableMapper target;
    private VariableMapper momento;
    
    public VariableMapperFactory(VariableMapper target) {
        if (target == null) {
            throw new NullPointerException("Target VariableMapper cannot be null");
        }
        this.target = target;
    }
    
    public VariableMapper create() {
        return this.momento;
    }

    public ValueExpression resolveVariable(String variable) {
        ValueExpression expr = this.target.resolveVariable(variable);
        if (expr != null) {
            if (this.momento == null) {
                this.momento = new VariableMapperImpl();
            }
            this.momento.setVariable(variable, expr);
        }
        return expr;
    }

    public ValueExpression setVariable(String variable, ValueExpression expression) {
        throw new UnsupportedOperationException("Cannot Set Variables on Factory");
    }
}

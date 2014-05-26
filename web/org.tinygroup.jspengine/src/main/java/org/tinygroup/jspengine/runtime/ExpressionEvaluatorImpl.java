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

import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.Expression;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

import org.tinygroup.jspengine.el.ExpressionFactoryImpl;

/**
 * <p>This is the implementation of ExpreesioEvaluator
 * using implementation of JSP2.1.
 * 
 * @author Kin-man Chung
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: jluehe $
 **/

public class ExpressionEvaluatorImpl extends ExpressionEvaluator
{
    private PageContext pageContext;

    //-------------------------------------
    /**
     * Constructor
     **/
    public ExpressionEvaluatorImpl (PageContext pageContext) {
        this.pageContext = pageContext;
    }
  
    //-------------------------------------
    public Expression parseExpression(String expression,
                                      Class expectedType,
                                      FunctionMapper fMapper )
            throws ELException {

        ExpressionFactory fac = new ExpressionFactoryImpl();
        javax.el.ValueExpression expr;
        ELContextImpl elContext = new ELContextImpl(null);
        javax.el.FunctionMapper fm = new FunctionMapperWrapper(fMapper);
        elContext.setFunctionMapper(fm);
        try {
            expr = fac.createValueExpression(
                           elContext,
                           expression, expectedType);
        } catch (javax.el.ELException ex) {
            throw new ELException(ex);
        }
        return new ExpressionImpl(expr, pageContext);
    }

     public Object evaluate(String expression,
                            Class expectedType,
                            VariableResolver vResolver,
                            FunctionMapper fMapper )
                throws ELException {

        ELContextImpl elContext;
        if (vResolver instanceof VariableResolverImpl) {
            elContext = (ELContextImpl) pageContext.getELContext();
        }
        else {
            // The provided variable Resolver is a custom resolver,
            // wrap it with a ELResolver 
            elContext = new ELContextImpl(new ELResolverWrapper(vResolver));
        }

        javax.el.FunctionMapper fm = new FunctionMapperWrapper(fMapper);
        elContext.setFunctionMapper(fm);
        ExpressionFactory fac = new ExpressionFactoryImpl();
        Object value;
        try {
            ValueExpression expr = fac.createValueExpression(
                                 elContext,
                                 expression,
                                 expectedType);
            value = expr.getValue(elContext);
        } catch (javax.el.ELException ex) {
            throw new ELException(ex);
        }
        return value;
    }

    static private class ExpressionImpl extends Expression {

        private ValueExpression valueExpr;
        private PageContext pageContext;

        ExpressionImpl(ValueExpression valueExpr,
                       PageContext pageContext) {
            this.valueExpr = valueExpr;
            this.pageContext = pageContext;
        }

        public Object evaluate(VariableResolver vResolver) throws ELException {

            ELContext elContext;
            if (vResolver instanceof VariableResolverImpl) {
                elContext = pageContext.getELContext();
            }
            else {
                // The provided variable Resolver is a custom resolver,
                // wrap it with a ELResolver 
                elContext = new ELContextImpl(new ELResolverWrapper(vResolver));
            }
            try {
                return valueExpr.getValue(elContext);
            } catch (javax.el.ELException ex) {
                throw new ELException(ex);
            }
        }
    }

    private static class FunctionMapperWrapper
        extends javax.el.FunctionMapper {

        private FunctionMapper mapper;

        FunctionMapperWrapper(FunctionMapper mapper) {
            this.mapper = mapper;
        }

        public java.lang.reflect.Method resolveFunction(String prefix,
                                                        String localName) {
            return mapper.resolveFunction(prefix, localName);
        }
    }

    private static class ELResolverWrapper extends ELResolver {
        private VariableResolver vResolver;

        ELResolverWrapper(VariableResolver vResolver) {
            this.vResolver = vResolver;
        }

        public Object getValue(ELContext context,
                               Object base,
                               Object property)
                throws javax.el.ELException {
            if (base == null) {
                context.setPropertyResolved(true);
                try {
                    return vResolver.resolveVariable(property.toString());
                } catch (ELException ex) {
                    throw new javax.el.ELException(ex);
                }
            }
            return null;
        }

        public Class getType(ELContext context,
                             Object base,
                             Object property)
                throws javax.el.ELException {
            return null;
        }

        public void setValue(ELContext context,
                             Object base,
                             Object property,
                             Object value)
                throws javax.el.ELException {
        }

        public boolean isReadOnly(ELContext context,
                                  Object base,
                                  Object property)
                throws javax.el.ELException {
            return false;
        }

        public Iterator getFeatureDescriptors(ELContext context,
                                              Object base) {
            return null;
        }

        public Class getCommonPropertyType(ELContext context,
                                           Object base) {
            return null;
        }
    }
}

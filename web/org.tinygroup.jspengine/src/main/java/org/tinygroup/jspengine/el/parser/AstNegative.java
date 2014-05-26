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
package org.tinygroup.jspengine.el.parser;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.el.ELException;

import org.tinygroup.jspengine.el.lang.EvaluationContext;


/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: tcfujii $
 */
public final class AstNegative extends SimpleNode {
    public AstNegative(int id) {
        super(id);
    }

    public Class getType(EvaluationContext ctx)
            throws ELException {
        return Number.class;
    }

    public Object getValue(EvaluationContext ctx)
            throws ELException {
        Object obj = this.children[0].getValue(ctx);

        if (obj == null) {
            return Long.valueOf(0);
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).negate();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).negate();
        }
        if (obj instanceof String) {
            if (isStringFloat((String) obj)) {
                return Double.valueOf(-Double.parseDouble((String) obj));
            }
            return Long.valueOf(-Long.parseLong((String) obj));
        }
        Class type = obj.getClass();
        if (obj instanceof Long || Long.TYPE == type) {
            return Long.valueOf(-((Long) obj).longValue());
        }
        if (obj instanceof Double || Double.TYPE == type) {
            return Double.valueOf(-((Double) obj).doubleValue());
        }
        if (obj instanceof Integer || Integer.TYPE == type) {
            return Integer.valueOf(-((Integer) obj).intValue());
        }
        if (obj instanceof Float || Float.TYPE == type) {
            return Float.valueOf(-((Float) obj).floatValue());
        }
        if (obj instanceof Short || Short.TYPE == type) {
            return Short.valueOf((short) -((Short) obj).shortValue());
        }
        if (obj instanceof Byte || Byte.TYPE == type) {
            return Byte.valueOf((byte) -((Byte) obj).byteValue());
        }
        Long num = (Long) coerceToNumber(obj, Long.class);
        return Long.valueOf(-num.longValue());
    }
}

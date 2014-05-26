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
package org.tinygroup.weblayer.webcontext.session.valueencode.impl;

import static org.tinygroup.commons.tools.Assert.assertTrue;
import static org.tinygroup.commons.tools.CollectionUtil.createLinkedHashMap;

import java.util.Map;

import org.tinygroup.weblayer.webcontext.session.valueencode.AbstractSessionValueEncoder;
import org.tinygroup.weblayer.webcontext.util.QueryStringParser;
import org.springframework.beans.TypeConverter;

/**
 * 将一个<code>Map</code>编码成字符串，或反之。支持加密。
 *
 * @author Michael Zhou
 */
public class MappedValuesEncoder extends AbstractSessionValueEncoder {
    private Class<?> valueType;

    public void setValueType(Class<?> type) {
        this.valueType = type;
    }

    
    protected boolean doURLEncode() {
        return false;
    }

    /** 复杂值压缩，取得的字符串较短。 */
    
    protected boolean doCompress() {
        return true;
    }

    protected String getEqualSign() {
        return ":";
    }

    
    protected String encodeValue(Object value) throws Exception {
        assertTrue(value instanceof Map, "wrong session attribute type: " + value.getClass());

        Map<?, ?> map = (Map<?, ?>) value;
        Map<String, String> encodedMap = createLinkedHashMap();
        TypeConverter converter = getTypeConverter();

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String encodedValue = convertToString(valueType, entry.getValue(), converter);

            encodedMap.put(key, encodedValue);
        }

        return new QueryStringParser(getCharset()).setEqualSign(getEqualSign()).append(encodedMap).toQueryString();
    }

    
    protected Object decodeValue(String encodedValue) throws Exception {
        final Map<String, Object> map = createLinkedHashMap();
        final TypeConverter converter = getTypeConverter();

        new QueryStringParser(getCharset()) {
            
            protected void add(String key, String encodedValue) {
                map.put(key, convertToType(valueType, encodedValue, converter));
            }
        }.setEqualSign(getEqualSign()).parse(encodedValue);

        return map;
    }

   }

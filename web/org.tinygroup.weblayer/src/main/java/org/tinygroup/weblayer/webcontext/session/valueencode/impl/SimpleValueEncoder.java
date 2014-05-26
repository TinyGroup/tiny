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

import org.tinygroup.weblayer.webcontext.session.valueencode.AbstractSessionValueEncoder;

/**
 * 将一个简单类型编码成字符串，或反之。支持加密。
 *
 * @author renhui
 */
public class SimpleValueEncoder extends AbstractSessionValueEncoder {
    private Class<?> type;

    public void setType(Class<?> type) {
        this.type = type;
    }

    
    protected boolean doURLEncode() {
        return true;
    }

    /** 简单值不压缩，取得的字符串较短。 */
    
    protected boolean doCompress() {
        return false;
    }

    
    protected String encodeValue(Object value) throws Exception {
        return convertToString(type, value, getTypeConverter());
    }

    
    protected Object decodeValue(String encodedValue) throws Exception {
        return convertToType(type, encodedValue, getTypeConverter());
    }


}

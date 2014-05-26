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
package org.tinygroup.weblayer.webcontext.session.impl;

import java.util.Map;
import static org.tinygroup.commons.tools.CollectionUtil.*;
import org.tinygroup.commons.tools.ClassUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.webcontext.session.SessionConfig;

/**
 * 用来控制session attributes的使用。
 *
 * @author renhui
 */
public class SessionAttributeWhitelist extends AbstractSessionAttributeAccessController {
	private final        Logger log                       = LoggerFactory.getLogger(SessionAttributeWhitelist.class);
    private Map<String, Class<?>> allowedAttributes;


    public void setAllowedAttributes(Map<String, Class<?>> allowedAttributes) {
        this.allowedAttributes = allowedAttributes;
    }

    
    public void init(SessionConfig sessionConfig) {
        super.init(sessionConfig);

        if (allowedAttributes == null) {
            allowedAttributes = createHashMap();
        }

        for (Map.Entry<String, Class<?>> entry : allowedAttributes.entrySet()) {
            if (entry.getValue() == null) {
                entry.setValue(Object.class);
            } else {
                entry.setValue(ClassUtil.getWrapperTypeIfPrimitive(entry.getValue()));
            }
        }
    }

    
    protected boolean allowForAttribute(String name, Class<?> type) {
        Class<?> allowedType = allowedAttributes.get(name);

        if (allowedType == null) {
            return false;
        }

        if (type == null) {
            return true;
        }

        return allowedType.isAssignableFrom(type);
    }

    
    protected Object readInvalidAttribute(String name, Object value) {
        log.getLogger().warn("Attribute to read is not in whitelist: name={}, type={}", name,
                             value == null ? "unknwon" : value.getClass().getName());

        return value;
    }

    
    protected Object writeInvalidAttribute(String name, Object value) {
        log.getLogger().warn("Attribute to write is not in whitelist: name={}, type={}", name,
                             value == null ? "unknwon" : value.getClass().getName());

        return value;
    }
}

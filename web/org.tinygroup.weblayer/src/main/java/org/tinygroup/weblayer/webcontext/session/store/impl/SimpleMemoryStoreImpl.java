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
package org.tinygroup.weblayer.webcontext.session.store.impl;

import static java.util.Collections.emptyList;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.weblayer.webcontext.session.SessionConfig;
import org.tinygroup.weblayer.webcontext.session.SessionStore;

/**
 * 存放在内存中的session attribute的机制。
 *
 * @author renhui
 */
public class SimpleMemoryStoreImpl implements SessionStore {
    private final ConcurrentMap<String, Map<String, Object>> sessions = CollectionUtil.createConcurrentHashMap();

    /** 初始化SessionStore。 */
    public void init(String storeName, SessionConfig sessionConfig) {
    }

    /** 取得指定session ID的所有值。 */
    public Map<String, Object> getSession(String sessionID) {
        return sessions.get(sessionID);
    }

    /** 取得指定session的所有attribute名称。 */
    public Iterable<String> getAttributeNames(String sessionID, StoreContext storeContext) {
        Map<String, Object> sessionData = sessions.get(sessionID);

        if (sessionData == null) {
            return emptyList();
        } else {
            return sessionData.keySet();
        }
    }

    /** 装载指定session的某个attribute。 */
    public Object loadAttribute(String attrName, String sessionID, StoreContext storeContext) {
        Map<String, Object> sessionData = sessions.get(sessionID);

        if (sessionData == null) {
            return null;
        } else {
            return sessionData.get(attrName);
        }
    }

    /** 丢弃指定session ID的所有内容。 */
    public void invaldiate(String sessionID, StoreContext storeContext) {
        sessions.remove(sessionID);
    }

    /** 保存指定session的attributes。attrs为<code>null</code>表示删除。 */
    public void commit(Map<String, Object> modifiedAttrs, String sessionID, StoreContext storeContext) {
        Map<String, Object> sessionData = null;

        if (!sessions.containsKey(sessionID)) {
            sessionData = CollectionUtil.createConcurrentHashMap();
            sessions.putIfAbsent(sessionID, sessionData);
        }

        sessionData = sessions.get(sessionID);

        Assert.assertNotNull(sessionData, "sessionData for ID: %s", sessionID);

        for (Map.Entry<String, Object> entry : modifiedAttrs.entrySet()) {
            String attrName = entry.getKey();
            Object attrValue = entry.getValue();

            if (attrValue == null) {
                sessionData.remove(attrName);
            } else {
                sessionData.put(attrName, attrValue);
            }
        }
    }

    
    public String toString() {
        return "SimpleMemoryStore[" + sessions.size() + " sessions]";
    }
}

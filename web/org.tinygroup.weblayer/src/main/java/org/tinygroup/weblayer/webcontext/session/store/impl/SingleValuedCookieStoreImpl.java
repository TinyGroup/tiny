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

import static org.tinygroup.commons.tools.ArrayUtil.*;
import static org.tinygroup.commons.tools.Assert.*;
import static org.tinygroup.commons.tools.CollectionUtil.*;
import static org.tinygroup.commons.tools.StringUtil.*;

import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.tinygroup.commons.tools.ToStringBuilder;
import org.tinygroup.commons.tools.ToStringBuilder.MapBuilder;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.weblayer.webcontext.session.ExactMatchesOnlySessionStore;
import org.tinygroup.weblayer.webcontext.session.store.AbstractCookieStore;
import org.tinygroup.weblayer.webcontext.session.valueencode.SessionValueEncoder;
import org.tinygroup.weblayer.webcontext.session.valueencode.impl.SimpleValueEncoder;

/**
 * 将Session状态保存在cookie中。
 * <ul>
 * <li>每个store只能保存一个值。</li>
 * <li>将仅有的session attribute value用<code>SessionValueEncoder</code>编码成字符串。</li>
 * </ul>
 *
 * @author Michael Zhou
 */
public class SingleValuedCookieStoreImpl extends AbstractCookieStore implements ExactMatchesOnlySessionStore {
    private String[]              attrNames;
    private SessionValueEncoder[] encoders;

    public void initAttributeNames(String[] attrNames) {
        this.attrNames = attrNames;
        assertTrue(attrNames.length <= 1, "Session store %s supports only 1 mapping to attribute name", getName());
    }

    public void setValueEncoders(SessionValueEncoder[] encoders) {
        this.encoders = encoders;
    }

    
    protected void init() throws Exception {
        if (isEmptyArray(encoders)) {
            encoders = new SessionValueEncoder[] { createDefaultSessionValueEncoder() };
        }
    }

    protected SessionValueEncoder createDefaultSessionValueEncoder() throws Exception {
        SimpleValueEncoder simpleValueEncoder = new SimpleValueEncoder();
        simpleValueEncoder.afterPropertiesSet();
        return simpleValueEncoder;
    }

    public Iterable<String> getAttributeNames(String sessionID, StoreContext storeContext) {
        State state = getState(storeContext);
        return state.attributes.keySet();
    }

    public Object loadAttribute(String attrName, String sessionID, StoreContext storeContext) {
        State state = getState(storeContext);
        return state.attributes.get(attrName);
    }

    public void invaldiate(String sessionID, StoreContext storeContext) {
        State state = getState(storeContext);

        if (!isSurvivesInInvalidating()) {
            state.attributes.clear();
        }
    }

    public void commit(Map<String, Object> modifiedAttrs, String sessionID, StoreContext storeContext) {
        State state = getState(storeContext);

        if (state.cookieCommitted) {
            return;
        }

        state.cookieCommitted = true;

        String attrName = attrNames[0];

        if (modifiedAttrs.containsKey(attrName)) {
            Object attrValue = modifiedAttrs.get(attrName);

            if (attrValue == null) {
                log.logMessage(LogLevel.DEBUG, "Remove from session: {}", attrName);
                state.attributes.remove(attrName);
            } else {
                    log.logMessage(LogLevel.DEBUG, "Set to session: {} = {}", attrName, attrValue);

                state.attributes.put(attrName, attrValue);
            }
        }

        String cookieState = null;

        if (!state.attributes.isEmpty()) {
            try {
                cookieState = encoders[0].encode(state.attributes.get(attrName), storeContext);
            } catch (Exception e) {
                log.logMessage(LogLevel.WARN, "Failed to encode session state", e);
            }
        }

        cookieState = trimToEmpty(cookieState);

        writeCookie(storeContext.getSessionWebContext().getResponse(), getName(), cookieState);
    }

    /** 取得cookie store的状态。 */
    private State getState(StoreContext storeContext) {
        State state = (State) storeContext.getState();

        if (state == null) {
            state = new State();
            storeContext.setState(state);
        }

        ensureCookieLoading(state, storeContext.getSessionWebContext().getRequest(), storeContext);
        return state;
    }

    /** 确保cookie被装载。 */
    private void ensureCookieLoading(State state, HttpServletRequest request, StoreContext storeContext) {
        if (state.cookieLoaded) {
            return;
        }

        state.cookieLoaded = true;

        // 读取cookie
        state.requestCookieValue = readCookie(request);

        // 依次使用所有encoders，试着对cookieValue解码，如果失败，则返回空表
        // 如果成功，则返回单值map。
        state.attributes = decodeCookieValue(state.requestCookieValue, storeContext);
    }

    /** 读取cookies。 */
    private String readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            cookies = new Cookie[0];
        }

        // 扫描cookie。
        String cookieValue = null;

        for (Cookie cookie : cookies) {
            String cookieName = cookie.getName();

            if (getName().equals(cookieName)) {
                cookieValue = cookie.getValue();

                    log.logMessage(LogLevel.DEBUG, "[{}] Loading cookie: {}[length={}]={}", new Object[] { getStoreName(), getName(),
                                                                                      cookie.getValue().length(), cookie.getValue() });

                break;
            }
        }

        return cookieValue;
    }

    private Map<String, Object> decodeCookieValue(String cookieValue, StoreContext storeContext) {
        Map<String, Object> attrs = createHashMap(4);

        if (cookieValue == null) {
            return attrs; // empty map
        }

        List<Exception> encoderExceptions = null;

        for (SessionValueEncoder encoder : encoders) {
            try {
                attrs.put(attrNames[0], encoder.decode(cookieValue, storeContext));
                log.logMessage(LogLevel.DEBUG, "Succeeded decoding cookieValue using {}", encoder);
                break;
            } catch (Exception e) {
                if (encoderExceptions == null) {
                    encoderExceptions = createLinkedList();
                }

                encoderExceptions.add(e);
                log.logMessage(LogLevel.TRACE, "Failure decoding cookieValue using {}: {}", encoder, e.getMessage());
            }
        }

        // 如果失败，记录日志
        if (attrs.isEmpty() && encoderExceptions != null) {
                ToStringBuilder buf = new ToStringBuilder();

                buf.append("Failed to decode cookie value: ").append(cookieValue);

                int i = 0;
                for (Exception e : encoderExceptions) {
                    buf.format("\n  Encoder #%d - %s threw %s", i + 1, encoders[i].getClass().getSimpleName(), e);
                }

                log.logMessage(LogLevel.WARN, buf.toString());
        } else {
                int attrCount = attrs.size();
                ToStringBuilder buf = new ToStringBuilder();

                buf.format("Found %d attributes:", attrCount);

                if (!attrs.isEmpty()) {
                    buf.append(new MapBuilder().setPrintCount(true).setSortKeys(true).appendAll(attrs));
                }

                log.logMessage(LogLevel.DEBUG, buf.toString());
        }

        return attrs;
    }

    
    protected void toString(MapBuilder mb) {
        super.toString(mb);
        mb.append("encoders", encoders);
    }

    /** 存放cookie的状态。 */
    private class State {
        private boolean             cookieLoaded;
        private boolean             cookieCommitted;
        private String              requestCookieValue;
        private Map<String, Object> attributes;
    }
}

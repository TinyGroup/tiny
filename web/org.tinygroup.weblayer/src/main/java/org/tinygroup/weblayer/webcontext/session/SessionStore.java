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
package org.tinygroup.weblayer.webcontext.session;

import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 * 用来持久化存储session attribute的机制。
 *
 * @author renhui
 */
public interface SessionStore {
    /** 初始化SessionStore。 */
    void init(String storeName, SessionConfig sessionConfig) throws Exception;

    /**
     * 取得指定session的所有attribute名称。
     *
     * @param sessionID    要装载的session ID
     * @param storeContext 用来取得request信息，并存放store当前的状态
     * @return attributes的列表
     */
    Iterable<String> getAttributeNames(String sessionID, StoreContext storeContext);

    /**
     * 装载指定session的某个attribute。
     *
     * @param attrName     要装载的attribute名称
     * @param sessionID    要存取的session ID
     * @param storeContext 用来取得request信息，并存放store当前的状态
     * @return attribute的值（如果存在的话）
     */
    Object loadAttribute(String attrName, String sessionID, StoreContext storeContext);

    /**
     * 丢弃指定session ID的所有内容。
     *
     * @param sessionID    要丢弃的session ID
     * @param storeContext 用来取得request信息，并存放store当前的状态
     */
    void invaldiate(String sessionID, StoreContext storeContext);

    /**
     * 保存指定session的attributes。
     *
     * @param modifiedAttrs 要保存的attrs，如果值为<code>null</code>表示删除
     * @param sessionID     要保存的sessionID
     * @param storeContext  用来取得request信息，并存放store当前的状态
     */
    void commit(Map<String, Object> modifiedAttrs, String sessionID, StoreContext storeContext);

    /** 帮助store取得当前request的信息，并存放它们自己的当前状态。 */
    interface StoreContext {
        /**
         * 取得store的request scope状态数据。
         *
         * @return 状态值
         */
        Object getState();

        /**
         * 设置store的request scope状态数据。
         *
         * @param stateObject 状态值
         */
        void setState(Object stateObject);

        /**
         * 取得指定名称的store的状态数据。
         *
         * @param storeName store名称
         * @return 状态值
         */
        StoreContext getStoreContext(String storeName);

        /**
         * 取得当前的request context。
         *
         * @return <code>SessionWebContext</code>对象
         */
        SessionWebContext getSessionWebContext();

        /**
         * 取得当前的session对象。
         *
         * @return <code>HttpSession</code>对象
         */
        HttpSession getHttpSession();
    }
}

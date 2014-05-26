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

/**
 * session属性读写操作时的拦截器
 * @author renhui
 *
 */
public interface SessionAttributeInterceptor extends SessionInterceptor {
    /**
     * 从session中取得数据后，方法被调用。
     * <p>
     * 方法可以修改数据，并将修改后的值返回给调用者。
     * </p>
     */
    Object onRead(String name, Object value);

    /**
     * 将值设置到session中之前，方法被调用。
     * <p>
     * 方法返回的值，将被设置到session中。
     * </p>
     * <p>
     * 值为<code>null</code>表示将要从session中删除该条数据。
     * </p>
     * <p>
     * 假如方法抛出异常，那么没有任何值会被设入session中。
     * </p>
     */
    Object onWrite(String name, Object value);
}

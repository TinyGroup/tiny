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
package org.tinygroup.context;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 把Context包装成Map
 *
 * @author luoguo
 */
public class Context2Map implements Map<String, Object> {
    private Context context = null;

    public Context2Map(Context context) {
        this.context = context;
    }

    public int size() {
        return notSupportMethod();
    }

    private int notSupportMethod() {
        throw getRuntimeException();
    }

    private RuntimeException getRuntimeException() {
        return new RuntimeException("This method is not supported.");
    }

    public boolean isEmpty() {
        throw getRuntimeException();
    }

    public boolean containsKey(Object key) {
        return context.get(key.toString()) != null;
    }

    public boolean containsValue(Object value) {
        throw getRuntimeException();
    }

    public Object get(Object key) {
        return context.get(key.toString());
    }

    public Object put(String key, Object value) {
        context.put(key, value);
        return value;
    }

    public Object remove(Object key) {
        throw getRuntimeException();
    }


    public void putAll(Map<? extends String, ?> map) {
        Iterator<? extends Entry<? extends String, ?>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<? extends String, ?> entry = iterator.next();
            context.put(entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        throw getRuntimeException();
    }

    public Set<String> keySet() {
        throw getRuntimeException();
    }

    public Collection<Object> values() {
        throw getRuntimeException();
    }

    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        throw getRuntimeException();
    }

}

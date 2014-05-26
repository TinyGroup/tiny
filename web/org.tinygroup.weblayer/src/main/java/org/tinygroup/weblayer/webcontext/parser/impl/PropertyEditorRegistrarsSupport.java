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
package org.tinygroup.weblayer.webcontext.parser.impl;

import static java.util.Collections.emptyList;
import static org.tinygroup.commons.tools.CollectionUtil.createArrayList;

import java.util.Iterator;
import java.util.List;

import org.tinygroup.commons.tools.ToStringBuilder;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * 代表一个property editor registrar的集合，然而它本身也是一个
 * <code>PropertyEditorRegistrar</code>。
 *
 * @author Michael Zhou
 */
public class PropertyEditorRegistrarsSupport implements PropertyEditorRegistrar, Iterable<PropertyEditorRegistrar> {
    private List<PropertyEditorRegistrar> propertyEditorRegistrars = emptyList();

    /**
     * 取得一组<code>PropertyEditor</code>注册器。
     * <p>
     * <code>PropertyEditor</code>负责将字符串值转换成bean property的类型，或反之。
     * </p>
     */
    public PropertyEditorRegistrar[] getPropertyEditorRegistrars() {
        return propertyEditorRegistrars.toArray(new PropertyEditorRegistrar[propertyEditorRegistrars.size()]);
    }

    /**
     * 设置一组<code>PropertyEditor</code>注册器。
     * <p>
     * <code>PropertyEditor</code>负责将字符串值转换成bean property的类型，或反之。
     * </p>
     */
    public void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars) {
        this.propertyEditorRegistrars = createArrayList(propertyEditorRegistrars);
    }

    /** 在registry中注册自定义的<code>PropertyEditor</code>。 */
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        for (PropertyEditorRegistrar registrar : getPropertyEditorRegistrars()) {
            registrar.registerCustomEditors(registry);
        }
    }

    /** 查看有几个registrars。 */
    public int size() {
        return propertyEditorRegistrars.size();
    }

    /** 遍历registrars。 */
    public Iterator<PropertyEditorRegistrar> iterator() {
        return propertyEditorRegistrars.iterator();
    }

    
    public String toString() {
        return new ToStringBuilder().append(getClass().getSimpleName()).append(propertyEditorRegistrars).toString();
    }
}

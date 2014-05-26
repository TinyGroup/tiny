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
package org.tinygroup.tinyioc;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by luoguo on 13-12-27.
 */
public interface BeanContainer {
    <T> void registerClass(Class<T> clazz);

    <T> T getBeanByName(String name);

    <T> T getBeanByType(String type);

    <T> T getBeanByType(Class<T> clazz);

    <T> List<T> getBeanList(String type);

    <T> List<T> getBeanList(Class<T> clazz);

    <T> Set<T> getBeanSet(String type);

    <T> Set<T> getBeanSet(Class<T> clazz);

    <T> Collection<T> getBeanCollection(String type);

    <T> Collection<T> getBeanCollection(Class<T> clazz);

    boolean isExistBeanByName(String name);

    boolean isExistBeanByType(String type);

    boolean isExistBeanByType(Class clazz);

    void addTypeConverter(TypeConverter typeConverter);

    void addAop(AopDefine aopDefine);

    void setParent(BeanContainer beanContainer);

    void addBeanContainer(BeanContainer beanContainer);

    void removeBeanContainer(BeanContainer beanContainer);

    ClassLoader getClassLoader();
}

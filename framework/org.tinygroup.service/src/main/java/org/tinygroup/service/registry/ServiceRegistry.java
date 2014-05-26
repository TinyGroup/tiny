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
package org.tinygroup.service.registry;

import org.tinygroup.service.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 服务注册表，提供对服务注册表的各项操作 每种执行器都有自己的服务注册表
 *
 * @author luoguo
 */
public interface ServiceRegistry {
    String BEAN_NAME = "serviceRegistry";

    /**
     * 注册服务，重复注册时旧的被替换
     *
     * @param serviceRegistryItem
     */
    void registeService(ServiceRegistryItem serviceRegistryItem);

    /**
     * 返回指定服务的服务注册信息
     *
     * @param service
     * @return
     */
    ServiceRegistryItem getServiceRegistryItem(Service service);

    /**
     * 注册一批服务，重复注册时旧的被替换
     *
     * @param serviceRegistryItems
     */
    void registeService(List<ServiceRegistryItem> serviceRegistryItems);

    /**
     * 注册一批服务，重复注册时旧的被替换
     *
     * @param serviceRegistryItems
     */

    void registeService(ServiceRegistryItem[] serviceRegistryItems);

    /**
     * 注册一批服务，重复注册时旧的被替换
     *
     * @param serviceRegistryItems
     */
    void registeService(Set<ServiceRegistryItem> serviceRegistryItems);

    /**
     * 删除服务
     *
     * @param serviceId
     */
    void removeService(String serviceId);


    /**
     * 返回注册服务个数
     *
     * @return
     */
    int size();

    /**
     * 清空所有服务
     */
    void clear();


    /**
     * 查找指定服务对应的注册项
     *
     * @param serviceId
     * @return
     */
    ServiceRegistryItem getServiceRegistryItem(String serviceId);


    /**
     * 获取所有服务注册项
     *
     * @return 所有的服务注册项
     */
    Collection<ServiceRegistryItem> getServiceRegistryItems();
}

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
package org.tinygroup.weblayer;

import org.tinygroup.weblayer.configmanager.TinyFiterConfigManager;

import java.util.List;

/**
 * 代表一组tinyfilter信息
 *
 * @author renhui
 */
public interface TinyFilterManager extends TinyWebResourceManager {

    String TINY_FILTER_MANAGER = "tinyFilterManager";

    /**
     * 根据请求url，获取相关的tinyfilter列表
     *
     * @param url
     * @return
     */
    List<TinyFilter> getTinyFiltersWithUrl(String url);

    /**
     * 设置web资源（servlet或filter）的配置管理接口
     *
     * @param configManager
     */
    void setConfigManager(TinyFiterConfigManager configManager);

    /**
     * 是不是filter包装模式，配置的tiny-filter是否实现了FilterWrapper接口如果是，则采用fiter包装方式来处理
     *
     * @return
     */
    boolean existFilterWrapper();

    /**
     * 获取filter包装实例
     *
     * @return
     */
    FilterWrapper getFilterWrapper();

}

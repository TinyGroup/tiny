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

import org.tinygroup.weblayer.configmanager.TinyProcessorConfigManager;


/**
 * tiny servlet 处理器管理接口
 *
 * @author renhui
 */
public interface TinyProcessorManager extends TinyWebResourceManager {

    String TINY_PROCESSOR_MANAGER = "tinyProcessorManager";
    String XSTEAM_PACKAGE_NAME = "weblayer";

    /**
     * 根据请求路径,获取servlet处理器进行逻辑处理
     *
     * @param url     请求路径
     * @param context 请求环境对象
     */
    boolean execute(String url, WebContext context);


    /**
     * 设置web资源（servlet或filter）的配置管理接口
     *
     * @param configManager
     */
    void setConfigManager(
            TinyProcessorConfigManager configManager);

}
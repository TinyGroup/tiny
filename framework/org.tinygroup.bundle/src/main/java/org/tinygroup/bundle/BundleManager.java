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
package org.tinygroup.bundle;

import org.tinygroup.bundle.config.BundleDefine;
import org.tinygroup.bundle.loader.TinyClassLoader;

import java.util.List;

/**
 * Bundle管理器
 */
public interface BundleManager extends SingleBundleManager, BatchBundleManager {
	String BEAN_NAME = "bundleManager";
    /**
     * 添加一个杂物箱
     *
     * @param bundleDefine
     */
    void addBundleDefine(BundleDefine bundleDefine);

    /**
     * 根据名字返回BundleDefine
     *
     * @param name
     * @return
     */
    BundleDefine getBundleDefine(String name) throws BundleException;

    /**
     * 删除一个杂物箱定义，删除一个杂物箱定义时，如果其对应的Bundle已经起动则会默认停止之，同时依赖它的其它杂物箱也会被停止
     *
     * @param bundleDefine
     */
    void removeBundle(BundleDefine bundleDefine) throws BundleException;

    /**
     * 设置杂物箱根目录
     *
     * @param path
     */
    void setBundleRoot(String path);

    /**
     * 设置杂物箱公共库目录
     *
     * @param path
     */
    void setCommonRoot(String path);

    /**
     * 返回杂物箱公共目录
     *
     * @return
     */
    String getBundleRoot();

    /**
     * 返回杂物箱公共目录
     *
     * @return
     */
    String getCommonRoot();

    /**
     * 插件上下文传递接口
     *
     * @return
     */
    BundleContext getBundleContext();

    /**
     * 返回杂物箱管理器类加载器，它作为一个容器引用了所有启动的Bundle的类加载器
     *
     * @return
     */
    TinyClassLoader getTinyClassLoader();
    
    
    TinyClassLoader getTinyClassLoader(String bundle);
    /**
     * 设置启动前事件处理器
     *
     * @param bundleEvents
     */
    void setBeforeStartBundleEvent(List<BundleEvent> bundleEvents);

    /**
     * 设置启动后事件处理器
     *
     * @param bundleEvents
     */

    void setAfterStartBundleEvent(List<BundleEvent> bundleEvents);

    /**
     * 设置停止前事件处理器
     *
     * @param bundleEvents
     */
    void setBeforeStopBundleEvent(List<BundleEvent> bundleEvents);

    /**
     * 设置停止后事件处理器
     *
     * @param bundleEvents
     */

    void setAfterStopBundleEvent(List<BundleEvent> bundleEvents);

    /**
     * 返回指定BundleDefine对应的TinyClassLoader
     *
     * @param bundleDefine
     * @return
     */
    TinyClassLoader getTinyClassLoader(BundleDefine bundleDefine);
}

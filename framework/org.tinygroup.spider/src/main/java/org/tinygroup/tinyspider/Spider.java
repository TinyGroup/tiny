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
package org.tinygroup.tinyspider;

import java.util.Map;

/**
 * 网络爬虫
 *
 * @author luoguo
 */
public interface Spider {
    void setResponseCharset(String charset);

    String getResponseCharset();

    /**
     * 添加站点访问器
     *
     * @param siteVisitor
     */
    void addSiteVisitor(SiteVisitor siteVisitor);

    /**
     * 添加监视器
     *
     * @param watcher
     */
    void addWatcher(Watcher watcher);

    /**
     * 处理url
     *
     * @param url
     */
    void processUrl(String url);

    /**
     * 处理url
     *
     * @param url
     * @param parameter
     */
    void processUrl(String url, Map<String, Object> parameter);

    /**
     * 设置URL仓库
     *
     * @param urlRepository
     */
    void setUrlRepository(UrlRepository urlRepository);
}

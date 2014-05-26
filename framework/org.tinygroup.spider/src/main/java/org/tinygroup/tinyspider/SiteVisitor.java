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
 * 网站访问者
 *
 * @author luoguo
 */
public interface SiteVisitor {
    String VISIT_MODE_GET = "get";
    String VISIT_MODE_POST = "post";

    /**
     * 设置提交方式
     *
     * @param visitMode
     */
    void setVisitMode(String visitMode);

    /**
     * 是否匹配
     *
     * @param url
     * @return
     */
    boolean isMatch(String url);

    /**
     * 返回内容
     *
     * @param url
     * @return
     */
    String getContent(String url, String charset);

    /**
     * 返回内容
     *
     * @param url
     * @param parameter
     * @return
     */
    String getContent(String url, Map<String, Object> parameter, String charset);
}

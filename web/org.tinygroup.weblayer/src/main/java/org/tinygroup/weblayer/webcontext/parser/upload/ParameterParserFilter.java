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
package org.tinygroup.weblayer.webcontext.parser.upload;

import javax.servlet.http.HttpServletRequest;

/**
 * 用来拦截和修改用户提交的数据。
 *
 * @author renhui
 */
public interface ParameterParserFilter {
    /**
     * 是否需要过滤，如果返回否，则对于该请求的所有参数均不执行该过滤器。
     * <p>
     * 有些filter可以根据URL来确定是否要过滤参数。
     * </p>
     */
    boolean isFiltering(HttpServletRequest request);
}

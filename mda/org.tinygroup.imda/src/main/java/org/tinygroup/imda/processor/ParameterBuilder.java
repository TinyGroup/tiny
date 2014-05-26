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
package org.tinygroup.imda.processor;

import org.tinygroup.context.Context;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;

/**
 * 参数构建器<br>
 * <p/>
 * 利用上下文构建服务调用时的参数
 *
 * @author luoguo
 */
public interface ParameterBuilder<T> {

    String PAGE_SIZE = "pageSize";
    String PAGE_NUMBER = "pageNumber";
    String ORDER_BY_FIELD = "sortField";//多个以逗号分隔
    String SORT_DIRECTION = "sortDirection";
    String GROUP_BY_FIELD = "groupByField";

    /**
     * 检查并构建参数，如果参数已经存在，则不用管，如果参数不存在，则需要构建之
     *
     * @param modelRequestInfo
     * @param context
     */
    Context buildParameter(ModelRequestInfo modelRequestInfo, Context context);

}

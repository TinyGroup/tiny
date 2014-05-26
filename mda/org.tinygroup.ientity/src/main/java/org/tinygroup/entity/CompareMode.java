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
package org.tinygroup.entity;

import org.tinygroup.context.Context;

import java.util.List;

/**
 * 功能说明: 条件比较信息生成接口
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-9-13 <br>
 * <br>
 */
public interface CompareMode {
    boolean needValue();

    /**
     * 根据字段名称、条件值生成比较条件信息
     *
     * @param fieldName
     * @return
     */
    String generateCompareSymbols(String fieldName);

    /**
     * 获取此比较模式的名称
     *
     * @return
     */
    String getCompareKey();

    /**
     * 先从上下文中获取参数，再对参数值进行格式化，最后存入参数列表中
     *
     * @param name    参数名称
     * @param context
     * @param params  参数列表
     * @return
     */
    void assembleParamterValue(String name, Context context, List<Object> params);
}

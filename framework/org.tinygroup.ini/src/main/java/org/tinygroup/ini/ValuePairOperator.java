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
package org.tinygroup.ini;

import java.util.List;

/**
 * Created by luoguo on 14-3-28.
 */
interface ValuePairOperator {
    /**
     * 添加值对
     *
     * @param sectionName
     * @param valuePair
     */
    void add(String sectionName, ValuePair valuePair);

    /**
     * 如果有valuePair同名的key，则会替换
     *
     * @param sectionName
     * @param valuePair
     */
    void set(String sectionName, ValuePair valuePair);

    /**
     * 添加所有的值对
     *
     * @param sectionName
     * @param valuePairList
     */
    void add(String sectionName, List<ValuePair> valuePairList);

    /**
     * 返回指定键的值对
     *
     * @param sectionName
     * @param key
     * @return
     */
    List<ValuePair> getValuePairList(String sectionName, String key);

    /**
     * 如果有多个，则会返回第一个
     *
     * @param sectionName
     * @param key
     * @return
     */
    ValuePair getValuePair(String sectionName, String key);
}

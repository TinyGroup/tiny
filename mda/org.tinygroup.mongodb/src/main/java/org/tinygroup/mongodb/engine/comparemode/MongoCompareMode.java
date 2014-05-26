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
package org.tinygroup.mongodb.engine.comparemode;

import org.bson.BSONObject;
import org.tinygroup.context.Context;

/**
 * 功能说明:mongodb的比较模式接口
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-11-27 <br>
 * <br>
 */
public interface MongoCompareMode {

    /**
     * 返回比较模式的DBObject
     *
     * @param propertyName 属性名称
     * @param value
     * @param context      上下文
     * @return
     */
    BSONObject generateBSONObject(String propertyName, Object value, Context context);

    /**
     * 获取此比较模式的名称
     *
     * @return
     */
    String getCompareKey();

    /**
     * 比较模式是否需要值
     *
     * @return
     */
    boolean needValue();

}

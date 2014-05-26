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
package org.tinygroup.dbrouter;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 分区规则接口<br>
 * 规则参数在实现类中定义
 *
 * @author luoguo
 */
@XStreamAlias("partition-rule")
public interface PartitionRule {
    /**
     * 返回是否命中
     * 如果有多个命中，则只用第一个进行处理,有多个命中是不合理的配置应该杜绝
     *
     * @param sql
     * @return
     */
    boolean isMatch(String sql);
}

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
package org.tinygroup.dbrouter.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 功能说明: 集群key生成器配置信息

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-7 <br>
 * <br>
 */
@XStreamAlias("key-generator-config")
public class KeyGeneratorConfig {

    private static final int DEFAULT_STEP = 100;

    public static final String END_NUMBER = "end_number";

    public static final String DEFAULT_KEY_TABLE_NAME = "key_table";

    @XStreamAlias("increment")
    @XStreamAsAttribute
    private int increment = 1;
    @XStreamAlias("key-table-name")
    @XStreamAsAttribute
    private String keyTableName = DEFAULT_KEY_TABLE_NAME;
    /**
     * 每次从数据库获取的幅度
     */
    private int step = DEFAULT_STEP;
    @XStreamAlias("data-source-id")
    @XStreamAsAttribute
    private String dataSourceId;

    public String getKeyTableName() {
        if (keyTableName == null) {
            keyTableName = DEFAULT_KEY_TABLE_NAME;
        }
        return keyTableName;
    }

    public void setKeyTableName(String keyTableName) {
        this.keyTableName = keyTableName;
    }

    public int getIncrement() {
        if (increment == 0) {
            increment = 1;
        }
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }


    public int getStep() {
        if (step == 0) {
            step = DEFAULT_STEP;
        }
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

}

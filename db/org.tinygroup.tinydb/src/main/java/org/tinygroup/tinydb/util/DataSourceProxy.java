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
package org.tinygroup.tinydb.util;

import org.springframework.transaction.support.TransactionTemplate;
import org.tinygroup.datasource.DynamicDataSource;

/**
 * 数据源代理
 *
 * @author luoguo
 */
public class DataSourceProxy {
    private DynamicDataSource datasource;
    private TransactionTemplate transactionTemplate;

    public DynamicDataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(DynamicDataSource datasource) {
        this.datasource = datasource;
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

}

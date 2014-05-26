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

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;

/**
 * 数据源工厂
 * 
 * @author luoguo
 * 
 */
public final class DataSourceFactory {
	
	private static Logger logger=LoggerFactory.getLogger(DataSourceFactory.class);
	
	private DataSourceFactory(){
		
	}

	public static PlatformTransactionManager getTransactionManager(
			String dataSourceName) {

		return getTransactionTemplate(dataSourceName).getTransactionManager();
	}

	public static TransactionTemplate getTransactionTemplate(
			String dataSourceName) {
		DataSourceProxy dataSource = SpringUtil.getBean("dataSourceProxy");
		return dataSource.getTransactionTemplate();
	}

	public static Connection getConnection(String dataSourceName) {
		DataSourceProxy dataSource = SpringUtil.getBean("dataSourceProxy");

		try {
			return dataSource.getDatasource().getDataSource(dataSourceName)
					.getConnection();
		} catch (SQLException e) {
			logger.errorMessage(e.getMessage(), e);
		}
		return null;
	}
}

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
package org.tinygroup.databasebuinstaller.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.logger.LogLevel;

/**
 * 
 * 功能说明:数据库表新建、字段更新、删除操作 

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public class TableInstallProcessor extends AbstractInstallProcessor {

	
	private List<Table> tableList = new ArrayList<Table>();
	private TableProcessor tableProcessor;

	public TableProcessor getTableProcessor() {
		return tableProcessor;
	}

	public void setTableProcessor(TableProcessor tableProcessor) {
		this.tableProcessor = tableProcessor;
	}
	
	
	private void deal(String language,Table table, List<String> sqls, Connection connect)throws SQLException  {
		if (tableList.contains(table))
			return;
		tableList.add(table);
		installTable(language,table, sqls, connect);
	}

	private void installTable(String language,Table table, List<String> sqls, Connection connect)throws SQLException  {
		logger.logMessage(LogLevel.INFO, "开始生成表格语句,表格 包:{0},名:{1}",
				table.getPackageName(), table.getName());
			DatabaseMetaData data = connect.getMetaData();
			// TableSqlProcessor sqlprocessor = new OracleSqlProcessorImpl();
			List<String> tableSqls = null;
			if (tableProcessor.checkTableExist(table, connect.getCatalog(),
					data, language)) {
				tableSqls = tableProcessor.getUpdateSql(table,
						table.getPackageName(), data, connect.getCatalog(),
						language);
			} else {
				tableSqls = tableProcessor.getCreateSql(table,
						table.getPackageName(), language);
			}
			if (tableSqls.size() != 0) {
				logger.logMessage(LogLevel.INFO, "生成sql:{0}", tableSqls);
			} else {
				logger.logMessage(LogLevel.INFO, "无需生成Sql");
			}
			sqls.addAll(tableSqls);
			logger.logMessage(LogLevel.INFO, "生成表格语句完成,表格 包:{0},名:{1}",
					table.getPackageName(), table.getName());
	}

	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

	
	protected List<String> getDealSqls(Connection con) throws SQLException {
		logger.logMessage(LogLevel.INFO, "开始获取数据库表安装操作执行语句");
		List<Table> list = tableProcessor.getTables();
		List<String> sqls = new ArrayList<String>();
		for (Table table : list) {
			deal(language,table, sqls, con);
		}
		logger.logMessage(LogLevel.INFO, "获取数据库表安装操作执行语句");
		return sqls;
		
	}


}

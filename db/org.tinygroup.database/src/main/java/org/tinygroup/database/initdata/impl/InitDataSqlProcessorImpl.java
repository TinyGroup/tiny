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
package org.tinygroup.database.initdata.impl;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.database.config.initdata.InitData;
import org.tinygroup.database.config.initdata.Record;
import org.tinygroup.database.config.initdata.ValuePair;
import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.database.initdata.InitDataSqlProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.util.MetadataUtil;

public class InitDataSqlProcessorImpl implements InitDataSqlProcessor {
	private Logger logger = LoggerFactory
			.getLogger(InitDataSqlProcessorImpl.class);

	public List<String> getInitSql(InitData initData) {
		List<String> initSqlList = new ArrayList<String>();
		Table table = DataBaseUtil.getTableById(initData.getTableId());
		if (initData.getRecordList() != null) {
			for (Record record : initData.getRecordList()) {
				String sql = valuePairsToAddString(record, table);
				if ("".equals(sql)) {
					continue;
				}
				logger.logMessage(LogLevel.DEBUG, "添加sql:{0}", sql);
				if(!initSqlList.contains(sql)){
					initSqlList.add(sql);
				}
				
			}
		}
		return initSqlList;
	}

	private String valuePairsToAddString(Record record, Table table) {
		List<ValuePair> valuePairs = record.getValuePairs();
		if (valuePairs == null || valuePairs.size() == 0) {
			return "";
		}else{
			StringBuffer keys = new StringBuffer();
			StringBuffer values = new StringBuffer();
				for (ValuePair valuePair : valuePairs) {
					StandardField standField = DataBaseUtil.getStandardField(
							valuePair.getTableFiledId(), table);
					String standFieldName = DataBaseUtil.getDataBaseName(standField
							.getName());
					String value = valuePair.getValue();
					// 数据由用户手动填写，包括如''之类的信息
					keys = keys.append(",").append(standFieldName);
					values = values.append(",").append("'").append(value).append("'");
				}

				return String.format("INSERT INTO %s (%s) VALUES (%s)",
						table.getName(), keys.substring(1), values.substring(1));
		}
	}

	public List<String> getDeinitSql(InitData initData) {
		List<String> initSqlList = new ArrayList<String>();
		Table table = DataBaseUtil.getTableById(initData.getTableId());
		List<String> keys = new ArrayList<String>();
		for (TableField field : table.getFieldList()) {
			if (field.getPrimary()) {
				StandardField stdField = MetadataUtil.getStandardField(field
						.getStandardFieldId());
				keys.add(DataBaseUtil.getDataBaseName(stdField.getName())

				);
			}
		}
		if (initData.getRecordList() != null) {
			for (Record record : initData.getRecordList()) {
				String sql = valuePairsToDelString(record, table, keys);
				if ("".equals(sql)) {
					continue;
				}
				logger.logMessage(LogLevel.DEBUG, "添加sql:{0}", sql);
				if(!initSqlList.contains(sql)){
					initSqlList.add(sql);
				}
			}
		}
		return initSqlList;
	}

	private String valuePairsToDelString(Record record, Table table,
			List<String> keys) {
		List<ValuePair> valuePairs = record.getValuePairs();
		if (valuePairs == null || valuePairs.size() == 0) {
			return "";
		}

		String where = "";
		int times = 0;// 当单条数据的delete语句生成时,times的次数应该等于keys，即主键列表的长度
		for (ValuePair valuePair : valuePairs) {
			StandardField standField = DataBaseUtil.getStandardField(
					valuePair.getTableFiledId(), table);
			String standFieldName = DataBaseUtil.getDataBaseName(standField
					.getName());
			String value = valuePair.getValue();
			if (!keys.contains(standFieldName)) {
				continue;// 不是主键
			}
			// 数据由用户手动填写，包括如''之类的信息
			where =where+ String.format(" AND %s='%s'", standFieldName, value);
			times++;
		}
		if (times != keys.size()) {
			logger.logMessage(LogLevel.ERROR,
					"解析生成delete语句时出错,主键数不匹配,应有主键数{0},实际{1}", keys.size(), times);
			return "";
		}
		int index = where.indexOf("AND");
		where = where.substring(index + 3);
		return String
				.format("DELETE FROM %s WHERE %s", table.getName(), where);
	}

}

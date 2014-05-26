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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.database.ProcessorManager;
import org.tinygroup.database.config.initdata.InitData;
import org.tinygroup.database.config.initdata.InitDatas;
import org.tinygroup.database.config.initdata.Record;
import org.tinygroup.database.initdata.InitDataProcessor;
import org.tinygroup.database.initdata.InitDataSqlProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.springutil.SpringUtil;

/**
 * @author chenjiao
 * 
 */
public class InitDataProcessorImpl implements InitDataProcessor {
	private Logger logger = LoggerFactory
			.getLogger(InitDataProcessorImpl.class);

	/**
	 * Map<包名,Map<表名,InitData>>
	 */
	private static Map<String, Map<String, InitData>> initDatasNameMap = new HashMap<String, Map<String, InitData>>();
	private static Map<String, InitData> initDatasIdMap = new HashMap<String, InitData>();

	public List<String> getInitSql(String packageName, String tableName,
			String language) {
		InitData tableInitData = getInitData(packageName, tableName); // 肯定不为空，为空的情况该函数会直接抛异常
		return getInitSql(tableInitData, language);
	}

	public List<String> getInitSql(String tableName, String language) {
		return getInitSql(null, tableName, language);
	}

	public List<String> getInitSqlByTableId(String tableId, String language) {
		InitData tableInitData = getInitDataByTableId(tableId);
		return getInitSql(tableInitData, language);
	}

	public List<String> getInitSql(String language) {
		ProcessorManager processorManager = SpringUtil
				.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		InitDataSqlProcessor sqlProcessor = (InitDataSqlProcessor) processorManager
				.getProcessor(language, "initData");
		List<String> list = new ArrayList<String>();
		for (Map<String, InitData> tablesInitData : initDatasNameMap.values()) {
			for (InitData tableInitData : tablesInitData.values()) {
				list.addAll(sqlProcessor.getInitSql(tableInitData));
			}
		}
		return list;
	}

	public List<String> getInitSql(InitData tableInitData, String language) {
		List<String> sqls = new ArrayList<String>();
		ProcessorManager processorManager = SpringUtil
				.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		InitDataSqlProcessor sqlProcessor = (InitDataSqlProcessor) processorManager
				.getProcessor(language, "initData");
		sqls.addAll(sqlProcessor.getInitSql(tableInitData));
		return sqls;
	}

	public void addInitDatas(InitDatas initDatas) {
		logger.logMessage(LogLevel.DEBUG, "开始添加表格初始数据");
		if (initDatas == null || initDatas.getInitDataList() == null) {
			logger.logMessage(LogLevel.DEBUG, "传入的初始数据为空，数据添加结束。");
			return;
		}
		for (InitData initData : initDatas.getInitDataList()) {
			addInitData(initData);
		}
		logger.logMessage(LogLevel.DEBUG, "表格初始数据添加完毕");
	}
	
	public void removeInitDatas(InitDatas initDatas) {
		logger.logMessage(LogLevel.DEBUG, "开始添加表格初始数据");
		if (initDatas == null || initDatas.getInitDataList() == null) {
			logger.logMessage(LogLevel.DEBUG, "传入的初始数据为空，数据添加结束。");
			return;
		}
		for (InitData initData : initDatas.getInitDataList()) {
			removeInitData(initData);
		}
		logger.logMessage(LogLevel.DEBUG, "表格初始数据添加完毕");
	}
	
	/**
	 * 添加初始化sql
	 * 
	 * @param initData
	 */
	private void removeInitData(InitData initData) {
		String packageName = MetadataUtil.passNull(initData.getPackageName());
		String tableId = MetadataUtil.passNull(initData.getTableId());
		String tableName = DataBaseUtil.getTableById(tableId).getName();
		logger.logMessage(LogLevel.DEBUG, "开始移除表格[包:{0},表名:{1},表ID:{2}]的初始化数据",
				packageName, tableName, tableId);

		Map<String, InitData> packageInitDataMap = initDatasNameMap
				.get(packageName);
		if(!CollectionUtil.isEmpty(packageInitDataMap)){
			InitData tableInitData = packageInitDataMap.get(tableName);
			if(tableInitData!=null){
				tableInitData.getRecordList().removeAll(initData.getRecordList());
			}
		}
		initDatasIdMap.remove(tableId);
		logger.logMessage(LogLevel.DEBUG, "移除表格[包:{0},表名:{1},表ID:{2}]的初始化数据完毕",
				packageName, tableName, tableId);
	}

	/**
	 * 添加初始化sql
	 * 
	 * @param initData
	 */
	private void addInitData(InitData initData) {
		String packageName = MetadataUtil.passNull(initData.getPackageName());
		String tableId = MetadataUtil.passNull(initData.getTableId());
		String tableName = DataBaseUtil.getTableById(tableId).getName();
		logger.logMessage(LogLevel.DEBUG, "开始为表格[包:{0},表名:{1},表ID:{2}]添加初始化数据",
				packageName, tableName, tableId);

		if (!initDatasNameMap.containsKey(packageName)) {
			Map<String, InitData> packageInitDataMap = new HashMap<String, InitData>();
			initDatasNameMap.put(packageName, packageInitDataMap);
		}
		Map<String, InitData> packageInitDataMap = initDatasNameMap
				.get(packageName);
		if (!packageInitDataMap.containsKey(tableName)) {
			InitData tableInitData = new InitData();
			tableInitData.setPackageName(packageName);
			tableInitData.setTableId(tableId);
			tableInitData.setRecordList(new ArrayList<Record>());
			packageInitDataMap.put(tableName, tableInitData);
		}
		InitData tableInitData = packageInitDataMap.get(tableName);
		tableInitData.getRecordList().addAll(initData.getRecordList());

		// 对象引用，由前面进行了维护，此处不需进行额外维护
		if (!initDatasIdMap.containsKey(tableId)) {
			initDatasIdMap.put(tableId, tableInitData);
		}

		logger.logMessage(LogLevel.DEBUG, "表格[包:{0},表名:{1},表ID:{2}]添加初始化数据完毕",
				packageName, tableName, tableId);
	}

	public InitData getInitDataByTableId(String tableId) {
		return initDatasIdMap.get(tableId);
	}

	public InitData getInitData(String tableName) {
		return getInitData("", tableName);
	}

	public InitData getInitData(String packageName, String tableName) {
		String realPackagetName = MetadataUtil.passNull(packageName);
		String realTableName = MetadataUtil.passNull(tableName);
		logger.logMessage(LogLevel.DEBUG, "获取表格[包:{0},表名:{1}]初始化数据",
				realPackagetName, realTableName);
		if (initDatasNameMap.containsKey(realPackagetName)) {
			if (initDatasNameMap.get(realPackagetName).containsKey(tableName)) {
				logger.logMessage(LogLevel.DEBUG, "成功获取表格[包:{0},表名:{1}]初始化数据",
						realPackagetName, realTableName);
				return initDatasNameMap.get(realPackagetName)
						.get(realTableName);
			}
		}
		logger.logMessage(LogLevel.DEBUG, "[包:{0}]下未找到[表名:{1}]的初始化数据",
				realPackagetName, realTableName);
		for (Map<String, InitData> packageInitDataMap : initDatasNameMap
				.values()) {
			if (packageInitDataMap.containsKey(realTableName)) {
				InitData initData = packageInitDataMap.get(realTableName);
				logger.logMessage(LogLevel.DEBUG,
						"成功获取表格[包:{0},表名:{1}]初始化数据,表实际所在[包:{0}]",
						realPackagetName, realTableName,
						initData.getPackageName());
				return initData;
			}
		}
		throw new RuntimeException(String.format("获取表格[包:%s,表名:%s]初始化数据失败",
				realPackagetName, realTableName));

	}

	public List<String> getDeinitSql(String packageName, String tableName,
			String language) {
		InitData tableInitData = getInitData(packageName, tableName); // 肯定不为空，为空的情况该函数会直接抛异常
		return getDeinitSql(tableInitData, language);

	}

	public List<String> getDeinitSql(String tableName, String language) {
		return getDeinitSql(null, tableName, language);
	}

	public List<String> getDeinitSql(String language) {
		ProcessorManager processorManager = SpringUtil
				.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		InitDataSqlProcessor sqlProcessor = (InitDataSqlProcessor) processorManager
				.getProcessor(language, "initData");
		List<String> list = new ArrayList<String>();
		for (Map<String, InitData> tablesInitData : initDatasNameMap.values()) {
			for (InitData tableInitData : tablesInitData.values()) {
				list.addAll(sqlProcessor.getDeinitSql(tableInitData));
			}
		}
		return list;
	}

	private List<String> getDeinitSql(InitData tableInitData, String language) {
		List<String> sqls = new ArrayList<String>();
		ProcessorManager processorManager = SpringUtil
				.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		InitDataSqlProcessor sqlProcessor = (InitDataSqlProcessor) processorManager
				.getProcessor(language, "initData");
		sqls.addAll(sqlProcessor.getDeinitSql(tableInitData));
		return sqls;
	}

	public List<String> getDeinitSqlByTableId(String tableId, String language) {
		InitData data = getInitDataByTableId(tableId);
		return getDeinitSql(data, language);
	}

	public List<InitData> getInitDatas() {
		List<InitData> initDatas = new ArrayList<InitData>();
		for (Map<String, InitData> initdata : initDatasNameMap.values()) {
			initDatas.addAll(initdata.values());
		}
		return initDatas;
	}

}

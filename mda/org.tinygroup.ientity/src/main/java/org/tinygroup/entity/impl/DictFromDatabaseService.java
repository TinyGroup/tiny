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
package org.tinygroup.entity.impl;

import org.tinygroup.dict.Dict;
import org.tinygroup.dict.DictGroup;
import org.tinygroup.dict.DictItem;
import org.tinygroup.service.annotation.ServiceComponent;
import org.tinygroup.service.annotation.ServiceMethod;
import org.tinygroup.service.annotation.ServiceResult;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.operator.DBOperator;

/**
 * 从数据库中读取字典
 * 
 * @author luoguo
 * 
 */
@ServiceComponent()
public class DictFromDatabaseService {
	@ServiceMethod(serviceId = "loadDictFromDatabase")
	@ServiceResult(name = "dictResult")
	public Dict getDict(String beanType, String sql, String valueFieldName,
			String textFieldName) {
		BeanOperatorManager manager = SpringUtil
				.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
		DBOperator<?> itemOperator = manager.getDbOperator(beanType);
		Dict dict = new Dict(beanType, beanType);
		Bean[] dictItemBeans = itemOperator.getBeans(sql);
		DictGroup dictGroup = new DictGroup("defaultGroupName",
				"defaultGroupName");
		dict.addDictGroup(dictGroup);
		for (Bean bean : dictItemBeans) {
			String value = bean.getProperty(valueFieldName);
			String text = bean.getProperty(textFieldName);
			DictItem dictItem = new DictItem(value, text);
			dictGroup.addDictItem(dictItem);
		}
		return dict;
	}

	@ServiceMethod(serviceId = "loadDictTextFromDatabase")
	@ServiceResult(name = "dictText")
	public String getText(String beanType, String sql, String value,
			String textFieldName) {
		BeanOperatorManager manager = SpringUtil
				.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
		DBOperator<?> itemOperator = manager.getDbOperator(beanType);
		Bean[] beans = itemOperator.getBeans(sql, value);
		if (beans.length >= 1) {
			return beans[0].getProperty(textFieldName);
		}
		return "";
	}

}

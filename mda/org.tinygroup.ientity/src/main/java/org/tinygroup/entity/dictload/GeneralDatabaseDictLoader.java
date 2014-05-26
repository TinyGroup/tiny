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
package org.tinygroup.entity.dictload;

import org.tinygroup.dict.Dict;
import org.tinygroup.dict.DictGroup;
import org.tinygroup.dict.DictItem;
import org.tinygroup.dict.DictManager;
import org.tinygroup.dict.impl.AbstractDictLoader;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.operator.DBOperator;

/**
 * 通用数据库字典加载器
 * 
 * @author luoguo
 * 
 */
public class GeneralDatabaseDictLoader extends AbstractDictLoader {
	String beanType;
	String valueFieldName;
	String textFieldName;
	String sql;

	public void load(DictManager dictManager) {
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
		putDict(dict.getName(), dict, dictManager);
	}

	public String getBeanType() {
		return beanType;
	}

	public void setBeanType(String beanType) {
		this.beanType = beanType;
	}

	public String getValueFieldName() {
		return valueFieldName;
	}

	public void setValueFieldName(String valueFieldName) {
		this.valueFieldName = valueFieldName;
	}

	public String getTextFieldName() {
		return textFieldName;
	}

	public void setTextFieldName(String textFieldName) {
		this.textFieldName = textFieldName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}

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

import org.tinygroup.commons.tools.StringUtil;
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
 * 
 * 功能说明:模型字典数据加载类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-27 <br>
 * <br>
 */
public class DictDataLoader extends AbstractDictLoader {

	private static final String DICT_TYPE_NAME = "dict";
	private static final String DICTITEM_TYPE_NAME = "dictItem";
	private static final String DICT_ID = "dictId";
	private static final String DICT_CODE = "dictCode";
	private static final String DICT_DESCRIPTION = "description";
	private static final String DICT_ITEM_CODE = "dictItemName";
	private static final String DICT_ITEM_VALUE = "dictItemValue";
	private static final String DICT_ITEM_GROUP = "dictItemGroup";
	private static final String DEFAULT_GROUP_NAME = "defaultGroupName";

	public void load(DictManager dictManager) {
		BeanOperatorManager manager = SpringUtil
				.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
		DBOperator operator = manager.getDbOperator(DICT_TYPE_NAME);
		DBOperator itemOperator = manager.getDbOperator(DICTITEM_TYPE_NAME);
		Bean[] beans = operator.getBeans(new Bean(DICT_TYPE_NAME));
		if(beans!=null){
			for (Bean bean : beans) {
				String dictId = bean.getProperty(DICT_ID);
				String dictCode = bean.getProperty(DICT_CODE);
				String description = bean.getProperty(DICT_DESCRIPTION);
				Dict dict = new Dict(dictCode, description);
				Bean[] groupItemBeans = itemOperator
						.getBeans(
								"SELECT dict_item_group FROM dict_item where dict_id=? group by dict_item_group",
								dictId);
				if (groupItemBeans != null && groupItemBeans.length > 0) {
					for (Bean groupItemBean : groupItemBeans) {
						String dictItemGroup = groupItemBean
								.getProperty(DICT_ITEM_GROUP);
						DictGroup dictGroup = null;
						Bean itemParamBean = new Bean(DICTITEM_TYPE_NAME);
						itemParamBean.setProperty(DICT_ID, dictId);
						if (StringUtil.isBlank(dictItemGroup)) {
							dictGroup = new DictGroup(DEFAULT_GROUP_NAME,
									DEFAULT_GROUP_NAME);
						} else {
							dictGroup = new DictGroup(dictItemGroup, dictItemGroup);
							itemParamBean.setProperty(DICT_ITEM_GROUP, dictItemGroup);
						}
						Bean[] itemBeans = itemOperator.getBeans(itemParamBean);
						for (Bean itemBean : itemBeans) {
							String dictItemCode = itemBean
									.getProperty(DICT_ITEM_CODE);
							String dictItemValue = itemBean
									.getProperty(DICT_ITEM_VALUE);
							DictItem dictItem = new DictItem(dictItemValue,
									dictItemCode);
							dictGroup.addDictItem(dictItem);
						}
						dict.addDictGroup(dictGroup);
					}
				}
				putDict(dict.getName(), dict, dictManager);

			}
		}
		
	}

}

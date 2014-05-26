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
package org.tinygroup.entity.common;

import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.operator.DBOperator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * 功能说明:下拉框输入模式
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-6 <br>
 * <br>
 */
@XStreamAlias("combo-box-input-mode")
public class ComboBoxInputMode extends InputMode {
	@XStreamAlias("bean-type")
	@XStreamAsAttribute
	private String beanType;
	@XStreamAlias("label-property")
	@XStreamAsAttribute
	private String labelProperty;
	@XStreamAlias("value-property")
	@XStreamAsAttribute
	private String valueProperty;

	public String getBeanType() {
		return beanType;
	}

	public void setBeanType(String beanType) {
		this.beanType = beanType;
	}

	public String getLabelProperty() {
		return labelProperty;
	}

	public void setLabelProperty(String labelProperty) {
		this.labelProperty = labelProperty;
	}

	public String getValueProperty() {
		return valueProperty;
	}

	public void setValueProperty(String valueProperty) {
		this.valueProperty = valueProperty;
	}

	@SuppressWarnings("rawtypes")
	public String getComboBoxDataJson() {
		BeanOperatorManager manager = SpringUtil
				.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
		DBOperator operator = manager.getDbOperator(beanType);
		Bean[] beans = operator.getBeans(new Bean(beanType));
		ComboBoxItem[] items = new ComboBoxItem[beans.length];
		if (beans != null) {
			for (int i = 0; i < beans.length; i++) {
				Bean bean=beans[i];
				if(bean!=null){
					ComboBoxItem item=new ComboBoxItem();
					item.setLabel((String) bean.getProperty(labelProperty));
					item.setValue((String) bean.getProperty(valueProperty));
					items[i]=item;
				}
			}
			
		}
		StringBuffer buffer=new StringBuffer();
		buffer.append("[");
		for (int i = 0; i < items.length; i++) {
			if(i>0){
				buffer.append(",");
			}
			buffer.append(items[i]);
		}
		buffer.append("]");
		return buffer.toString();
	}

}

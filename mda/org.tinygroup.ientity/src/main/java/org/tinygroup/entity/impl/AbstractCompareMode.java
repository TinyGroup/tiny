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

import java.util.List;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.context.Context;
import org.tinygroup.entity.CompareMode;

/**
 * 
 * 功能说明: 抽象的比较模式实现
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-13 <br>
 * <br>
 */
public abstract class AbstractCompareMode implements CompareMode {
	public boolean needValue() {
		return true;
	}

	public AbstractCompareMode() {

	}

	public void assembleParamterValue(String name, Context context, List<Object> params) {
		Assert.assertNotNull(params, "params must not null");
		Object value=context.get(name);
		if (value != null && value.getClass().isArray()) {
			formatValueArray((Object[]) value, params);
		} else {
			Object formatValue = formaterValue(value,name,context);
			if (formatValue != null) {
				params.add(formatValue);
			}
		}

	}

	protected void formatValueArray(Object[] array, List<Object> params) {
		for (Object object : array) {
			params.add(object);
		}
	}
    
	/**
	 * 
	 * 对参数值进行格式化,又外部进行调用
	 * @param value
	 * @param name
	 * @param context
	 */
	public Object formaterValueExternalUse(Object value, String name, Context context){
		return formaterValue(value, name, context);
	}
	
	/**
	 * 
	 * 对参数值进行格式化,由子类实现
	 * @param value
	 * @param name
	 * @param context
	 * @return
	 */
	protected Object formaterValue(Object value, String name, Context context) {
		return value;
	}

}

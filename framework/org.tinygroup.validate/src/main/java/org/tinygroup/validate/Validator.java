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
package org.tinygroup.validate;

/**
 * 校验接口
 * 
 * @author luoguo
 * 
 */
public interface Validator {
	/**
	 * 对数据进行校验，如果校验的数据有错误，则把错误信息添加到validateResult当中
	 * 
	 * @param name
	 *            字段名字
	 * @param title
	 *            字段的标题
	 * @param value
	 *            字段的值
	 * @param validateResult
	 *            校验结果
	 */
	<T> void validate(String name, String title, T value,
			ValidateResult validateResult);
}

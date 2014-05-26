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
package org.tinygroup.mongodb.util;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.exception.TinySysRuntimeException;


/**
 * 
 * 功能说明:工具类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-4 <br>
 * <br>
 */
public class ModelUtil {

	
	public static String getSpliceParamterName(String propertyName, String suffix) {
		return propertyName + suffix;
	}

	
	@SuppressWarnings("rawtypes")
	public static <T> T bean2Object(Map bean, Class<T> type) {
		try {
			T target = type.newInstance();
			BeanUtils.populate(target, bean);
			return target;
		} catch (Exception e) {
			throw new TinySysRuntimeException(e);
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	public static <T> T bean2Object(Map bean, T target) {
		try {
			BeanUtils.populate(target, bean);
			return target;
		} catch (Exception e) {
			throw new TinySysRuntimeException(e);
		}
	}
	

}

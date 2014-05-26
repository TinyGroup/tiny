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
package org.tinygroup.service.util;

import java.util.List;

import org.tinygroup.event.Parameter;
import org.tinygroup.service.exception.ServiceParamValidateException;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.ValidatorManager;
import org.tinygroup.validate.XmlValidatorManager;
import org.tinygroup.validate.impl.ValidateResultImpl;

public class ParamValidate {

	public static void validate(Object[] args, List<Parameter> inputParameters) {
		ValidateResult result = new ValidateResultImpl();
		for (int i = 0; i < inputParameters.size(); i++) {
			Parameter p = inputParameters.get(i);
			Object value = args[i];
			String sence = p.getValidatorSence();
			if (sence != null && !"".equals(sence)) {
				ValidatorManager xmlValidatorManager = SpringUtil
						.getBean(XmlValidatorManager.class);
				if (p.isArray()) {//如果是数组
					Object[] array = (Object[]) value;
					validateArray(sence, array, result, xmlValidatorManager);
				} else if (value instanceof List) { //如果是List
					validateList(sence, (List) value, result,
							xmlValidatorManager);
				} else {
					validateObject(sence, value, result, xmlValidatorManager);
				}
			}
		}
		if (result.hasError()) {
			throw new ServiceParamValidateException(result);
		}
	}

	private static void validateList(String sence, List value,
			ValidateResult result, ValidatorManager xmlValidatorManager) {
		Object[] array = value.toArray();
		for (Object o : array) {
			validateObject(sence, o, result, xmlValidatorManager);
		}
	}

	private static void validateArray(String sence, Object[] value,
			ValidateResult result, ValidatorManager xmlValidatorManager) {
		for (Object o : value) {
			validateObject(sence, o, result, xmlValidatorManager);
		}
	}

	private static void validateObject(String sence, Object value,
			ValidateResult result, ValidatorManager xmlValidatorManager) {
		xmlValidatorManager.validate(sence, value, result);
	}
}

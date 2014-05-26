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
package org.tinygroup.validate.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.exception.FlowRuntimeException;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.ValidatorManager;
import org.tinygroup.validate.impl.ValidateResultImpl;

/**
 * 校验组件
 * 
 * @author renhui
 * 
 */
public class ValidateComponent implements ComponentInterface {

	private static final String VALIDATE_ERROR = "validateError";
	private static final String VALIDATOR_MANAGER = "complexValidatorManager";
	/**
	 * 校验环境对象域名
	 */
	private static final String VALIDATE_DOMAIN = "validate";
	/**
	 * 环境对象中校验信息key的后缀
	 */
	private static final String VALIDATE_MESSAGE = "validateMessage";
	private String objectName;
	private String scene;
	
	

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * 校验管理器对象
	 */
	private ValidatorManager validatorManager;

	public ValidatorManager getValidatorManager() {
		return validatorManager;
	}

	public void setValidatorManager(ValidatorManager validatorManager) {
		this.validatorManager = validatorManager;
	}

	public void execute(Context context) {

		Object validateObject = context.get(objectName);
		if (validateObject != null) {
			ValidateResult result = new ValidateResultImpl();
			if (validatorManager == null) {
				validatorManager = SpringUtil.getBean(VALIDATOR_MANAGER);
			}
			validatorManager.validate(scene,validateObject, result);
			context.put(VALIDATE_DOMAIN,  VALIDATE_MESSAGE, result);
			if(result.hasError()){
				context.put(VALIDATE_ERROR, true);
			}else{
				context.put(VALIDATE_ERROR, false);
			}
			return;
		} else {
			throw new FlowRuntimeException("flow.validatorNotExist", objectName);
		}
	}

}

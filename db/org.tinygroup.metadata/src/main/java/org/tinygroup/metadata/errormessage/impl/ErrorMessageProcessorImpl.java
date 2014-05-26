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
package org.tinygroup.metadata.errormessage.impl;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.metadata.config.errormessage.ErrorMessage;
import org.tinygroup.metadata.config.errormessage.ErrorMessages;
import org.tinygroup.metadata.errormessage.ErrorMessageProcessor;
import org.tinygroup.metadata.util.MetadataUtil;

public class ErrorMessageProcessorImpl implements ErrorMessageProcessor {
	Map<String, ErrorMessages> errorMessageMap = new HashMap<String, ErrorMessages>();

	public ErrorMessage getErrorMessage(String packageName, String name) {
		if (packageName != null) {
			ErrorMessage errorMessage = errorMessageMap.get(packageName)
					.getErrorMessageMap().get(name);
			if (errorMessage != null) {
				return errorMessage;
			}
		}
		for (String pkgName : errorMessageMap.keySet()) {
			ErrorMessage errorMessage = errorMessageMap.get(pkgName)
					.getErrorMessageMap().get(name);
			if (errorMessage != null) {
				return errorMessage;
			}
		}
		throw new RuntimeException(String.format(
				"package [%s] name [%s]的错误信息不存在。", packageName, name));
	}

	public ErrorMessage getErrorMessage(String name) {
		return getErrorMessage(null, name);
	}

	public void addErrorMessages(ErrorMessages errorMessages) {
		errorMessageMap.put(
				MetadataUtil.passNull(errorMessages.getPackageName()),
				errorMessages);
		Map<String, ErrorMessage> errorInfoMap = errorMessages
				.getErrorMessageMap();
		if (errorMessages.getErrorMessageList() != null) {
			for (ErrorMessage errorMessage : errorMessages
					.getErrorMessageList()) {
				errorInfoMap.put(errorMessage.getErrorId(), errorMessage);
			}
		}
	}
	
	public void removeErrorMessages(ErrorMessages errorMessages) {
		errorMessageMap.remove(
				MetadataUtil.passNull(errorMessages.getPackageName()));
	}
}

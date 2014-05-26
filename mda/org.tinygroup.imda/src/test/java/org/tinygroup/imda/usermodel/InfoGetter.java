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
package org.tinygroup.imda.usermodel;

import java.util.List;
import java.util.Map;

import org.tinygroup.imda.ModelInformationGetter;
import org.tinygroup.imda.config.CustomizeStageConfig;
import org.tinygroup.imda.validate.ValidateRule;

public class InfoGetter implements ModelInformationGetter<CaseModel> {

	public String getId(CaseModel model) {
		return model.getId();
	}

	public String getName(CaseModel model) {
		return model.getName();
	}

	public String getCategory(CaseModel model) {
		return model.getPath();
	}

	public String getTitle(CaseModel model) {
		return model.getTitle();
	}

	public String getDescription(CaseModel model) {
		return model.getDes();
	}

	public Object getOperation(CaseModel model, String id) {
		return null;
	}

	public String getOperationType(CaseModel model, String id) {
		return null;
	}

	public String getOperationId(Object operation) {
		return null;
	}

	public List<String> getParamterList(CaseModel model, Object operation) {
		return null;
	}

	public CustomizeStageConfig getCustomizeStageConfig(CaseModel model, String operationId,
			String stageName) {
		return null;
	}

	public Map<String, List<ValidateRule>> getOperationValidateMap(
			CaseModel model, Object operation) {
		return null;
	}

}

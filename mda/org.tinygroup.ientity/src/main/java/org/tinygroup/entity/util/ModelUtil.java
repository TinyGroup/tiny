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
package org.tinygroup.entity.util;

import java.util.List;

import org.tinygroup.entity.BaseModel;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.View;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.util.MetadataUtil;

/**
 * 
 * 功能说明:工具类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-4 <br>
 * <br>
 */
public class ModelUtil {

	public static Operation getOperation(BaseModel entityModel,
			String processorId) {
		if (entityModel != null) {
			List<Operation> operations = entityModel.getOperations();
			if (operations != null) {
				for (Operation operation : operations) {
					if (operation.getId().equals(processorId)) {
						return operation;
					}
				}
			}
		}
		return null;

	}

	public static View getView(BaseModel entityModel, String processorId) {
		if (entityModel != null) {
			List<View> views = entityModel.getViews();
			if (views != null) {
				for (View view : views) {
					if (view.getId().equals(processorId)) {
						return view;
					}
				}
			}
		}
		return null;

	}

	
	/**
	 * 
	 * 
	 * @param 标准字段
	 * @return
	 */
	public static String getDbFieldName(String standardFieldId) {
		return MetadataUtil.getStandardField(standardFieldId).getName();
	}

	public static StandardField getStandardField(String standardFieldId) {
		return MetadataUtil.getStandardField(standardFieldId);
	}
	
	public static String getSpliceParamterName(String propertyName, String suffix) {
		return propertyName + suffix;
	}

	

}

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
package org.tinygroup.mongodb.engine.operation.modify;

import org.tinygroup.mongodb.engine.operation.AbstractOperationServiceProcessor;
import org.tinygroup.mongodb.engine.operation.MongoOperationContext;

/**
 * 
 * 功能说明: 更新操作服务

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-28 <br>
 * <br>
 */
public class ModifyOperationSaveServiceProcessor extends
		AbstractOperationServiceProcessor<Long> {

	
	protected Long serviceProcessor(MongoOperationContext serviceProcessor) {
		return serviceProcessor.updateMongoModel();
	}

}

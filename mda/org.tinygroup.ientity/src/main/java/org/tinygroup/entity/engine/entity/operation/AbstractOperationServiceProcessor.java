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
package org.tinygroup.entity.engine.entity.operation;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.context.Context;
import org.tinygroup.entity.AbstractEntityModelServiceProcessor;
import org.tinygroup.entity.EntityModelFilterProcessor;
import org.tinygroup.entity.OperationServiceProcessor;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.View;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.exception.TinySysRuntimeException;

/**
 * 
 * 功能说明: 操作服务逻辑的抽象类，不能调用视图服务

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-6 <br>
 * <br>
 */
public abstract class AbstractOperationServiceProcessor<R> extends
		AbstractEntityModelServiceProcessor<R> {
	
   private List<EntityModelFilterProcessor> filters=new ArrayList<EntityModelFilterProcessor>();
	
	public List<EntityModelFilterProcessor> getFilters() {
		return filters;
	}

	public void setFilters(List<EntityModelFilterProcessor> filters) {
		this.filters = filters;
	}

	
	protected R process(Context context, EntityModel entityModel, View view) {
           throw new TinySysRuntimeException("ientity.canNotViewService");
	}

	
	protected R process(Context context, EntityModel entityModel,
			Operation operation) {
		OperationServiceProcessor processor = new OperationServiceProcessor(
				entityModel, context, operation);//模型操作服务处理
		for (EntityModelFilterProcessor filterProcessor : filters) {
			filterProcessor.filterProcessor(processor);
		}
		return operateProcess(processor);
	}

	/**
	 * 
	 * 操作服务处理接口
	 * @param processor
	 * @return
	 */
	protected abstract R operateProcess(OperationServiceProcessor processor);
	

}

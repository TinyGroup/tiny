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
package org.tinygroup.mongodb.engine.view;

import org.tinygroup.context.Context;
import org.tinygroup.mongodb.common.Operation;
import org.tinygroup.mongodb.common.View;
import org.tinygroup.mongodb.engine.AbstractMongoServiceProcessor;
import org.tinygroup.mongodb.model.MongoDBModel;

/**
 * 
 * 功能说明: 抽象的视图服务处理

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-28 <br>
 * <br>
 */
public abstract class AbstractViewServiceProcessor<R> extends AbstractMongoServiceProcessor<R> {

	
	protected R process(Context context, MongoDBModel mongoDBModel,
			Operation operation) {
		throw new RuntimeException("不能调用操作服务");
	}

	
	protected R process(Context context, MongoDBModel mongoDBModel, View view) {
		MongoViewContext serviceProcessor=new MongoViewContext(mongoDBModel, view, context);
		return serviceProcessor(serviceProcessor);
	}

	protected abstract R serviceProcessor(MongoViewContext serviceProcessor);

}

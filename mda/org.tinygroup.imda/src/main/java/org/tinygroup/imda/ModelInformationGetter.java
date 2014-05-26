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
package org.tinygroup.imda;

import java.util.List;
import java.util.Map;

import org.tinygroup.imda.config.CustomizeStageConfig;
import org.tinygroup.imda.validate.ValidateRule;

/**
 * 模型基本信息获取接口<br>
 * 此接口用于从模型中读取相关信息
 * 
 * @author luog
 * 
 */
public interface ModelInformationGetter<T> {
	/**
	 * 返回标识
	 * 
	 * @param model
	 * @return
	 */
	String getId(T model);

	/**
	 * 返回英文名称
	 * 
	 * @param model
	 * @return
	 */
	String getName(T model);

	/**
	 * 返回分类,可以有多层分类，用"/"分隔<br>
	 * 比如：基础管理/用户权限/用户
	 * 
	 * @param model
	 * @return
	 */
	String getCategory(T model);

	/**
	 * 返回标题
	 * 
	 * @param model
	 * @return
	 */
	String getTitle(T model);// 返回标题

	/**
	 * 返回描述
	 * 
	 * @param model
	 * @return
	 */
	String getDescription(T model);

	/**
	 * 返回对应的操作或视图
	 * 
	 * @param model
	 * @param operationId
	 * @return
	 */
	Object getOperation(T model, String operationId);

	/**
	 * 获取操作类型
	 * 
	 * @param model
	 * @param operationId
	 * @return
	 */
	String getOperationType(T model, String operationId);

	/**
	 * 获取某指定操作的自定义配置
	 * 
	 * @param model
	 * @param operationId
	 * @return
	 */
	CustomizeStageConfig getCustomizeStageConfig(T model, String operationId,
			String stageName);

	/**
	 * 获取操作ID
	 * 
	 * @param operation
	 * @return
	 */
	String getOperationId(Object operation);

	/**
	 * 返回参数部分的URL
	 * 
	 * @param model
	 * @param operation
	 * @return
	 */
	List<String> getParamterList(T model, Object operation);

	/**
	 * 返回操作的校验规则列表<br>
	 * key:字段名称
	 * @param model 
	 * @param operation
	 * 
	 * @return
	 */
	Map<String, List<ValidateRule>> getOperationValidateMap(T model, Object operation);
}

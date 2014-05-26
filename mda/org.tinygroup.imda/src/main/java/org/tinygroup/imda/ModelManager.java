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

import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.tinygroup.context.Context;
import org.tinygroup.imda.config.CustomizeStageConfig;
import org.tinygroup.imda.config.ModelDefine;
import org.tinygroup.imda.config.ModelProcessorDefine;
import org.tinygroup.imda.config.ModelProcessorStage;
import org.tinygroup.imda.impl.ModelContainer;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.imda.validate.ValidateRule;

/**
 * 模型管理器<br>
 * 所谓模型，就是含有一些描述信息的对象，通过对此模型进行解释，就可以动态完成一些系统资源的内容
 * 
 * @author luog
 * 
 */
public interface ModelManager {
	String MODELMANAGER_BEAN = "modelManager";

	/**
	 * 更新模型<br>
	 * 当模型被修改之后，通过模型更新可以使之生效，如果模型不存在，则相当于添加模型
	 * 
	 * @param model
	 */
	void updateModel(Object model);

	/**
	 * 添加模型，如果已经存在，则相当于更新模型
	 * 
	 * @param model
	 */
	void addModel(Object model);

	/**
	 * 删除模型
	 * 
	 * @param model
	 */
	void removeModel(Object model);

	/**
	 * 增加模型定义
	 * 
	 * @param modelType
	 * @param modelProcessor
	 */
	void addModelDefine(ModelDefine modelDefine);
	
	/**
	 * 移除模型定义
	 * 
	 * @param modelType
	 * @param modelProcessor
	 */
	void removeModelDefine(ModelDefine modelDefine);

	/**
	 * 获取模型
	 * 
	 * @param modelId
	 * @return
	 */
	<T> T getModel(String modelId);

	/**
	 * 获取模型定义
	 * 
	 * @param name
	 * @return
	 */
	ModelDefine getModelDefine(String modelDefineId);

	/**
	 * 获取模型定义处理
	 * 
	 * @param modelDefineId
	 * @param operationType
	 * @return
	 */
	ModelProcessorDefine getModelProcessorDefine(String modelDefineId,
			String operationType);

	/**
	 * 获取模型定义处理
	 * 
	 * @param modelDefine
	 * @param operationType
	 * @return
	 */
	ModelProcessorDefine getModelProcessorDefine(ModelDefine modelDefine,
			String operationType);

	/**
	 * 对模型服务进行处理
	 * 
	 * @param model
	 * @param processorId
	 * @return
	 */
	<T> T processService(ModelRequestInfo modelRequestInfo, Context context);

	/**
	 * 对模型视图进行处理
	 * 
	 * @param modelId
	 * @param processorId
	 * @param writer
	 */
	void processView(ModelRequestInfo modelRequestInfo, Writer writer,
			Context context);

	/**
	 * 返回指定操作的定义
	 * 
	 * @param model
	 * @param operationId
	 * @return
	 */
	<T> T getOperationDeclare(ModelDefine modelDefine, Object model,
			String operationId);

	/**
	 * 根据模型标识和操作标识返回操作定义
	 * 
	 * @param modelId
	 * @param operationId
	 * @return
	 */
	ModelDefineInfo getModelDefineInfo(ModelRequestInfo modelRequestInfo,
			String modelId, String operationId);

	String getOperationType(ModelDefine modelDefine, Object model,
			String operationId);

	/**
	 * 处理参数
	 * 
	 * @param model
	 * @param processorId
	 * @param writer
	 */
	Context processParameter(ModelRequestInfo modelRequestInfo,
			String parameterBuilderBeanName, Context context);

	List<ModelLoader> getModelLoaders();

	ModelProcessorStage getModelProcessorStage(ModelRequestInfo modelRequestInfo);

	ModelProcessorDefine getModelProcessorDefine(
			ModelRequestInfo modelRequestInfo);

	ModelDefine getModelDefine(Object model);

	/**
	 * 返回操作记录类型
	 * 
	 * @param modelDefineId
	 * @param operationType
	 * @return
	 */
	String getOperationRecordMode(ModelRequestInfo modelRequestInfo,
			String modelId, String operationId);

	/**
	 * 获取参数url
	 * 
	 * @param model
	 * @param operationObject
	 * @param parameter
	 * @return
	 */
	List<String> getParamterList(Object model, Object operationObject);

	/**
	 * 获取指定操作的自定义配置
	 * 
	 * @param model
	 * @param operationId
	 * @return
	 */
	CustomizeStageConfig getCustomizeStageConfig(Object model,
			String operationId, String stageName);

	/**
	 * 
	 * 获取存储模型的容器
	 * 
	 * @return
	 */
	ModelContainer getModelContainer();


	/**
	 * 返回操作的校验规则定义
	 * 
	 * @param model
	 * @param operation
	 * @return
	 */
	Map<String, List<ValidateRule>> getOperationValidateMap(Object model,
			Object operation);
	
}
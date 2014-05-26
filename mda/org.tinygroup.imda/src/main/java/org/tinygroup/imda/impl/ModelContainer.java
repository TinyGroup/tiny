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
package org.tinygroup.imda.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.imda.ModelInformationGetter;
import org.tinygroup.imda.ModelLoader;
import org.tinygroup.imda.config.ModelDefine;
import org.tinygroup.imda.config.ModelProcessorDefine;
import org.tinygroup.imda.config.ModelProcessorStage;
import org.tinygroup.imda.exception.IMdaRuntimeException;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.springutil.SpringUtil;

public class ModelContainer {
	/**
	 * 模型类：模型定义
	 */
	private Map<Class<?>, ModelDefine> modelDefineMap = new HashMap<Class<?>, ModelDefine>();
	/**
	 * 模型ID:模型
	 */
	private Map<String, Object> models = new HashMap<String, Object>();
	/**
	 * 模型类型名称:模型定义
	 */
	private Map<String, ModelDefine> modelDefines = new HashMap<String, ModelDefine>();
	/**
	 * 模型列表
	 */
	private List<ModelLoader> modelLoaders = new ArrayList<ModelLoader>();

	public void addModelDefine(ModelDefine modelDefine) {
		modelDefines.put(modelDefine.getId(), modelDefine);
		String className = modelDefine.getModelClass();
		try {
			modelDefineMap.put(Class.forName(className), modelDefine);
		} catch (ClassNotFoundException e) {
			throw new IMdaRuntimeException("imda.modelClassNotFound",
					modelDefine.getId(), modelDefine.getModelClass());
		}
		String loaderBean = modelDefine.getModelLoaderBean();
		ModelLoader loader = SpringUtil.getBean(loaderBean);
		modelLoaders.add(loader);
	}
	
	public void removeModelDefine(ModelDefine modelDefine) {
		modelDefines.remove(modelDefine.getId());
		String className = modelDefine.getModelClass();
		try {
			modelDefineMap.remove(Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new IMdaRuntimeException("imda.modelClassNotFound",
					modelDefine.getId(), modelDefine.getModelClass());
		}
		String loaderBean = modelDefine.getModelLoaderBean();
		ModelLoader loader = SpringUtil.getBean(loaderBean);
		modelLoaders.remove(loader);
	}

	public Map<String, Object> getModelInstances() {
		return models;
	}

	public Map<String, ModelDefine> getModelDefines() {
		return modelDefines;
	}

	/**
	 * 根据模型对象获取模型类型
	 * 
	 * @param model
	 *            模型对象
	 * @return 模型类型
	 */
	public ModelDefine getModelDefine(Object model) {
		Class<?> modelClass = model.getClass();
		for (Class<?> clazz : modelDefineMap.keySet()) {
			if (clazz.isAssignableFrom(modelClass)) {
				return modelDefineMap.get(clazz);
			}
		}
		throw new IMdaRuntimeException("imda.modelDefineNotFoundByClass",
				modelClass.getName());
	}

	
	/**
	 * 根据模型类型名获取模型类型
	 * 
	 * @param modelDefineName
	 *            模型类型名
	 * @return 模型类型
	 */
	public ModelDefine getModelDefine(String modelDefineName) {
		if (modelDefines.containsKey(modelDefineName))
			return modelDefines.get(modelDefineName);
		throw new IMdaRuntimeException("imda.modelDefineNotFound",
				modelDefineName);
	}

	/**
	 * 根据模型对象、处理器名获取模型处理器
	 * 
	 * @param model
	 *            模型对象
	 * @param operationType
	 *            模型类型的处理器名
	 * @return 模型处理器
	 */
	public ModelProcessorDefine getModelProcessorDefine(String modelDefineName,
			String operationType) {
		ModelDefine define = getModelDefine(modelDefineName);

		return getModelProcessorDefine(define, operationType);
	}

	/**
	 * 获取模型类型的处理器
	 * 
	 * @param define
	 *            模型类型
	 * @param processorType
	 *            处理器名
	 * @return
	 */
	public ModelProcessorDefine getModelProcessorDefine(ModelDefine define,
			String processorType) {
		for (ModelProcessorDefine processorDefine : define
				.getModelProcessorDefines()) {
			if (processorDefine.getName().equalsIgnoreCase(processorType)) {
				return processorDefine;
			}
		}
		throw new IMdaRuntimeException("imda.processorDefineNotFound",
				define.getId(), processorType);
	}

	/**
	 * 根据模型、模型处理器名、处理器子阶段名获取处理器子阶段
	 * 
	 * @param modelId
	 *            模型
	 * @param processorId
	 *            模型处理器名
	 * @param stageName
	 *            模型处理器子阶段
	 * @return 模型处理器子阶段
	 */
	public ModelProcessorStage getModelProcessorStageByModelId(
			ModelRequestInfo modelRequestInfo) {
		ModelProcessorDefine processorDefine = getModelProcessorDefine(
				modelRequestInfo.getModelTypeName(),
				modelRequestInfo.getOperationType());
		return getModelProcessorStage(processorDefine,
				modelRequestInfo.getStageName());
	}

	/**
	 * 根据模型对象、模型处理器名、处理器子阶段名获取处理器子阶段
	 * 
	 * @param model
	 *            模型对象
	 * @param processorId
	 *            模型处理器名
	 * @param stageName
	 *            模型处理器子阶段名
	 * @return 模型处理器子阶段
	 */
	public ModelProcessorStage getModelProcessorStage(
			ModelRequestInfo modelRequestInfo) {
		ModelProcessorDefine processorDefine = getModelProcessorDefine(
				modelRequestInfo.getModelTypeName(),
				modelRequestInfo.getOperationType());
		return getModelProcessorStage(processorDefine,
				modelRequestInfo.getStageName());
	}

	private ModelProcessorStage getModelProcessorStage(
			ModelProcessorDefine processorDefine, String stageName) {
		if (stageName == null) {
			return processorDefine.getProcessorStages().get(0);
		}
		for (ModelProcessorStage stage : processorDefine.getProcessorStages()) {
			if (stage.getName().equalsIgnoreCase(stageName)) {
				return stage;
			}
		}
		throw new IMdaRuntimeException("imda.modelProcessorStageNotFound",
				processorDefine.getName(), stageName);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateModel(Object model) {
		ModelInformationGetter infoBean = getInfoBean(model);
		String id = infoBean.getId(model);
		models.put(id, model);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addModel(Object model) {
		ModelInformationGetter infoBean = getInfoBean(model);
		String id = infoBean.getId(model);
		models.put(id, model);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void removeModel(Object model) {
		ModelInformationGetter infoBean = getInfoBean(model);
		String id = infoBean.getId(model);
		models.remove(id);
	}

	@SuppressWarnings({ "rawtypes" })
	private ModelInformationGetter getInfoBean(Object model) {
		ModelDefine modelDefine = getModelDefine(model);
		String infoBean = modelDefine.getModelInfomationGetterBean();
		return SpringUtil.getBean(infoBean);
	}

	@SuppressWarnings("unchecked")
	public <T> T getModel(String modelId) {
		if (models.containsKey(modelId)) {
			return (T) models.get(modelId);
		}
		throw new IMdaRuntimeException("imda.modelNotFound", modelId);
	}

	public List<ModelLoader> getModelLoaders() {
		return modelLoaders;
	}
}

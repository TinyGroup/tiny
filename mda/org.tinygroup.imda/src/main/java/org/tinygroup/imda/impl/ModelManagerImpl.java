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

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.imda.ModelDefineInfo;
import org.tinygroup.imda.ModelInformationGetter;
import org.tinygroup.imda.ModelLoader;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.imda.config.CustomizeStageConfig;
import org.tinygroup.imda.config.ModelDefine;
import org.tinygroup.imda.config.ModelProcessorDefine;
import org.tinygroup.imda.config.ModelProcessorStage;
import org.tinygroup.imda.processor.ModelServiceProcessor;
import org.tinygroup.imda.processor.ModelViewProcessor;
import org.tinygroup.imda.processor.ParameterBuilder;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.imda.validate.ValidateRule;
import org.tinygroup.springutil.SpringUtil;

public class ModelManagerImpl implements ModelManager {
    Map<String, ModelDefineInfo> modelDefineInfoCache = new HashMap<String, ModelDefineInfo>();
    private ModelContainer container = new ModelContainer();

    public void updateModel(Object model) {
        container.updateModel(model);
    }

    public void addModel(Object model) {
        container.addModel(model);
    }

    public void removeModel(Object model) {
        container.removeModel(model);
    }

    public void addModelDefine(ModelDefine modelDefine) {
        container.addModelDefine(modelDefine);
    }
    
    public void removeModelDefine(ModelDefine modelDefine) {
        container.removeModelDefine(modelDefine);
    }

    public <T> T getModel(String modelId) {
        return (T) container.getModel(modelId);
    }

    public ModelDefine getModelDefine(String modelDefineId) {
        return container.getModelDefine(modelDefineId);
    }

    public ModelProcessorDefine getModelProcessorDefine(String modelDefineId,
                                                        String processorName) {
        ModelDefine modelDefine = getModelDefine(modelDefineId);
        return container.getModelProcessorDefine(modelDefine, processorName);
    }

    public ModelProcessorDefine getModelProcessorDefine(ModelDefine modelDefine,
                                                        String processorName) {
        return getModelProcessorDefine(modelDefine.getId(), processorName);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> T processService(ModelRequestInfo modelRequestInfo, Context context) {
        CustomizeStageConfig customizeConfig = getCustomizeStageConfig(getModel(modelRequestInfo.getModelId()), modelRequestInfo.getProcessorId(), modelRequestInfo.getStageName());
        String serviceProcessorBean;
        if (customizeConfig != null && customizeConfig.getServiceProcessorBean() != null
                && customizeConfig.getServiceProcessorBean().length() > 0) {
            serviceProcessorBean = customizeConfig.getServiceProcessorBean();
        } else {
            ModelProcessorStage processorStage = container.getModelProcessorStage(modelRequestInfo);
            serviceProcessorBean = processorStage.getServiceProcessorBean();
        }

        if (StringUtil.isBlank(serviceProcessorBean)) {
            throw new RuntimeException("找不到服务处理bean:" + serviceProcessorBean);
        }
        ModelServiceProcessor serviceProcessor = SpringUtil.getBean(serviceProcessorBean);
        return (T) serviceProcessor.process(modelRequestInfo, context);
    }

    @SuppressWarnings({"rawtypes"})
    public void processView(ModelRequestInfo modelRequestInfo, Writer writer, Context context) {
        ModelProcessorStage processorStage = container.getModelProcessorStage(modelRequestInfo);
        String viewProcessorBean = processorStage.getViewProcessorBean();
        if (StringUtil.isBlank(viewProcessorBean)) {
            throw new RuntimeException("找不到服务处理bean:" + viewProcessorBean);
        }
        ModelViewProcessor viewProcessor = SpringUtil.getBean(viewProcessorBean);
        viewProcessor.process(modelRequestInfo, context, writer);
    }

    @SuppressWarnings({"rawtypes"})
    public Context processParameter(ModelRequestInfo modelRequestInfo,
                                    String parameterBuilderBeanName, Context context) {
        if (StringUtil.isBlank(parameterBuilderBeanName)) {
            return new ContextImpl();
        }
        ParameterBuilder builder = SpringUtil.getBean(parameterBuilderBeanName);
        return builder.buildParameter(modelRequestInfo, context);
    }

    public List<ModelLoader> getModelLoaders() {
        return container.getModelLoaders();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> T getOperationDeclare(ModelDefine modelDefine, Object model, String operationId) {
        ModelInformationGetter infomationGetter = SpringUtil.getBean(modelDefine.getModelInfomationGetterBean());
        return (T) infomationGetter.getOperation(model, operationId);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getOperationType(ModelDefine modelDefine, Object model, String operationId) {
        ModelInformationGetter infomationGetter = SpringUtil.getBean(modelDefine.getModelInfomationGetterBean());
        return infomationGetter.getOperationType(model, operationId);
    }

    public ModelProcessorStage getModelProcessorStage(ModelRequestInfo modelRequestInfo) {
        return container.getModelProcessorStage(modelRequestInfo);
    }

    public ModelProcessorDefine getModelProcessorDefine(ModelRequestInfo modelRequestInfo) {
        return container.getModelProcessorDefine(modelRequestInfo.getModelTypeName(), modelRequestInfo.getOperationType());
    }

    public String getOperationRecordMode(ModelRequestInfo modelRequestInfo) {
        ModelDefine modelDefine = this.getModelDefine(modelRequestInfo.getModelTypeName());
        for (ModelProcessorDefine processorDefine : modelDefine.getModelProcessorDefines()) {
            if (processorDefine.getName().equalsIgnoreCase(modelRequestInfo.getOperationType())) {
                return processorDefine.getRecordMode();
            }
        }
        return null;
    }

    /**
     * 返回指定模型指定操作的记录操作类型
     */
    public String getOperationRecordMode(ModelRequestInfo modelRequestInfo, String modelId,
                                         String operationId) {
        if (modelId == null || modelId.length() == 0) {// 如果没有指定模型，表示当前模型
            modelId = modelRequestInfo.getModelId();
        }
        Object model = container.getModel(modelId);
        ModelDefine modelDefine = container.getModelDefine(model);
        return getModelProcessorDefine(modelDefine.getId(), getOperationType(modelDefine, model, operationId)).getRecordMode();
    }

    public ModelDefine getModelDefine(ModelRequestInfo modelRequestInfo, String modelId,
                                      String operationId) {
        if (modelId == null || modelId.length() == 0) {// 如果没有指定模型，表示当前模型
            modelId = modelRequestInfo.getModelId();
        }
        Object model = container.getModel(modelId);
        return container.getModelDefine(model);
    }

    public ModelProcessorDefine getModelOperationDefine(ModelRequestInfo modelRequestInfo,
                                                        String modelId, String operationId) {
        ModelDefine modelDefine = getModelDefine(modelRequestInfo, modelId, operationId);
        for (ModelProcessorDefine modelProcessorDefine : modelDefine.getModelProcessorDefines()) {
            if (modelProcessorDefine.getName().equals(this.getOperationType(modelDefine, getModel(modelId), operationId))) {
                return modelProcessorDefine;
            }
        }
        return null;
    }

    public <T> T getOperationDeclare(ModelRequestInfo modelRequestInfo, String modelId,
                                     String operationId) {
        if (modelId == null || modelId.length() == 0) {// 如果没有指定模型，表示当前模型
            modelId = modelRequestInfo.getModelId();
        }
        Object model = container.getModel(modelId);
        ModelDefine modelDefine = container.getModelDefine(model);
        return (T) this.getOperationDeclare(modelDefine, model, operationId);
    }

    public ModelDefineInfo getModelDefineInfo(ModelRequestInfo modelRequestInfo, String modelId,
                                              String operationId) {
        if (modelId == null) {
            modelId = modelRequestInfo.getModelId();
        }
        String key = modelId + "|" + operationId;
        ModelDefineInfo modelDefineInfo = modelDefineInfoCache.get(key);
        if (modelDefineInfo != null) {
            return modelDefineInfo;
        } else {
            modelDefineInfo = new ModelDefineInfo();
            modelDefineInfoCache.put(key, modelDefineInfo);
        }
        Object model = getModel(modelId);
        modelDefineInfo.setModel(model);
        ModelDefine modelDefine = this.getModelDefine(modelRequestInfo, modelId, operationId);
        modelDefineInfo.setModelDefine(modelDefine);
        modelDefineInfo.setModelProcessorDefine(this.getModelOperationDefine(modelRequestInfo, modelId, operationId));
        modelDefineInfo.setProcessDefine(getOperationDeclare(modelRequestInfo, modelId, operationId));
        return modelDefineInfo;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<String> getParamterList(Object model, Object operationObject) {
        ModelInformationGetter infomationGetter = SpringUtil.getBean(container.getModelDefine(model).getModelInfomationGetterBean());
        return infomationGetter.getParamterList(model, operationObject);
    }

    public ModelDefine getModelDefine(Object model) {
        return container.getModelDefine(model);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public CustomizeStageConfig getCustomizeStageConfig(Object model, String operationId,
                                                        String stageName) {
        ModelInformationGetter infomationGetter = SpringUtil.getBean(container.getModelDefine(model).getModelInfomationGetterBean());
        return infomationGetter.getCustomizeStageConfig(model, operationId, stageName);
    }

    public ModelContainer getModelContainer() {
        return container;
    }


    public Map<String, List<ValidateRule>> getOperationValidateMap(Object model, Object operation) {
        ModelInformationGetter<Object> infomationGetter = SpringUtil.getBean(container.getModelDefine(model).getModelInfomationGetterBean());
        return infomationGetter.getOperationValidateMap(model, operation);
    }

}

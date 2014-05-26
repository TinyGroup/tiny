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
package org.tinygroup.flow.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.tinygroup.annotation.AnnotationClassAction;
import org.tinygroup.annotation.AnnotationPropertyAction;
import org.tinygroup.commons.tools.AnnotationUtils;
import org.tinygroup.event.Parameter;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.annotation.config.ComponentDefine;
import org.tinygroup.flow.annotation.config.ComponentParameter;
import org.tinygroup.flow.annotation.config.ComponentResult;
import org.tinygroup.flow.config.Result;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;

/**
 * 流程组件注解处理器
 * @author renhui
 *
 */
public class FlowComponentAnnotationAction implements AnnotationClassAction,AnnotationPropertyAction {

	private FlowExecutor executor;
	
	private Logger logger=LoggerFactory.getLogger(FlowComponentAnnotationAction.class);
	
	public FlowExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(FlowExecutor executor) {
		this.executor = executor;
	}

	public <T> void process(Class<T> clazz, Annotation annotation) {
		if(executor==null){
			executor=SpringUtil.getBean(FlowExecutor.FLOW_BEAN);
		}
		
		ComponentDefine annoDefine = AnnotationUtils.findAnnotation(clazz,
				ComponentDefine.class);
		org.tinygroup.flow.config.ComponentDefine componentDefine = new org.tinygroup.flow.config.ComponentDefine();
        setBasicProperty(annoDefine, componentDefine);
        executor.addComponent(componentDefine);
	}

	private void setBasicProperty(ComponentDefine annoDefine,
			org.tinygroup.flow.config.ComponentDefine componentDefine) {
		componentDefine.setName(annoDefine.name());
        componentDefine.setBean(annoDefine.bean());
        componentDefine.setCategory(annoDefine.category());
        componentDefine.setIcon(annoDefine.icon());
        componentDefine.setShortDescription(annoDefine.shortDescription());
        componentDefine.setLongDescription(annoDefine.longDescription());
        componentDefine.setTitle(annoDefine.title());
	}

	public <T> void process(Class<T> clazz, Field field, Annotation annotation) {
		ComponentDefine annoDefine = AnnotationUtils.findAnnotation(clazz,
				ComponentDefine.class);
		String componentName=annoDefine.name();
		org.tinygroup.flow.config.ComponentDefine componentDefine=executor.getComponentDefine(componentName);
		if(componentDefine!=null){
			if(annotation.annotationType().isAssignableFrom(ComponentParameter.class)){
				ComponentParameter annoParameter=(ComponentParameter)annotation;
				Parameter parameter=createParameter(annoParameter);
				componentDefine.addParamter(parameter);
			}else if(annotation.annotationType().isAssignableFrom(ComponentResult.class)){
				ComponentResult componentResult=(ComponentResult)annotation;
				Result result=createResult(componentResult);
				componentDefine.addResult(result);
			}			
		}else{
			logger.logMessage(LogLevel.WARN, "不存在组件名称为：[{0}]的组件信息",componentName);
		}
	}

	private Result createResult(ComponentResult componentResult) {
		Result result=new Result();
		result.setArray(componentResult.array());
		result.setDescription(componentResult.description());
		result.setName(componentResult.name());
		result.setRequired(componentResult.required());
		result.setTitle(componentResult.title());
		result.setType(componentResult.type());
		return result;
	}

	private Parameter createParameter(ComponentParameter annoParameter) {
		Parameter parameter=new Parameter();
		parameter.setArray(annoParameter.array());
		parameter.setCollectionType(annoParameter.collectionType());
		parameter.setDescription(annoParameter.description());
		parameter.setName(annoParameter.name());
		parameter.setRequired(annoParameter.required());
		parameter.setScope(annoParameter.scope());
		parameter.setTitle(annoParameter.title());
		parameter.setType(annoParameter.type());
		parameter.setValidatorSence(annoParameter.validatorScene());
		return parameter;
	}

}

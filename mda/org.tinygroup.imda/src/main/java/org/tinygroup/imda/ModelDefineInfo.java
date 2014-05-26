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

import org.tinygroup.imda.config.ModelDefine;
import org.tinygroup.imda.config.ModelProcessorDefine;
import org.tinygroup.springutil.SpringUtil;

/**
 * 模型相关定义信息
 * 
 * @author luoguo
 * 
 */
public class ModelDefineInfo {
	ModelDefine modelDefine;// 模型定义
	ModelProcessorDefine modelProcessorDefine;// 模型处理定义
	Object model; // 模型
	Object processDefine;// 模型操作
	private String visitUrl;

	public ModelDefine getModelDefine() {
		return modelDefine;
	}

	public void setModelDefine(ModelDefine modelDefine) {
		this.modelDefine = modelDefine;
	}

	public ModelProcessorDefine getModelProcessorDefine() {
		return modelProcessorDefine;
	}

	public void setModelProcessorDefine(
			ModelProcessorDefine modelProcessorDefine) {
		this.modelProcessorDefine = modelProcessorDefine;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public Object getProcessDefine() {
		return processDefine;
	}

	public void setProcessDefine(Object processDefine) {
		this.processDefine = processDefine;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getVisitUrl(){
		if(visitUrl==null){
			ModelInformationGetter getter=SpringUtil.getBean(modelDefine.getModelInfomationGetterBean());
			visitUrl=modelDefine.getId()+"_"+modelProcessorDefine.getName()+"_"+getter.getId(model)+"_"+getter.getOperationId(processDefine);
		}
		return visitUrl;
	}
}

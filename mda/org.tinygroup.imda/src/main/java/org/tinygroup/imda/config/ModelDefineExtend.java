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
package org.tinygroup.imda.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 用于定义模型
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("model-define-extend")
public class ModelDefineExtend {
	@XStreamAsAttribute
	private String id;// 模型类型标识

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XStreamAlias("model-processor-defines")
	private List<ModelProcessorDefine> modelProcessorDefines;// 模型处理器定义

	public List<ModelProcessorDefine> getModelProcessorDefines() {
		if (modelProcessorDefines == null)
			modelProcessorDefines = new ArrayList<ModelProcessorDefine>();
		return modelProcessorDefines;
	}

	public void setModelProcessorDefines(
			List<ModelProcessorDefine> modelProcessorDefines) {
		this.modelProcessorDefines = modelProcessorDefines;
	}

}

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
package org.tinygroup.entity.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 对象引用类
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("reference")
public class Reference {
	@XStreamAsAttribute
	String type;// 引用类型
	@XStreamAsAttribute
	@XStreamAlias("id")
	String id;// 引用的标识
	@XStreamAsAttribute
	@XStreamAlias("model-id")
	String modelId;// 所属模型，如果是当前模型，可以忽略
	@XStreamAsAttribute
	@XStreamAlias("show-mode")
	String showMode;// 模式对话框modalDialog，对话框Dialog，替换当前位置replace

	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

}

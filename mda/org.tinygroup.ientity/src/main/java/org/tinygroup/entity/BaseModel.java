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
package org.tinygroup.entity;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.entity.base.BaseObject;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.View;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 模型
 * 
 * @author luoguo
 * 
 */
public class BaseModel extends BaseObject {

	@XStreamAsAttribute
	@XStreamAlias("abstract-model")
	Boolean abstractModel;// 是否抽象模型，如果是抽象模型，只能够用来被继承

	@XStreamAsAttribute
	private String version;// 版本

	@XStreamAsAttribute
	@XStreamAlias("parent-model-id")
	String parentModelId;// 如果是继承自另外的模型，这里可以输入，只能是单继承

	@XStreamAlias("operations")
	List<Operation> operations;// 操作

	@XStreamAlias("views")
	List<View> views;// 视图

	public Boolean getAbstractModel() {
		return abstractModel;
	}

	public void setAbstractModel(Boolean abstractModel) {
		this.abstractModel = abstractModel;
	}

	public String getParentModelId() {
		return parentModelId;
	}

	public void setParentModelId(String parentModelId) {
		this.parentModelId = parentModelId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Operation> getOperations() {
		if (operations == null)
			operations = new ArrayList<Operation>();
		return operations;
	}

	public void setOperations(List<Operation> operations) {
			this.operations = operations;
	}

	public List<View> getViews() {
		if (views == null)
			views = new ArrayList<View>();
		return views;
	}

	public void setViews(List<View> views) {
		this.views = views;
	}
	
}

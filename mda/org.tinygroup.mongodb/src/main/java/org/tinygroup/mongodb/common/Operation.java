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
package org.tinygroup.mongodb.common;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.imda.config.CustomizeStageConfig;
import org.tinygroup.imda.config.PageConfig;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("operation")
public class Operation extends BaseObject {
	@XStreamAlias("condition-fields")
	List<ConditionField> conditionFields;// 条件字段
	@XStreamAlias("operation-group")
	OperationGroup operationGroup;// 操作分组
	@XStreamAsAttribute
	String type;// 操作类型，可以是新增，删除，更新，查看等等

	@XStreamAsAttribute
	@XStreamAlias("permission-check")
	Boolean permissionCheck;// 是否进行权限检查，如果需要权限检查，则会自动注册权限，并进行权限校验
	@XStreamAlias("references")
	List<Reference> references;// 关联操作
	@XStreamAsAttribute
	String width;
	@XStreamAsAttribute
	String height;
	@XStreamAsAttribute
	@XStreamAlias("fixed-size")
	boolean fixedSize;
	@XStreamAsAttribute
	@XStreamAlias("modal")
	boolean modal;
	@XStreamAsAttribute
	@XStreamAlias("customize-stage-configs")
	List<CustomizeStageConfig> customizeStageConfigs;
	@XStreamAlias("front-event-defines")
	List<FrontEventDefine> frontEventDefines;
	@XStreamAlias("css-define")
	String cssDefine;// 扩展定义的任意css定义
	@XStreamAlias("js-define")
	String jsDefine;// 扩展定义的任意js脚本
	@XStreamAlias("page-config")
	PageConfig pageConfig;

	public String getCssDefine() {
		return cssDefine;
	}

	public void setCssDefine(String cssDefine) {
		this.cssDefine = cssDefine;
	}

	public String getJsDefine() {
		return jsDefine;
	}

	public void setJsDefine(String jsDefine) {
		this.jsDefine = jsDefine;
	}

	public List<FrontEventDefine> getFrontEventDefines() {
		if (frontEventDefines == null) {
			return new ArrayList<FrontEventDefine>();
		}
		return frontEventDefines;
	}

	public void setFrontEventDefines(List<FrontEventDefine> frontEventDefines) {
		this.frontEventDefines = frontEventDefines;
	}

	public List<CustomizeStageConfig> getCustomizeStageConfigs() {
		if (customizeStageConfigs == null)
			customizeStageConfigs = new ArrayList<CustomizeStageConfig>();
		return customizeStageConfigs;
	}

	public void setCustomizeStageConfigs(
			List<CustomizeStageConfig> customizeStageConfigs) {
		this.customizeStageConfigs = customizeStageConfigs;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public boolean isFixedSize() {
		return fixedSize;
	}

	public void setFixedSize(boolean fixedSize) {
		this.fixedSize = fixedSize;
	}

	public String getWidth() {
		if (width == null || width.length() == 0) {
			width = "400";
		}
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		if (height == null || height.length() == 0) {
			height = "150";
		}
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public List<Reference> getReferences() {
		return references;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public Boolean getPermissionCheck() {
		return permissionCheck;
	}

	public void setPermissionCheck(Boolean permissionCheck) {
		this.permissionCheck = permissionCheck;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ConditionField> getConditionFields() {
		if (conditionFields == null)
			conditionFields = new ArrayList<ConditionField>();
		return conditionFields;
	}

	public void setConditionFields(List<ConditionField> conditionFields) {
		this.conditionFields = conditionFields;
	}

	public OperationGroup getOperationGroup() {
		return operationGroup;
	}

	public void setOperationGroup(OperationGroup operationGroup) {
		this.operationGroup = operationGroup;
	}

	public PageConfig getPageConfig() {
		if(pageConfig==null){
			pageConfig=new PageConfig();
		}
		return pageConfig;
	}

	public void setPageConfig(PageConfig pageConfig) {
		this.pageConfig = pageConfig;
	}

    
	
}

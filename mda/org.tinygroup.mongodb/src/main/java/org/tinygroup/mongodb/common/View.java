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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 视图，用于展现数据
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("view")
public class View extends BaseObject {
	@XStreamAsAttribute
	String type;// 显示风格，card，tree，树表，table，chart，等等
	// 条件属性
	@XStreamAsAttribute
	@XStreamAlias("display-mode")
	String displayMode;// 分组显示模式，可以是Tab,Group,Wizard,Expander,Carousel，一般仅用在卡片方式中

	@XStreamAsAttribute
	@XStreamAlias("condition-groups")
	String conditionGroups;// 条件分组，多个用逗号分隔，在查询的时候，可以出现多个查询Tab页

	@XStreamAlias("condition-fields")
	List<ConditionField> conditionFields;
	// 分类属性
	@XStreamAlias("category-fields")
	List<CategoryField> categoryFields;
	// 视图属性，视图字段分组
	@XStreamAlias("view-groups")
	List<ViewGroup> viewGroups;
	// 排序属性 order by
	@XStreamAlias("order-fields")
	List<OrderField> orderFields;
	// group by
	@XStreamAlias("group-fields")
	List<GroupField> groupFields;
	@XStreamAlias("having-fields")
	List<HavingField> havingFields;
	@XStreamAsAttribute
	@XStreamAlias("page-size")
	int pageSize;// 分页大小

	@XStreamAlias("references")
	List<Reference> references;// 关联操作

	@XStreamAsAttribute
	@XStreamAlias("allow-edit")
	boolean allowEdit;// 是否允许在表格中进行修改
	@XStreamAsAttribute
	@XStreamAlias("allow-filter-front")
	boolean allowFilterFront;// 是否允许前台过滤，只有frontPaging=true时才生效
	@XStreamAsAttribute
	@XStreamAlias("permission-check")
	Boolean permissionCheck;// 是否进行权限检查，如果需要权限检查，则会自动注册权限，并进行权限校验
	@XStreamAsAttribute
	String width;// 窗口
	@XStreamAsAttribute
	String height;// 窗口高度
	@XStreamAsAttribute
	@XStreamAlias("fixed-size")
	boolean fixedSize;// 是否固定大小，不允许前台调整
	@XStreamAsAttribute
	@XStreamAlias("modal")
	boolean modal;// 是否显示为模式窗口

	@XStreamAsAttribute
	@XStreamAlias("front-paging")
	//
	boolean frontPaging;// 是否前台分布
	@XStreamAsAttribute
	@XStreamAlias("customize-stage-configs")
	List<CustomizeStageConfig> customizeStageConfigs;// 自定义处理的配置
	@XStreamAlias("front-event-defines")
	List<FrontEventDefine> frontEventDefines;// 前台事件定义
	@XStreamAlias("css-define")
	String cssDefine;// 扩展定义的任意css定义
	@XStreamAlias("js-define")
	String jsDefine;// 扩展定义的任意js脚本
	@XStreamAlias("mongo-relations")
    private List<MongoRelation> relations;

	public String getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}



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

	public boolean isFrontPaging() {
		return frontPaging;
	}

	public void setFrontPaging(boolean frontPaging) {
		this.frontPaging = frontPaging;
	}

	public boolean isAllowFilterFront() {
		return allowFilterFront;
	}

	public void setAllowFilterFront(boolean allowFilterFront) {
		this.allowFilterFront = allowFilterFront;
	}

	public boolean isAllowEdit() {
		return allowEdit;
	}

	public void setAllowEdit(boolean allowEdit) {
		this.allowEdit = allowEdit;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
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
			width = "800";
		}
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		if (height == null || height.length() == 0) {
			height = "600";
		}
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public Boolean getPermissionCheck() {
		return permissionCheck;
	}

	public void setPermissionCheck(Boolean permissionCheck) {
		this.permissionCheck = permissionCheck;
	}

	public List<Reference> getReferences() {
		if (references == null) {
			references = new ArrayList<Reference>();
		}
		return references;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
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

	public List<CategoryField> getCategoryFields() {
		if (categoryFields == null)
			categoryFields = new ArrayList<CategoryField>();
		return categoryFields;
	}

	public void setCategoryFields(List<CategoryField> categoryFields) {
		this.categoryFields = categoryFields;
	}

	public List<ViewGroup> getViewGroups() {
		if (viewGroups == null)
			viewGroups = new ArrayList<ViewGroup>();
		return viewGroups;
	}

	public void setViewGroups(List<ViewGroup> viewGroups) {
		this.viewGroups = viewGroups;
	}

	public List<OrderField> getOrderFields() {
		if (orderFields == null)
			orderFields = new ArrayList<OrderField>();
		return orderFields;
	}

	public String getConditionGroups() {
		return conditionGroups;
	}

	public void setConditionGroups(String conditionGroups) {
		this.conditionGroups = conditionGroups;
	}

	public void setOrderFields(List<OrderField> orderFields) {
		this.orderFields = orderFields;
	}

	public List<GroupField> getGroupFields() {
		if (groupFields == null)
			groupFields = new ArrayList<GroupField>();
		return groupFields;
	}

	public void setGroupFields(List<GroupField> groupFields) {
		this.groupFields = groupFields;
	}

	public List<HavingField> getHavingFields() {
		if(havingFields==null){
			havingFields=new ArrayList<HavingField>();
		}
		return havingFields;
	}

	public void setHavingFields(List<HavingField> havingFields) {
		this.havingFields = havingFields;
	}

	public List<MongoRelation> getRelations() {
		if(relations==null){
			relations=new ArrayList<MongoRelation>();
		}
		return relations;
	}

	public void setRelations(List<MongoRelation> relations) {
		this.relations = relations;
	}
	

}

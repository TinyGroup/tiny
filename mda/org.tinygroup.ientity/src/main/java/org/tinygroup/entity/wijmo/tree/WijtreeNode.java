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
package org.tinygroup.entity.wijmo.tree;

import java.util.List;

public class WijtreeNode {
	String text;//显示的文本
	String value;
	String id;
	boolean expanded;// 是否展开
	boolean checked;// 是否检查框选中
	boolean selected;// 是否是当前选中节点
	boolean disabled;
	String toolTip;// 提示信息
	String navigateUrl;// url
	String collapsedIconClass;// 折叠时图标
	String expandedIconClass;// 展开时图标
	String itemIconClass;// 图标
	List<WijtreeNode> nodes;// 子节点

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public String getNavigateUrl() {
		return navigateUrl;
	}

	public void setNavigateUrl(String navigateUrl) {
		this.navigateUrl = navigateUrl;
	}

	public String getCollapsedIconClass() {
		return collapsedIconClass;
	}

	public void setCollapsedIconClass(String collapsedIconClass) {
		this.collapsedIconClass = collapsedIconClass;
	}

	public String getExpandedIconClass() {
		return expandedIconClass;
	}

	public void setExpandedIconClass(String expandedIconClass) {
		this.expandedIconClass = expandedIconClass;
	}

	public String getItemIconClass() {
		return itemIconClass;
	}

	public void setItemIconClass(String itemIconClass) {
		this.itemIconClass = itemIconClass;
	}

	public List<WijtreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<WijtreeNode> nodes) {
		this.nodes = nodes;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

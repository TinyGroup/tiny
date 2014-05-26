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
package org.tinygroup.entity.base;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 基本对象<br>
 * 用于描述数据的基础信息
 * 
 * @author luog
 * 
 */
public class BaseObject {
	@XStreamAsAttribute
	protected String id;// 唯一标识
	@XStreamAsAttribute
	protected String name;// 英文名称
	@XStreamAsAttribute
	protected String aliasName;// 别名
	@XStreamAsAttribute
	protected String title;// 中文标题
	protected String description;// 描述
	@XStreamAsAttribute
	@XStreamAlias("enable-delete")
	protected Boolean enableDelete;// 是否允许删除
	@XStreamAsAttribute
	@XStreamAlias("enable-modity")
	protected Boolean enableModify;// 是否允许修改
	@XStreamAlias("extend-information")
	protected String extendInformation;// 扩展信息，具体是什么不做规定，用于自行进行规范制定与扩展
	@XStreamAsAttribute
	String help;// 帮助信息

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getExtendInformation() {
		return extendInformation;
	}

	public void setExtendInformation(String extendInformation) {
		this.extendInformation = extendInformation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnableDelete() {
		return enableDelete;
	}

	public void setEnableDelete(Boolean enableDelete) {
		this.enableDelete = enableDelete;
	}

	public Boolean getEnableModify() {
		return enableModify;
	}

	public void setEnableModify(Boolean enableModify) {
		this.enableModify = enableModify;
	}
	

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public BaseObject() {
	}

}
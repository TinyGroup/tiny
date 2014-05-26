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
package org.tinygroup.factory.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("bean")
public class Bean {
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String scope;// singleton共享，prototype每次新
							// //后面三种对于框架来说，用不着,session,request,global
	@XStreamAsAttribute
	private String type;// 接口名称
	@XStreamAsAttribute
	@XStreamAlias("class")
	private String className;
	@XStreamAsAttribute
	private String autowire;
	@XStreamImplicit
	private List<Property> properties;

	public String getAutowire() {
		return autowire;
	}

	public void setAutowire(String autowire) {
		this.autowire = autowire;
	}

	public String getScope() {
		if (scope == null || scope.length() == 0) {
			return "singleton";
		}
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String id) {
		this.name = id;
	}

	public String getType() {
		if (type == null) {
			type = className;
		}
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> property) {
		this.properties = property;
	}

}

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
package org.tinygroup.validate.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("validator")
public class ValidatorConfig {
	@XStreamAsAttribute
	@XStreamAlias("bean-name")
	private String validatorBeanName;
	@XStreamAsAttribute
	@XStreamAlias("class-name")
	private String validatorClassName;
	@XStreamAsAttribute
	@XStreamAlias("properties")
	private List<Property> properties;
	@XStreamAsAttribute
	private String scene;
	
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	public String getValidatorBeanName() {
		return validatorBeanName;
	}
	public void setValidatorBeanName(String validatorBeanName) {
		this.validatorBeanName = validatorBeanName;
	}
	public List<Property> getProperties() {
		if(properties==null)
			properties = new ArrayList<Property>();
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public String getValidatorClassName() {
		return validatorClassName;
	}
	public void setValidatorClassName(String validatorClassName) {
		this.validatorClassName = validatorClassName;
	}
	
}

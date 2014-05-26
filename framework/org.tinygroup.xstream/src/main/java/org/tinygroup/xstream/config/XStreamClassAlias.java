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
package org.tinygroup.xstream.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("class-alias")
public class XStreamClassAlias {
	@XStreamAsAttribute
	@XStreamAlias("alias-name")
	private String aliasName;
	@XStreamAsAttribute
	private String type;
	@XStreamAlias("proper-aliases")
	private List<XStreamPropertyAlias> properAliases;
	@XStreamAlias("property-implicits")
	private List<XStreamPropertyImplicit> propertyImplicits;
	@XStreamAlias("property-omits")
	private List<XStreamPropertyOmit> propertyOmits;


	public List<XStreamPropertyAlias> getProperAliases() {
		return properAliases;
	}

	public void setProperAliases(List<XStreamPropertyAlias> properAliases) {
		this.properAliases = properAliases;
	}

	public List<XStreamPropertyImplicit> getPropertyImplicits() {
		return propertyImplicits;
	}

	public void setPropertyImplicits(
			List<XStreamPropertyImplicit> propertyImplicits) {
		this.propertyImplicits = propertyImplicits;
	}

	public List<XStreamPropertyOmit> getPropertyOmits() {
		return propertyOmits;
	}

	public void setPropertyOmits(List<XStreamPropertyOmit> propertyOmits) {
		this.propertyOmits = propertyOmits;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

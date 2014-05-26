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
package org.tinygroup.context2object.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("generator-config")
public class GeneratorConfig {
	@XStreamAlias("type-converters")
	private List<GeneratorConfigItem> typeConverters;
	@XStreamAlias("type-creators")
	private List<GeneratorConfigItem> typeCreators;

	public List<GeneratorConfigItem> getTypeConverters() {
		if (typeConverters == null) {
			typeConverters = new ArrayList<GeneratorConfigItem>();
		}
		return typeConverters;
	}

	public void setTypeConverters(List<GeneratorConfigItem> typeConverters) {
		this.typeConverters = typeConverters;
	}

	public List<GeneratorConfigItem> getTypeCreators() {
		if (typeCreators == null) {
			typeCreators = new ArrayList<GeneratorConfigItem>();
		}
		return typeCreators;
	}

	public void setTypeCreators(List<GeneratorConfigItem> typeCreators) {
		this.typeCreators = typeCreators;
	}

}

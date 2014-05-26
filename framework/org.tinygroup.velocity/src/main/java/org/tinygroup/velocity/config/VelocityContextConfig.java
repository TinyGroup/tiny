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
package org.tinygroup.velocity.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("velocity-context-config")
public class VelocityContextConfig {
	@XStreamAlias("static-classes")
	private List<StaticClass> staticClasses;
	@XStreamAlias("platform-beans")
	private List<Bean> platformBeans;
	@XStreamAlias("spring-beans")
	private List<Bean> springBeans;

	public List<StaticClass> getStaticClasses() {
		return staticClasses;
	}

	public void setStaticClasses(List<StaticClass> staticClasses) {
		this.staticClasses = staticClasses;
	}

	public List<Bean> getPlatformBeans() {
		return platformBeans;
	}

	public void setPlatformBeans(List<Bean> platformBeans) {
		this.platformBeans = platformBeans;
	}

	public List<Bean> getSpringBeans() {
		return springBeans;
	}

	public void setSpringBeans(List<Bean> springBeans) {
		this.springBeans = springBeans;
	}

}

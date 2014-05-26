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
package org.tinygroup.bundle.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 这里的Bundle，取其杂物箱之意 BundleDefine，则是杂物箱定义的意思，它以静态的方式定义了杂物箱的名字，依赖其它的杂物箱及公共文件
 * Created by luoguo on 2014/5/4.
 */
@XStreamAlias("bundle")
public class BundleDefine {
	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String title;

	// 所依赖的bundle,以,分割
	@XStreamAsAttribute
	private String dependencies;

	// 这里只存放放在公共jar目录的jar,放在当前的bundle目录下的jar不需要配置到这里
	@XStreamAlias("common-jars")
	@XStreamAsAttribute
	private String commonJars;

	@XStreamAlias("short-description")
	@XStreamAsAttribute
	private String shortDescription;

	@XStreamAlias("long-description")
	@XStreamAsAttribute
	private String longDescription;
	// 值为其对应的bean
	@XStreamAlias("bundle-activator")
	@XStreamAsAttribute
	private String bundleActivator;
	private transient String[] dependencyArray = null;

	public String getBundleActivator() {
		return bundleActivator;
	}

	public void setBundleActivator(String bundleActivator) {
		this.bundleActivator = bundleActivator;
	}


	public boolean isDependency(String name) {
		for (String key : getDependencyArray()) {
			if (key.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public String[] getDependencyArray() {
		if (dependencyArray == null) {
			if (dependencies == null || dependencies.trim().length() == 0) {
				dependencyArray = new String[0];
			} else {
				dependencyArray = dependencies.split(",");
			}
		}
		return dependencyArray;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDependencies() {
		if (dependencies == null) {
			dependencies = "";
		}
		return dependencies;
	}

	public void setDependencies(String dependencies) {
		this.dependencies = dependencies;
	}

	public String getCommonJars() {
		if (commonJars == null) {
			commonJars = "";
		}
		return commonJars;
	}

	public void setCommonJars(String commonJars) {
		this.commonJars = commonJars;
	}
}

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
package org.tinygroup.imda.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("customize-stage-config")
public class CustomizeStageConfig {
	@XStreamAsAttribute
	@XStreamAlias("stage-name")
	String stageName;// 自定义阶段名称
	@XStreamAsAttribute
	@XStreamAlias("service-processor-bean")
	String serviceProcessorBean;// 服务处理Bean
	@XStreamAsAttribute
	@XStreamAlias("parameter-builder-bean")
	String parameterBuilderBean;// 参数构建Bean
	@XStreamAsAttribute
	@XStreamAlias("view-path")
	String viewPath;// 自定义展现路径

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getServiceProcessorBean() {
		return serviceProcessorBean;
	}

	public void setServiceProcessorBean(String serviceProcessorBean) {
		this.serviceProcessorBean = serviceProcessorBean;
	}

	public String getParameterBuilderBean() {
		return parameterBuilderBean;
	}

	public void setParameterBuilderBean(String parameterBuilderBean) {
		this.parameterBuilderBean = parameterBuilderBean;
	}

	public String getViewPath() {
		return viewPath;
	}

	public void setViewPath(String viewPath) {
		this.viewPath = viewPath;
	}

}

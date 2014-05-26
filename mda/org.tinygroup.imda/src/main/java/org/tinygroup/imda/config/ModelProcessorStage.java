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

@XStreamAlias("model-processor-stage")
public class ModelProcessorStage {

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String title;

	@XStreamAsAttribute
	@XStreamAlias("service-processor")
	private String serviceProcessorBean;// 服务处理器bean

	@XStreamAsAttribute
	@XStreamAlias("parameter-builder")
	private String parameterBuilderBean;;// 参数构建器bean

	@XStreamAsAttribute
	@XStreamAlias("view-processor")
	private String viewProcessorBean;// 视图处理器Bean
	@XStreamAsAttribute
	@XStreamAlias("need-validate")
	boolean needValidate;

	public boolean isNeedValidate() {
		return needValidate;
	}

	public void setNeedValidate(boolean needValid) {
		this.needValidate = needValid;
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

	public String getViewProcessorBean() {
		return viewProcessorBean;
	}

	public void setViewProcessorBean(String viewProcessorBean) {
		this.viewProcessorBean = viewProcessorBean;
	}

}
